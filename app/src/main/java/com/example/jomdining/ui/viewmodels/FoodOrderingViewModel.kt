package com.example.jomdining.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jomdining.JomDiningApplication
import com.example.jomdining.data.JomDiningRepository
import com.example.jomdining.data.OfflineRepository
import com.example.jomdining.data.UserPreferencesRepository
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.ui.components.MenuUi
import com.example.jomdining.ui.components.OrderItemUi
import com.example.jomdining.ui.components.TransactionsUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FoodOrderingViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var menuUi by mutableStateOf(MenuUi())
        private set

    var orderItemUi by mutableStateOf(OrderItemUi())
        private set

    var transactionsUi by mutableStateOf(TransactionsUi())
        private set

    // All variables used in FoodOrderingModuleScreen
    private val _activeTransaction = MutableLiveData<Transactions?>()
    val activeTransaction: LiveData<Transactions?> get() = _activeTransaction

    fun getAllMenuItemsExceptRetired() {
        viewModelScope.launch {
            menuUi = menuUi.copy(
                menuItems = repository.getAllMenuItemsExceptRetired()
                    .filterNotNull()
                    .first()
            )
        }
    }

    fun getCurrentActiveTransaction(accountID: Int) {
        viewModelScope.launch {
            val transaction = repository.getCurrentActiveTransactionStream(accountID)
            _activeTransaction.value = transaction

            // The fetched current active transaction will be stored in this mutableList
            val currentActiveTransactionList = mutableListOf<Transactions>()

            // Also, the fetched Transaction object will be stored in this val
            val currentActiveTransaction = repository.getCurrentActiveTransactionStream(accountID)
            Log.d(
                "CAT_fetch",
                "Successfully fetched current active transaction: $currentActiveTransaction"
            )

            // Update TransactionsUi with the new current active transaction
            currentActiveTransactionList.add(currentActiveTransaction)
            transactionsUi = transactionsUi.copy(
                currentActiveTransactionList = currentActiveTransactionList
            )
            Log.d(
                "CAT_toList",
                "Details of current active transaction moved to List: $currentActiveTransactionList"
            )

            // Then, using the fetched Transactions object, fetched all its order items
            getAllCurrentOrderItems(currentActiveTransaction.transactionID)
            Log.d(
                "CAT_orderItems",
                "Successfully fetched all order items under transaction with ID ${currentActiveTransaction.transactionID}"
            )
        }
    }

    fun addNewOrIncrementOrderItem(transactionID: Int, menuItemID: Int, operationFlag: Int) {
        // The operation flag will be used to decide which control flow to use.
        // operationFlag = 1 -> add new order item to the list, operationFlag = 2 -> increment existing orderItemQuantity
        viewModelScope.launch {
            if (operationFlag == 1) {   // operationFlag = 1 -> add new item to the list
                try {
                    // invoke the function that inserts a new OrderItem to the DB
                    val currentOrderItems = repository.getAllOrderItemsByTransactionIDStream(transactionID)
                        .filterNotNull()
                        .first()
                    Log.d("AddOrInc_OF1_COI", "currentOrderItems content: $currentOrderItems")
                    var isNewOrderItem = true
                    for (orderItem in currentOrderItems) {
                        if (orderItem.menuItemID == menuItemID) {
                            isNewOrderItem = false
                            break
                        }
                    }
                    // Log.d("ANOIOI_IsNewOrderItem", "With $menuItemID, value of isNewOrderItem returned as $isNewOrderItem")
                    if (isNewOrderItem) {
                        // invoke the function that adds a new order item
                        repository.addNewOrderItemStream(transactionID, menuItemID)
                        Log.d("AddOrInc_OF1_PASS1", "New OrderItem added to the currently active transaction list.")
                    } else {
                        // invoke the function that increments orderItemQuantity
                        repository.increaseOrderItemQuantityStream(transactionID, menuItemID)
                        Log.d("AddOrInc_OF1_PASS2", "OrderItems exists in list. Existing orderItemQuantity increased by 1.")
                    }
                } catch (e: Exception) {
                    Log.e("AddOrInc_OF1_FAIL", "Failed to add new OrderItem to currently active transaction list: $e")
                }
            } else if (operationFlag == 2) {    // operationFlag = 2 -> increase existing orderItemQuantity
                try {
                    // invoke the function that increments orderItemQuantity
                    repository.increaseOrderItemQuantityStream(transactionID, menuItemID)
                    Log.d("AddOrInc_OF2_PASS", "Existing orderItemQuantity increased by 1.")
                } catch (e: Exception) {
                    Log.e("AddOrInc_OF2_FAIL", "Failed to increase existing orderItemQuantity by 1: $e")
                }
            }
            getAllCurrentOrderItems(transactionID)
        }
    }

    fun deleteOrDecrementOrderItem(transactionID: Int, menuItemID: Int, operationFlag: Int) {
        // The operation flag will be used to decide which control flow to use.
        // operationFlag = 1 -> delete order item from the list, operationFlag = 2 -> decrement existing orderItemQuantity
        viewModelScope.launch {
            if (operationFlag == 1) {   // operationFlag = 1 -> delete order item from the list
                try {
                    // invoke the function that deletes an OrderItem from the DB
                    repository.deleteOrderItemStream(transactionID, menuItemID)
                    Log.d("DelOrDec_OF1_PASS", "OrderItem deleted from the current active transaction list.")
                } catch (e: Exception) {
                    Log.e("DelOrDec_OF1_FAIL", "Failed to delete OrderItem from currently active transaction list: $e")
                }
            } else if (operationFlag == 2) {    // operationFlag = 2 -> decrement existing orderItemQuantity
                try {
                    // invoke the function that decrements orderItemQuantity
                    repository.decreaseOrderItemQuantityStream(transactionID, menuItemID)
                    Log.d("DelOrDec_OF2_PASS", "Existing orderItemQuantity decreased by 1.")
                } catch (e: Exception) {
                    Log.e("DelOrDec_OF2_FAIL", "Failed to decrease existing orderItemQuantity by 1: $e")
                }
            }
            getAllCurrentOrderItems(transactionID)
        }
    }

    fun confirmAndFinalizeTransaction(
        transactionID: Int,
        transactionDateTime: String,
        transactionMethod: String,
        transactionTotalPrice: Double,
        transactionPayment: Double,
        transactionBalance: Double,
        tableNumber: Int
    ) {
        viewModelScope.launch {
            // Invoke the function that updates the Transactions item in the DB
            repository.confirmAndFinalizeTransactionStream(
                transactionID,
                transactionDateTime,
                transactionMethod,
                transactionTotalPrice,
                transactionPayment,
                transactionBalance,
                tableNumber
            )
            Log.d(
                "ConfirmTransaction",
                "Transaction with ID $transactionID confirmed and finalized successfully."
            )
            // The previous transaction has been finalized. Now, a fresh one will be created and activated for this account
        }
    }

    fun createNewTransactionUnderAccount(newAccountID: Long) {
        viewModelScope.launch {
            try {
                // invoke the function that creates a new Transactions item in the DB
                repository.createNewTransactionUnderAccountStream(newAccountID)
                Log.d("NewTransaction", "Created new Transactions item for accountID $newAccountID. This Transactions item is now currently active for this account.")
            } catch (e: Exception) {
                Log.e("NewTransaction", "Failed to create new Transactions item: $e")
            }
        }
    }

    private fun getAllCurrentOrderItems(transactionID: Int) {
        viewModelScope.launch {
            val currentOrderItemsListWithMenus = fetchOrderItemsWithMenus(transactionID)

            // Update orderItemUi with the list of order items
            orderItemUi = orderItemUi.copy(
                orderItemsList = currentOrderItemsListWithMenus
            )
            Log.d("orderItemUi", "New orderItemsList created with size ${orderItemUi.orderItemsList.size}")
        }
    }

    private suspend fun fetchOrderItemsWithMenus(transactionID: Int): List<Pair<OrderItem, Menu>> {
        val orderItems = repository.getAllOrderItemsByTransactionIDStream(transactionID)
            .filterNotNull()
            .first()
        // The value pairs will be stored in the following mutableList
        val orderItemsListWithMenus = mutableListOf<Pair<OrderItem, Menu>>()
        // Then, the mutableList is populated with pairs of (OrderItem, Menu), iteratively through each orderItem
        orderItems.forEach { orderItem ->
            // Get the OrderItem and its corresponding Menu
            val correspondingMenuItem = repository.getCorrespondingMenuItemStream(menuItemID = orderItem.menuItemID)
            // Add the OrderItem and Menu to the mutableList
            orderItemsListWithMenus.add(Pair(orderItem, correspondingMenuItem))
        }
        return orderItemsListWithMenus
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JomDiningApplication)
                val repository =
                    OfflineRepository(
                        application.database.accountDao(),
                        application.database.menuDao(),
                        application.database.orderItemDao(),
                        application.database.stockDao(),
                        application.database.transactionsDao()
                    )
                FoodOrderingViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
package com.example.jomdining.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class JomDiningViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var menuUi by mutableStateOf(MenuUi())
        private set

    var orderItemUi by mutableStateOf(OrderItemUi())
        private set

    var transactionsUi by mutableStateOf(TransactionsUi())
        private set

    init {
        runBlocking {
            getAllMenuItems()
            // FOR TESTING ONLY
            // addNewOrIncrementOrderItem(1, 8, 1)
        }
    }

    /*
        ALL ITEMS UNDER MenuDao
     */
    fun getAllMenuItems() {
        viewModelScope.launch {
            menuUi = menuUi.copy(
                menuItems = repository.getAllMenuItems()
                    .filterNotNull()
                    .first()
            )
        }
    }

    /*
        ALL ITEMS UNDER OrderItemDao
     */
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
                    Log.d("ANOIOI_OF1_COI", "currentOrderItems content: $currentOrderItems")
                    var isNewOrderItem = true
                    for (orderItem in currentOrderItems) {
                        if (orderItem.menuItemID == menuItemID) {
                            isNewOrderItem = false
                            break
                        }
                    }
                    Log.d("ANOIOI_IsNewOrderItem", "With $menuItemID, value of isNewOrderItem returned as $isNewOrderItem")
                    if (isNewOrderItem) {
                        // invoke the function that adds a new order item
                        repository.addNewOrderItemStream(transactionID, menuItemID)
                        Log.d("ANOIOI_OF1_PASS1", "New OrderItem added to the currently active transaction list.")
                    } else {
                        // invoke the function that increments orderItemQuantity
                        repository.increaseOrderItemQuantityStream(transactionID, menuItemID)
                        Log.d("ANOIOI_OF1_PASS2", "OrderItems exists in list. Existing orderItemQuanttiy increased by 1.")
                    }
                } catch (e: Exception) {
                    Log.e("ANOIOI_OF1_FAIL", "Failed to add new OrderItem to currently active transaction list: $e")
                }
            } else if (operationFlag == 2) {    // operationFlag = 2 -> increase existing orderItemQuantity
                try {
                    // invoke the function that increments orderItemQuantity
                    repository.increaseOrderItemQuantityStream(transactionID, menuItemID)
                    Log.d("ANOIOI_OF2_PASS", "Existing orderItemQuantity increased by 1.")
                } catch (e: Exception) {
                    Log.e("ANOIOI_OF2_FAIL", "Failed to increase existing orderItemQuantity by 1: $e")
                }
            }
            getAllCurrentOrderItems(transactionID)
        }
    }

    fun deleteOrDecrementOrderItem(transactionID: Int, menuItemID: Int, operationFlag: Int) {
        // The operation flag will be used to decide which control flow to use.
        // operationFlag = 1 -> delete order item from the list, operationFlag = 2 -> decrement existing orderItemQuantity
        viewModelScope.launch {
            if (operationFlag == 1) {
                try {
                    // invoke the function that deletes an OrderItem from the DB
                    Log.d("DODOI_OF1_PASS", "OrderItem deleted from the current active transaction list.")
                } catch (e: Exception) {
                    Log.e("DODOI_OF1_FAIL", "Failed to delete OrderItem from currently active transaction list: $e")
                }
            } else if (operationFlag == 2) {
                try {
                    // invoke the function that decrements orderItemQuantity
                    repository.decreaseOrderItemQuantityStream(transactionID, menuItemID)
                    Log.d("DODOI_OF2_PASS", "Existing orderItemQuantity decreased by 1.")
                } catch (e: Exception) {
                    Log.e("DODOI_OF2_FAIL", "Failed to decrease existing orderItemQuantity by 1: $e")
                }
            }
            getAllCurrentOrderItems(transactionID)
        }
    }

    fun getAllCurrentOrderItems(transactionID: Int) {
        viewModelScope.launch {
            // Firstly, a list of orderItems is generated
            val currentOrderItems = repository.getAllOrderItemsByTransactionIDStream(transactionID)
                .filterNotNull()
                .first()
                // Log.d("gACOI_OrderItemList", "Successfully created with total of ${currentOrderItems.size} items.")

            // The value pairs will be stored in the following mutableList
            val currentOrderItemsListWithMenus = mutableListOf<Pair<OrderItem, Menu>>()

            // Then, the mutableList is populated with pairs of (OrderItem, Menu), iteratively through each OrderItem
            currentOrderItems.forEach { orderItem ->
                // Get the OrderItem
                // Log.d("gACOI_Element", "Order item details: $orderItem")
                // Get the corresponding Menu
                val correspondingMenuItem = repository.getCorrespondingMenuItemStream(menuItemID = orderItem.menuItemID)
                // Log.d("gACOI_MenuItem", "Found corresponding menu item: $correspondingMenuItem")
                // Add the OrderItem and Menu to the mutableList
                currentOrderItemsListWithMenus.add(Pair(orderItem, correspondingMenuItem))
            }
            // Log.d("gACOI_FinalList", "New list created with size ${currentOrderItemsListWithMenus.size}")
            // Log.d("gACOI_FinalListDetails", "Details: $currentOrderItemsListWithMenus")

            // Update orderItemUi with the new list of order items
            orderItemUi = orderItemUi.copy(
                orderItemsList = currentOrderItemsListWithMenus
            )
            Log.d("orderItemUi", "New orderItemsList created with size ${orderItemUi.orderItemsList.size}")
        }
    }

    /*
        ALL ITEMS UNDER TransactionsDao
     */
    fun getCurrentActiveTransaction(transactionID: Int) {
        viewModelScope.launch {
            // The fetched current active transaction will be stored in this mutableList
            val currentActiveTransactionList = mutableListOf<Transactions>()

            // Also, the fetched Transaction object will be stored in this val
            val currentActiveTransaction = repository.getCurrentActiveTransactionStream(transactionID)
            Log.d("CAT_fetch", "Successfully fetched current active transaction: $currentActiveTransaction")

            // Update TransactionsUi with the new current active transaction
            currentActiveTransactionList.add(currentActiveTransaction)
            transactionsUi = transactionsUi.copy(
                currentActiveTransaction = currentActiveTransactionList
            )
            Log.d("CAT_toList", "Details of current active transaction moved to List: $currentActiveTransactionList")

            // Then, using the fetched Transaction object, fetched all its order items
            getAllCurrentOrderItems(currentActiveTransaction.transactionID)
            Log.d("CAT_orderItems", "Successfully fetched all order items under transaction with ID ${currentActiveTransaction.transactionID}")
        }
    }

    /*
        EVERYTHING ELSE
     */
    fun updateInputPreferences(input: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveInputString(inputString = input)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JomDiningApplication)
                val repository =
                    OfflineRepository(
//                        application.database.accountDao(),
                        application.database.menuDao(),
//                        application.database.menuItemIngredientDao(),
                        application.database.orderItemDao(),
//                        application.database.stockDao(),
                        application.database.transactionsDao()
                    )
                JomDiningViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
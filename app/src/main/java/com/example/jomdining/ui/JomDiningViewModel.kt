package com.example.jomdining.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.jomdining.databaseentities.Account
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.ui.components.HistoricalTransactionsUi
import com.example.jomdining.ui.components.MenuUi
import com.example.jomdining.ui.components.OrderHistoryOrderItemsUi
import com.example.jomdining.ui.components.OrderHistoryUi
import com.example.jomdining.ui.components.OrderItemUi
import com.example.jomdining.ui.components.OrderTrackingUi
import com.example.jomdining.ui.components.StockUi
import com.example.jomdining.ui.components.TransactionsUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class JomDiningViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var menuUi by mutableStateOf(MenuUi())
        private set

    var orderHistoryUi by mutableStateOf(OrderHistoryUi())
        private set

    var orderHistoryOrderItemsUi by mutableStateOf(OrderHistoryOrderItemsUi())
        private set

    var orderTrackingUi by mutableStateOf(OrderTrackingUi())
        private set

    var orderItemUi by mutableStateOf(OrderItemUi())
        private set

    var transactionsUi by mutableStateOf(TransactionsUi())
        private set

    var historicalTransactionsUi by mutableStateOf(HistoricalTransactionsUi())

    var stockUi by mutableStateOf(StockUi())
        private set

    // All variables used to track currently active login session
    private val _activeLoginAccount = MutableLiveData<Account?>()
    private val _loginAttempted = MutableLiveData(false)
    val activeLoginAccount: LiveData<Account?> get() = _activeLoginAccount
    val loginAttempted: LiveData<Boolean> get() = _loginAttempted

    // All variables used in FoodOrderingModuleScreen
    private val _activeTransaction = MutableLiveData<Transactions?>()
    val activeTransaction: LiveData<Transactions?> get() = _activeTransaction
    private val _activeHistoricalTransaction = MutableLiveData<Transactions?>()
    val activeHistoricalTransaction: LiveData<Transactions?> get() = _activeHistoricalTransaction

    // All variables used in the StockManagementModuleScreen
    var selectedStockItem by mutableStateOf<String?>(null)
    var stockItemID by mutableIntStateOf(0)
    var stockItemName by mutableStateOf("")
    var stockItemQuantity by mutableIntStateOf(0)
    var stockItemImageUri by mutableStateOf<String?>(null)

    // All variables used in the MenuManagementModuleScreen
    var selectedMenuItem by mutableStateOf<String?>(null)
    var menuItemID by mutableIntStateOf(0)
    var menuItemName by mutableStateOf("")
    var menuItemPrice by mutableStateOf("")
    var menuItemType by mutableStateOf("")
    var menuItemImageUri by mutableStateOf<String?> (null)
    var menuItemAvailability by mutableIntStateOf(0)

    // All variables used in the OrderHistoryModuleScreen
    var transactionIsSelected by mutableIntStateOf(0)
    var selectedTransactionID by mutableIntStateOf(0)

    /*
        ALL ITEMS UNDER AccountDao
     */
    fun getAccountByLoginDetails(loginUsername: String, loginPassword: String) {
        viewModelScope.launch {
            try {
                val fetchedAccount = repository.getAccountByLoginDetailsStream(loginUsername, loginPassword)
                _activeLoginAccount.postValue(fetchedAccount)
            } catch (e: Exception) {
                Log.e("getAccByLoginDetails", "Error encountered: $e")
                _activeLoginAccount.postValue(null)
            } finally {
                _loginAttempted.postValue(true)
            }
        }
    }
    fun resetLoginAttempt() {
        _loginAttempted.value = false
    }
    fun logout() {
        _activeLoginAccount.value = null
        _loginAttempted.value = false
    }

    fun registerAndCreateNewAccount(accountUsername: String, accountPassword: String, accountEmail: String) {
        viewModelScope.launch {
            try {
                // invoke the function that creates a new account and pushes it to the DB
                val newAccountID = repository.createNewAccountStream(accountUsername, accountPassword, accountEmail)
                Log.d("AccountRegistration", "New account with username $accountUsername registered successfully [New accountID: $newAccountID]")
                // invoke the function that creates a new Transactions item. a new account will have one active Transactions item at all times
                createNewTransactionUnderAccount(newAccountID)
            } catch (e: Exception) {
                Log.e("AccountRegistration", "Error when registering new account: $e")
            }
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

    fun getAllMenuItemsExceptRetired() {
        viewModelScope.launch {
            menuUi = menuUi.copy(
                menuItems = repository.getAllMenuItemsExceptRetired()
                    .filterNotNull()
                    .first()
            )
        }
    }

    fun addNewMenuItem(menuItemName: String, menuItemPrice: Double, menuItemType: String) {
        viewModelScope.launch {
            try {
                // invoke the function that inserts a new Menu item to the DB
                repository.addNewMenuItemStream(menuItemName, menuItemPrice, menuItemType)
                Log.d("addNewMenuItem", "New menu item added successfully")
            } catch (e: Exception) {
                Log.e("addNewMenuItem", "Error when adding new menu item: $e")
            }
            getAllMenuItems()
        }
    }

    fun updateMenuItemDetails(menuItemID: Int, menuItemName: String, menuItemPrice: Double, menuItemType: String) {
        viewModelScope.launch {
            try {
                // invoke the function that updates the Menu item details in the DB
                repository.updateMenuItemDetailsStream(menuItemID, menuItemName, menuItemPrice, menuItemType)
                Log.d("updateMenuItemDetails", "Menu item updated successfully. New details: (menuItemID: $menuItemID | menuItemName: $menuItemName | menuItemPrice: $menuItemPrice | menuItemType: $menuItemType)")
            } catch (e: Exception) {
                Log.e("updateMenuItemDetails", "Error when updating menu item details: $e")
            }
            getAllMenuItems()
        }
    }

    fun updateMenuAvailability(menuItemID: Int, availabilityToggle: Int) {
        viewModelScope.launch {
            repository.updateMenuItemAvailabilityStream(menuItemID, availabilityToggle)
            getAllMenuItems()
        }
    }

    fun retireMenuItem(menuItemID: Int) {
        viewModelScope.launch {
            try {
                // invoke the function that retires the menu item by updating its availability flag
                repository.retireMenuItemStream(menuItemID)
                Log.d("retireMenuItem", "Menu item retired successfully.")
            } catch (e: Exception) {
                Log.e("retireMenuItem", "Error when retiring menu item: $e")
            }
            getAllMenuItems()
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

    fun getAllCurrentOrderItems(transactionID: Int) {
        viewModelScope.launch {
            val currentOrderItemsListWithMenus = fetchOrderItemsWithMenus(transactionID)

            // Update orderItemUi with the list of order items
            orderItemUi = orderItemUi.copy(
                orderItemsList = currentOrderItemsListWithMenus
            )
            Log.d("orderItemUi", "New orderItemsList created with size ${orderItemUi.orderItemsList.size}")
        }
    }

    fun getAllHistoricalOrderItems(transactionID: Int) {
        viewModelScope.launch {
            val historicalOrderItemsListWithMenus = fetchOrderItemsWithMenus(transactionID)

            // Update orderHistoryOrderItemsUi with the list of order items
            orderHistoryOrderItemsUi = orderHistoryOrderItemsUi.copy(
                orderHistoryOrderItemsList = historicalOrderItemsListWithMenus
            )
            Log.d("orderHistoryOrderItemsUi", "New orderHistoryOrderItemsList created with size ${orderHistoryOrderItemsUi.orderHistoryOrderItemsList.size}")
        }
    }

    fun updateFoodServedFlag(newFlag: Int, connectedTransactionID: Int, menuItemID: Int) {
        viewModelScope.launch {
            // invoke the function that updates the foodServed flag for the orderItem in the DB
            repository.updateFoodServedFlagStream(newFlag, connectedTransactionID, menuItemID)
            Log.d("UFSF", "Flag updated")
            // regenerate the list of transactions
            getAllTransactionsBeingPrepared()
        }
    }

    /*
        ALL ITEMS UNDER TransactionsDao
     */
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

            // Then, using the fetched Transaction object, fetched all its order items
            getAllCurrentOrderItems(currentActiveTransaction.transactionID)
            Log.d(
                "CAT_orderItems",
                "Successfully fetched all order items under transaction with ID ${currentActiveTransaction.transactionID}"
            )
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

    fun getAllHistoricalTransactions(accountID: Int) {
        viewModelScope.launch {
            orderHistoryUi = orderHistoryUi.copy(
                orderHistoryList = repository.getAllHistoricalTransactionsStream(accountID)
                    .filterNotNull()
                    .first()
            )
            Log.d("orderHistoryList", "Total historical transactions: ${orderHistoryUi.orderHistoryList.size}")
        }
    }

    fun getHistoricalTransactionDetailsByID(transactionID: Int) {
        viewModelScope.launch {
            val transaction = repository.getHistoricalTransactionByIDStream(transactionID)
            _activeHistoricalTransaction.value = transaction

            val currentHistoricalTransactionList = mutableListOf<Transactions>()
            val currentHistoricalTransaction = repository.getHistoricalTransactionByIDStream(transactionID)

            currentHistoricalTransactionList.add(currentHistoricalTransaction)
            historicalTransactionsUi = historicalTransactionsUi.copy(
                currentHistoricalTransactionList = currentHistoricalTransactionList
            )

            getAllHistoricalOrderItems(currentHistoricalTransaction.transactionID)
        }
    }
    fun getAllTransactionsBeingPrepared() {
        viewModelScope.launch {
            // Pair(Transactions, List<Pair<OrderItem, Menu>>)
            val completeTrackingListItems = mutableListOf<Pair<Transactions, List<Pair<OrderItem, Menu>>>>()
            // First, fetch all transactions where isActive = 0.
            val collectedTransactions = repository.getAllTransactionsBeingPrepared()
            collectedTransactions.forEach { transaction ->
                // Next, fetch the order items for each Transactions item
                val orderListToThisTransaction = fetchOrderItemsWithMenus(transaction.transactionID)
                // Then, merge it to the large complex datatype
                completeTrackingListItems.add(Pair(transaction, orderListToThisTransaction))
            }
            orderTrackingUi = orderTrackingUi.copy(
                completeTrackingList = completeTrackingListItems
            )
        }
    }

    fun updateTransactionAsCompleted(transactionID: Int) {
        viewModelScope.launch {
            // Invoke the function that updates the isActive flag for the Transactions item in the DB
            repository.updateTransactionAsCompleteStream(transactionID)
            Log.d("UTAC", "Flag update. Transaction now marked as completed")
            // regenerate the list of transactions
            getAllTransactionsBeingPrepared()
        }
    }

    /*
        ALL ITEMS UNDER StockDao
     */
    fun addNewStockItem(stockItemName: String, stockItemQuantity: Int) {
        viewModelScope.launch {
            try {
                // invoke the function that inserts a new Stock item to the DB
                repository.addNewStockItemStream(stockItemName, stockItemQuantity)
                Log.d("addNewStockItem", "New stock item added successfully")
            } catch (e: Exception) {
                Log.e("addNewStockItem", "Error when adding new stock item: $e")
            }
            getAllStockItems()
        }
    }

    fun updateStockItemDetails(stockItemID: Int, newStockItemName: String, newStockItemQuantity: Int) {
        viewModelScope.launch {
            try {
                // invoke the function that update the Stock item details in the DB
                repository.updateStockItemDetailsStream(stockItemID, newStockItemName, newStockItemQuantity)
                Log.d("updateStockItemDetails",
                    "Stock item updated successfully. New details: (stockItemID: $stockItemID | stockItemName: $newStockItemName | stockItemQuantity: $newStockItemQuantity"
                )
            } catch (e: Exception) {
                Log.e("updateStockItemDetails", "Error when update stock item details: $e")
            }
            getAllStockItems()
        }
    }

    fun deleteStockItem(stockItemID: Int) {
        viewModelScope.launch {
            try {
                // invoke the function that deletes the Stock item in the DB
                repository.deleteStockItemStream(stockItemID)
                Log.d("deleteStockItem", "Stock item deleted successfully.")
            } catch (e: Exception) {
                Log.e("deleteStockItem", "Error when deleting stock item: $e")
            }
            getAllStockItems()
        }
    }

    fun getAllStockItems() {
        viewModelScope.launch {
            stockUi = stockUi.copy(
                stockItems = repository.getAllStockItems()
                    .filterNotNull()
                    .first()
            )
            Log.d("stockItems", "Total stock items: ${stockUi.stockItems.size}")
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
                        application.database.accountDao(),
                        application.database.menuDao(),
                        application.database.orderItemDao(),
                        application.database.stockDao(),
                        application.database.transactionsDao()
                    )
                JomDiningViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
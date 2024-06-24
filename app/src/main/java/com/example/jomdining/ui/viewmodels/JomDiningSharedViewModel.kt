package com.example.jomdining.ui.viewmodels

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
import com.example.jomdining.ui.components.OrderHistoryOrderItemsUi
import com.example.jomdining.ui.components.OrderHistoryUi
import com.example.jomdining.ui.components.OrderTrackingUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JomDiningSharedViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var orderHistoryUi by mutableStateOf(OrderHistoryUi())
        private set

    var orderHistoryOrderItemsUi by mutableStateOf(OrderHistoryOrderItemsUi())
        private set

    var orderTrackingUi by mutableStateOf(OrderTrackingUi())
        private set

    private var historicalTransactionsUi by mutableStateOf(HistoricalTransactionsUi())

    // All variables used to track currently active login session
    private val _activeLoginAccount = MutableLiveData<Account?>()
    private val _loginAttempted = MutableLiveData(false)
    val activeLoginAccount: LiveData<Account?> get() = _activeLoginAccount
    val loginAttempted: LiveData<Boolean> get() = _loginAttempted

    // All variables used in the MenuManagementModuleScreen
    var menuItemID by mutableIntStateOf(0)
    var menuItemName by mutableStateOf("")
    var menuItemPrice by mutableStateOf("")
    var menuItemType by mutableStateOf("")
    var menuItemAvailability by mutableIntStateOf(0)

    // All variables used in the OrderHistoryModuleScreen
    var transactionIsSelected by mutableIntStateOf(0)
    var selectedTransactionID by mutableIntStateOf(0)
    private val _activeHistoricalTransaction = MutableLiveData<Transactions?>()
    val activeHistoricalTransaction: LiveData<Transactions?> get() = _activeHistoricalTransaction

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

    /*
        ALL ITEMS UNDER OrderItemDao
     */
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

    private fun getAllHistoricalOrderItems(transactionID: Int) {
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
            // regenerate the list of transactions
            getAllTransactionsBeingPrepared()
        }
    }

    /*
        ALL ITEMS UNDER TransactionsDao
     */
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
            // regenerate the list of transactions
            getAllTransactionsBeingPrepared()
        }
    }

    fun updateTransactionAsCancelled(transactionID: Int) {
        viewModelScope.launch {
            try {
                // Invoke the function that updates the isActive flag for the Transactions item in the DB
                repository.updateTransactionAsCancelledStream(transactionID)
            } catch (e: Exception) {
                Log.e("cancelTransaction", "Error when cancelling transaction: $e")
            }
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
                JomDiningSharedViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
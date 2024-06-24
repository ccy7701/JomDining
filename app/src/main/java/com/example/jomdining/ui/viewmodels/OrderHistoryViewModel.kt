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
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.ui.components.HistoricalTransactionsUi
import com.example.jomdining.ui.components.OrderHistoryOrderItemsUi
import com.example.jomdining.ui.components.OrderHistoryUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var orderHistoryUi by mutableStateOf(OrderHistoryUi())
        private set

    var orderHistoryOrderItemsUi by mutableStateOf(OrderHistoryOrderItemsUi())
        private set

    private var historicalTransactionsUi by mutableStateOf(HistoricalTransactionsUi())
        private set

    // All variables used in the OrderHistoryModuleScreen
    var transactionIsSelected by mutableIntStateOf(0)
    var selectedTransactionID by mutableIntStateOf(0)
    private val _activeHistoricalTransaction = MutableLiveData<Transactions?>()
    val activeHistoricalTransaction: LiveData<Transactions?> get() = _activeHistoricalTransaction

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
                OrderHistoryViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
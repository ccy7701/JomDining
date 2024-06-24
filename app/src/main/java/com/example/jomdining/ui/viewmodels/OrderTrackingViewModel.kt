package com.example.jomdining.ui.viewmodels

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
import com.example.jomdining.ui.components.OrderTrackingUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderTrackingViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var orderTrackingUi by mutableStateOf(OrderTrackingUi())
        private set

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

    fun updateFoodServedFlag(newFlag: Int, connectedTransactionID: Int, menuItemID: Int) {
        viewModelScope.launch {
            // invoke the function that updates the foodServed flag for the orderItem in the DB
            repository.updateFoodServedFlagStream(newFlag, connectedTransactionID, menuItemID)
            // regenerate the list of transactions
            getAllTransactionsBeingPrepared()
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
                OrderTrackingViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
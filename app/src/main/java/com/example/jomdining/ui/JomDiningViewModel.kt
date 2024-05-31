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

    init {
        runBlocking {
            getAllMenuItems()
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

    fun getAllCurrentOrderItems(transactionID: Int) {
        viewModelScope.launch {
            // The value pairs will be stored in the following mutableList
            val currentOrderItemsListWithMenus = mutableListOf<Pair<OrderItem, Menu>>()

            // Firstly, a list of orderItems is generated
            val currentOrderItems = repository.getAllOrderItemsByTransactionIDStream(transactionID)
                .filterNotNull()
                .first()
                // Log.d("COI_List", "Successfully created with total of ${currentOrderItems.size} items.")

            // Then, the mutableList is populated with pairs of (OrderItem, Menu), iteratively through each OrderItem
            currentOrderItems.forEach { orderItem ->
                // Get the OrderItem
                // Log.d("COI_Element", "Order item details: $orderItem")
                // Get the corresponding Menu
                val correspondingMenuItem = repository.getCorrespondingMenuItem(menuItemID = orderItem.menuItemID)
                // Log.d("COI_MenuItem", "Found corresponding menu item: $correspondingMenuItem")
                // Add the OrderItem and Menu to the mutableList
                currentOrderItemsListWithMenus.add(Pair(orderItem, correspondingMenuItem))
            }
            Log.d("COI_FinalList", "New list created with size ${currentOrderItemsListWithMenus.size}")
            Log.d("COI_FinalListDetails", "Details: $currentOrderItemsListWithMenus")

            // Update orderItemUi with the new list of order items
            orderItemUi = orderItemUi.copy(
                orderItemsList = currentOrderItemsListWithMenus
            )
            Log.d("orderItemUi", "New orderItemsList created with size ${orderItemUi.orderItemsList.size}")
        }
    }

//    fun increaseOrDecreaseOrderItemQuantity(
//        transactionID: Int,
//        menuItemID: Int,
//        toIncrease: Boolean
//    ) {
//        viewModelScope.launch {
//            val orderItem = repository.getOrderItemByID(transactionID, menuItemID)
//            orderItem?.let {
//                if (toIncrease) {
//                    repository.increaseOrderItemQuantity(transactionID, menuItemID)
//                } else {
//                    repository.decreaseOrderItemQuantity(transactionID, menuItemID)
//                }
//            }
//            updateOrderItemQuantity(transactionID)
//            // You might need a different tutorial as reference.
//            // populate with somethingSomething.menuItemID for buttons and whatnot in the future
//        }
//    }

//    private fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int) {
//        viewModelScope.launch {
//            repository.increaseOrderItemQuantity(transactionID, menuItemID)
//        }
//    }
//
//    private fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int) {
//        viewModelScope.launch {
//            repository.decreaseOrderItemQuantity(transactionID, menuItemID)
//        }
//    }

//    fun updateOrderItemQuantity(transactionID: Int) {
//        viewModelScope.launch {
//            orderItemUi = orderItemUi.copy(
//                orderItemsList = repository.getAllOrderItemsByTransactionID(transactionID)
//                    .filterNotNull()
//                    .first()
//            )
//        }
//    }

    /*
        ALL ITEMS UNDER TransactionsDao
     */
    fun getCurrentActiveTransaction(transactionID: Int) {
        viewModelScope.launch {
            // The fetched Transaction object will be stored in this val
            val currentActiveTransaction = repository.getCurrentActiveTransactionStream(transactionID)
            Log.d("CAT_val", "Successfully fetched current active transaction: $currentActiveTransaction")
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
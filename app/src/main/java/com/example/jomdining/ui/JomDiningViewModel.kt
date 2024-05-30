package com.example.jomdining.ui

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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JomDiningViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var menuUi by mutableStateOf(MenuUi())
        private set

    var orderItemUi by mutableStateOf(OrderItemUi())
        private set

    init {
        // getAllMenuItems()
    }

    // ...

    // ...

    /*
        ALL ITEMS UNDER MenuDao
     */
    private fun getAllMenuItems() {
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
    fun increaseOrDecreaseOrderItemQuantity(
        transactionID: Int,
        menuItemID: Int,
        toIncrease: Boolean
    ) {
        viewModelScope.launch {
            val orderItem = repository.getOrderItemByID(transactionID, menuItemID)
            orderItem?.let {
                if (toIncrease) {
                    repository.increaseOrderItemQuantity(transactionID, menuItemID)
                } else {
                    repository.decreaseOrderItemQuantity(transactionID, menuItemID)
                }
            }
            updateOrderItemQuantity(transactionID)
            // You might need a different tutorial as reference.
            // populate with somethingSomething.menuItemID for buttons and whatnot in the future
        }
    }

    private fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int) {
        viewModelScope.launch {
            repository.increaseOrderItemQuantity(transactionID, menuItemID)
        }
    }

    private fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int) {
        viewModelScope.launch {
            repository.decreaseOrderItemQuantity(transactionID, menuItemID)
        }
    }

    fun updateOrderItemQuantity(transactionID: Int) {
        viewModelScope.launch {
            orderItemUi = orderItemUi.copy(
                orderItems = repository.getAllOrderItemsByTransactionID(transactionID)
                    .filterNotNull()
                    .first()
            )
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
//                        application.database.transactionsDao()
                    )
                JomDiningViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
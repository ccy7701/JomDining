package com.example.jomdining.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jomdining.JomDiningApplication
import com.example.jomdining.data.JomDiningRepository
import com.example.jomdining.data.OfflineRepository
import com.example.jomdining.data.UserPreferencesRepository
import kotlinx.coroutines.launch

class JomDiningViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // ...

    // ...

    /*
        ALL ITEMS UNDER OrderItemDao
     */
    fun increaseOrDecreaseOrderItemQuantity(
        transactionID: Int,
        menuItemID: Int,
        toIncrease: Boolean
    ) {
        viewModelScope.launch {
            val orderItem = repository.fetchOrderItemByID(transactionID, menuItemID)
            orderItem?.let {
                if (toIncrease) {
                    repository.increaseOrderItemQuantity(transactionID, menuItemID)
                } else {
                    repository.decreaseOrderItemQuantity(transactionID, menuItemID)
                }
            }
            // You need a different tutorial as reference.
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
                        application.database.menuItemIngredientDao(),
                        application.database.orderItemDao(),
                        application.database.stockDao(),
                        application.database.transactionDao()
                    )
                JomDiningViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
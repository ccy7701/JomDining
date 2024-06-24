package com.example.jomdining.ui.viewmodels

import android.util.Log
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

class RegisterViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
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

    private fun createNewTransactionUnderAccount(newAccountID: Long) {
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
                RegisterViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
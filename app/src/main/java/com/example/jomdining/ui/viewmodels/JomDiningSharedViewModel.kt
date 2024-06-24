package com.example.jomdining.ui.viewmodels

import android.util.Log
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
import kotlinx.coroutines.launch

class JomDiningSharedViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // All variables used to track currently active login session
    private val _activeLoginAccount = MutableLiveData<Account?>()
    private val _loginAttempted = MutableLiveData(false)
    val activeLoginAccount: LiveData<Account?> get() = _activeLoginAccount
    val loginAttempted: LiveData<Boolean> get() = _loginAttempted

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
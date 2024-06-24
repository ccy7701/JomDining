package com.example.jomdining.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.jomdining.ui.components.MenuUi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MenuManagementViewModel(
    private val repository: JomDiningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var menuUi by mutableStateOf(MenuUi())
        private set

    // All variables used in the MenuManagementModuleScreen
    var selectedMenuItem by mutableStateOf<String?>(null)
    var menuItemID by mutableIntStateOf(0)
    var menuItemName by mutableStateOf("")
    var menuItemPrice by mutableStateOf("")
    var menuItemType by mutableStateOf("")
    var menuItemImageUri by mutableStateOf<String?> (null)
    var menuItemAvailability by mutableIntStateOf(0)

    fun getAllMenuItems() {
        viewModelScope.launch {
            menuUi = menuUi.copy(
                menuItems = repository.getAllMenuItems()
                    .filterNotNull()
                    .first()
            )
        }
    }

    fun updateMenuAvailability(menuItemID: Int, availabilityToggle: Int) {
        viewModelScope.launch {
            repository.updateMenuItemAvailabilityStream(menuItemID, availabilityToggle)
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
                MenuManagementViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}
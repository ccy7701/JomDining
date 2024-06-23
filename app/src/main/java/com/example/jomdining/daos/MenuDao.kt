package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.jomdining.databaseentities.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Query("""
        INSERT INTO menu (menuItemName, menuItemPrice, menuItemType, menuItemImagePath, menuItemAvailability)
        VALUES (:menuItemName, :menuItemPrice, :menuItemType, "", 1)
    """)
    suspend fun addNewMenuItem(menuItemName: String, menuItemPrice: Double, menuItemType: String)

    @Query("""
        UPDATE menu
        SET menuItemName = :menuItemName, menuItemPrice = :menuItemPrice, menuItemType = :menuItemType
        WHERE menuItemID = :menuItemID
    """)
    suspend fun updateMenuItemDetails(menuItemID: Int, menuItemName: String, menuItemPrice: Double, menuItemType: String)

    @Query("""
        UPDATE menu
        SET menuItemAvailability = :availabilityToggle
        WHERE menuItemID = :menuItemID
    """)
    suspend fun updateMenuAvailability(menuItemID: Int, availabilityToggle: Int)

    // The idea behind using the menuItemAvailability flag is,
    // By default, active menu items have value 1, out of stock items have value 0, retired items have value -1.
    // Therefore it will not disturb historical transaction receipts.
    @Query("""
        UPDATE menu
        SET menuItemAvailability = (-1)
        WHERE menuItemID = :menuItemID
    """)
    suspend fun retireMenuItem(menuItemID: Int)

    @Query("SELECT * FROM menu ORDER BY menuItemType")
    fun getAllMenuItems(): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE menuItemAvailability != (-1) ORDER BY menuItemType")
    fun getAllMenuItemsExceptRetired(): Flow<List<Menu>>
}
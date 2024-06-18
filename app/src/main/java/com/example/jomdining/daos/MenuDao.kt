package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jomdining.databaseentities.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
//    @Delete
//    suspend fun removeMenu(menu: Menu)

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

    @Query("SELECT * FROM menu ORDER BY menuItemType")
    fun getAllMenuItems(): Flow<List<Menu>>
}
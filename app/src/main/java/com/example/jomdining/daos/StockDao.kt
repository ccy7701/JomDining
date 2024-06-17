package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.jomdining.databaseentities.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    // Add a new row to the stock table
    @Query("""
        INSERT INTO stock (stockItemName, stockItemQuantity, stockItemImagePath)
        VALUES (:stockItemName, :stockItemQuantity, '')
    """)
    suspend fun addNewStockItem(stockItemName: String, stockItemQuantity: Int)

    // Edit an existing row in the stock table
    @Query("""
        UPDATE stock
        SET stockItemName = :newStockItemName, stockItemQuantity = :newStockItemQuantity
        WHERE stockItemID = :stockItemID
    """)
    suspend fun updateStockItemDetails(stockItemID: Int, newStockItemName: String, newStockItemQuantity: Int)

    // Remove a row from the stock table
    @Query("""
        DELETE FROM stock
        WHERE stockItemID = :stockItemID
    """)
    suspend fun deleteStockItem(stockItemID: Int)

    // Fetch all rows of stock
    @Query("SELECT * FROM stock ORDER BY stockItemID")
    fun getAllStockItems(): Flow<List<Stock>>
}
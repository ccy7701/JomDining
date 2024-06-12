package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.Stock
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.TransactionsConverter
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionsConverter::class
)
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
    @Delete
    suspend fun removeStock(stock: Stock)

    // Fetch all rows of stock
    @Query("SELECT * FROM stock ORDER BY stockItemID")
    fun getAllStockItems(): Flow<List<Stock>>

//    @Query("""
//        UPDATE stock
//        SET stockItemQuantity = :newStockItemQuantity
//        WHERE stockItemID = :stockItemID
//    """)
//    fun updateStockItemQuantity(newStockItemQuantity: Int, stockItemID: Int)
}
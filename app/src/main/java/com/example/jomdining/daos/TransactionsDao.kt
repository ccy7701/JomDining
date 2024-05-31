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
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.databaseentities.TransactionsConverter

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionsConverter::class
)
interface TransactionsDao {
    // Add a new row to the transaction table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transactions)

    @Delete
    suspend fun removeTransaction(transaction: Transactions)

    // THERE IS MORE TO BE ADDED LATER
    @Query("""
        SELECT * FROM transactions
        WHERE transactionID = :transactionID
        AND isActive = 1
    """)
    suspend fun getCurrentActiveTransaction(transactionID: Int): Transactions
}
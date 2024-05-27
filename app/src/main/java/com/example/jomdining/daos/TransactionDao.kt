package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.Transaction
import com.example.jomdining.databaseentities.TransactionConverter

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionConverter::class
)
interface TransactionDao {
    // Add a new row to the transaction table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Delete
    suspend fun removeTransaction(transaction: Transaction)

    // THERE IS MORE TO BE ADDED LATER
}
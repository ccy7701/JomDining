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
    @Query("""
        INSERT INTO transactions (accountID, transactionDateTime, transactionMethod, transactionTotalPrice, transactionPayment, transactionBalance, tableNumber, isActive) VALUES
        (:newAccountID, "", "", 0.00, 0.00, 0.00, 0, 1)
    """)
    suspend fun createNewTransactionUnderAccount(newAccountID: Long)

    @Delete
    suspend fun removeTransaction(transaction: Transactions)

    @Query("""
        SELECT * FROM transactions
        WHERE accountID = :accountID AND isActive = 1
        LIMIT 1
    """)
    suspend fun getCurrentActiveTransaction(accountID: Int): Transactions

    @Query("""
        UPDATE transactions
        SET transactionTotalPrice = :runningTotal
        WHERE accountID = :accountID and isActive = 1
    """)
    suspend fun updateRunningTotal(runningTotal: Long, accountID: Int)

    // Update the running grand total of the currently active transaction
//    @Query("""
//
//    """)

}
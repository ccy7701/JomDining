package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.jomdining.databaseentities.Transactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {
    // Add a new row to the transaction table
    @Query("""
        INSERT INTO transactions (accountID, transactionDateTime, transactionMethod, transactionTotalPrice, transactionPayment, transactionBalance, tableNumber, isActive) VALUES
        (:newAccountID, "", "", 0.00, 0.00, 0.00, 1, 1)
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
        SELECT * FROM transactions
        WHERE transactionID = :transactionID
    """)
    suspend fun getTransactionByID(transactionID: Int): Transactions

    @Query("""
        SELECT * FROM transactions
        WHERE accountID = :accountID AND isActive = 0
    """)
    fun getAllHistoricalTransactions(accountID: Int): Flow<List<Transactions>>
}

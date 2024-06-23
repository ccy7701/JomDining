package com.example.jomdining.daos

import androidx.room.Dao
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

    @Query("""
        SELECT * FROM transactions
        WHERE accountID = :accountID AND isActive = 1
        LIMIT 1
    """)
    suspend fun getCurrentActiveTransaction(accountID: Int): Transactions

    @Query("""
        UPDATE transactions
        SET 
            transactionDateTime = :transactionDateTime, transactionMethod = :transactionMethod,
            transactionTotalPrice = :transactionTotalPrice, transactionPayment = :transactionPayment,
            transactionBalance = :transactionBalance, tableNumber = :tableNumber,
            isActive = 0
        WHERE transactionID = :transactionID
    """)
    suspend fun confirmAndFinalizeTransaction(
        transactionID: Int,
        transactionDateTime: String,
        transactionMethod: String,
        transactionTotalPrice: Double,
        transactionPayment: Double,
        transactionBalance: Double,
        tableNumber: Int
    )

    @Query("""
        SELECT * FROM transactions
        WHERE transactionID = :transactionID
    """)
    suspend fun getHistoricalTransactionByID(transactionID: Int): Transactions

    @Query("""
        SELECT * FROM transactions
        WHERE accountID = :accountID AND isActive IN (0, -1, -2)
    """)
    fun getAllHistoricalTransactions(accountID: Int): Flow<List<Transactions>>

    @Query("""
        SELECT * FROM transactions
        WHERE isActive = 0
    """)
    suspend fun getAllTransactionsBeingPrepared(): List<Transactions>

    @Query("""
        UPDATE transactions
        SET isActive = (-1)
        WHERE transactionID = :transactionID
    """)
    suspend fun updateTransactionAsComplete(transactionID: Int)

    @Query("""
        UPDATE transactions
        SET isActive = (-2)
        WHERE transactionID = :transactionID
    """)
    suspend fun updateTransactionAsCancelled(transactionID: Int)
}

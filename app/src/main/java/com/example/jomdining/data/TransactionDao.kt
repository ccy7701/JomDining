package com.example.jomdining.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface TransactionDao {
    // Add a new row to the transaction table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Delete
    suspend fun removeTransaction(transaction: Transaction)

    // THERE IS MORE TO BE ADDED LATER
}
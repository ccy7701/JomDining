package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "transactions",
    foreignKeys = [
        // FOREIGN KEY ("accountID") REFERENCES "account"("accountID")
        ForeignKey(
            entity = Account::class,
            parentColumns = ["accountID"],
            childColumns = ["accountID"]
        )
    ]
)
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    val transactionID: Int,
    val accountID: Int,
    val transactionDateTime: String,
    val transactionMethod: String,
    val transactionTotalPrice: Float,
    val transactionPayment: Float,
    val transactionBalance: Float,
    val tableNumber: Int,
    val isActive: Int
)

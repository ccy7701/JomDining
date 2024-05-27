package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity(
    tableName = "transaction",
    foreignKeys = [
        // FOREIGN KEY ("accountID") REFERENCES "account"("accountID")
        ForeignKey(
            entity = Account::class,
            parentColumns = ["accountID"],
            childColumns = ["accountID"]
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionID: Int,
    val accountID: Int,
    val transactionDateTime: String,
    val transactionMethod: String,
    val transactionTotalPrice: Float,
    val transactionPayment: Float,
    val transactionBalance: Float,
    val tableNumber: Int
)

@ProvidedTypeConverter
class TransactionConverter {
    @TypeConverter
    fun stringToTransaction(transactionJson: String?): Transaction? {
        return transactionJson?.let {
            Json.decodeFromString(it)
        }
    }

    @TypeConverter
    fun transactionToString(transaction: Transaction?): String {
        return Json.encodeToString(transaction)
    }
}
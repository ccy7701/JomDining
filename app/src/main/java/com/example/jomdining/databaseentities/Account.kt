package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionsConverter::class
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountID: Int,
    val accountPassword: String,
    val employeeName: String,
    val employeePosition: String,
    val employeePhoneNumber: String,
    val employeeEmail: String
)

@ProvidedTypeConverter
class AccountConverter {
    // Convert String to Account
    @TypeConverter
    fun stringToAccount(accountJson: String?): Account? {
        return accountJson?.let {
            Json.decodeFromString(it)
        }
    }

    // Convert Account to String
    @TypeConverter
    fun accountToString(account: Account?): String {
        return Json.encodeToString(account)
    }
}
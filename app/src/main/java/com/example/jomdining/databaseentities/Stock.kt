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
    TransactionConverter::class
)
data class Stock (
    @PrimaryKey(autoGenerate = true)
    val stockItemID: Int,
    val stockItemName: String,
    val stockItemQuantity: Int
)

@ProvidedTypeConverter
class StockConverter {
    @TypeConverter
    fun stringToStock(stockJson: String?): Stock? {
        return stockJson?.let {
            Json.decodeFromString(it)
        }
    }

    @TypeConverter
    fun stockToString(stock: Stock?): String {
        return Json.encodeToString(stock)
    }
}
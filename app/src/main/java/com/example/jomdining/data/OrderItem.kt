package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity(
    tableName = "order_item",
    primaryKeys = ["menuItemID", "transactionID"],
    foreignKeys = [
        // FOREIGN KEY ("menuItemID") REFERENCES "menu"("menuItemID")
        ForeignKey(
            entity = Menu::class,
            parentColumns = ["menuItemID"],
            childColumns = ["menuItemID"]
        ),
        // FOREIGN KEY ("transactionID") REFERENCES "transaction"("transactionID")
        ForeignKey(
            entity = Transaction::class,
            parentColumns = ["transactionID"],
            childColumns = ["transactionID"]
        )
    ]
)
data class OrderItem(
    val menuItemID: Int,
    val transactionID: Int,
    val orderItemQuantity: Int,
    val foodServed: Int
)

@ProvidedTypeConverter
class OrderItemConverter {
    @TypeConverter
    fun stringToOrderItem(orderItemJson: String?): OrderItem? {
        return orderItemJson?.let {
            Json.decodeFromString(it)
        }
    }

    @TypeConverter
    fun orderItemToString(orderItem: OrderItem?): String {
        return Json.encodeToString(orderItem)
    }
}
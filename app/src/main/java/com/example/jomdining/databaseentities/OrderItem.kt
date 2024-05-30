package com.example.jomdining.databaseentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
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
            entity = Transactions::class,
            parentColumns = ["transactionID"],
            childColumns = ["transactionID"]
        )
    ]
)
@TypeConverters(
//    AccountConverter::class,
//    MenuConverter::class,
//    MenuItemIngredientConverter::class,
//    OrderItemConverter::class,
//    StockConverter::class,
//    TransactionsConverter::class
)
data class OrderItem(
    @ColumnInfo(name = "menuItemID")
    val menuItemID: Int,
    @ColumnInfo(name = "transactionID")
    val transactionID: Int,
    val orderItemQuantity: Int,
    val foodServed: Int
)

@ProvidedTypeConverter
class OrderItemConverter {
    // Convert String to OrderItem
    @TypeConverter
    fun stringToOrderItem(orderItemJson: String?): OrderItem? {
        return orderItemJson?.let {
            Json.decodeFromString(it)
        }
    }

    // Convert OrderItem to String
    @TypeConverter
    fun orderItemToString(orderItem: OrderItem?): String {
        return Json.encodeToString(orderItem)
    }
}
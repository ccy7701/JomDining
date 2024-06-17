package com.example.jomdining.databaseentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
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
data class OrderItem(
    @ColumnInfo(name = "menuItemID")
    val menuItemID: Int,
    @ColumnInfo(name = "transactionID")
    val transactionID: Int,
    val orderItemQuantity: Int,
    val foodServed: Int
)

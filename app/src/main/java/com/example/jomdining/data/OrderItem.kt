package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.ForeignKey

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
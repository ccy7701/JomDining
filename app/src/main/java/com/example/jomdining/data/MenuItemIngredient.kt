package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "menu_item_ingredient",
    primaryKeys = ["menuItemID", "stockItemID"],
    foreignKeys = [
        // FOREIGN KEY ("menuItemID") REFERENCES "menu"("menuItemID")
        ForeignKey(
            entity = Menu::class,
            parentColumns = ["menuItemID"],
            childColumns = ["menuItemID"]
        ),
        // FOREIGN KEY ("stockItemID") REFERENCES "stock"("stockItemID")
        ForeignKey(
            entity = Stock::class,
            parentColumns = ["stockItemID"],
            childColumns = ["stockItemID"]
        )
    ]
)
data class MenuItemIngredient(
    val menuItemID: Int,
    val stockItemID: Int,
    val ingredientQuantity: Int
)
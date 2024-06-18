package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "menu"
)
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val menuItemID: Int,
    val menuItemName: String,
    val menuItemPrice: Double,
    val menuItemType: String,
    val menuItemImagePath: String,
    val menuItemAvailability: Int
)

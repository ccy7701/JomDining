package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

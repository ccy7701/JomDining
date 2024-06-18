package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "stock"
)
data class Stock (
    @PrimaryKey(autoGenerate = true)
    val stockItemID: Int,
    val stockItemName: String,
    val stockItemQuantity: Int,
    val stockItemImagePath: String
)

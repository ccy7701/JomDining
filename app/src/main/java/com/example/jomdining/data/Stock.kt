package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stock (
    @PrimaryKey(autoGenerate = true)
    val stockItemID: Int,
    val stockItemName: String,
    val stockItemQuantity: Int
)
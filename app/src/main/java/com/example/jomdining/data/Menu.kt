package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val menuItemID: Int,
    val menuItemName: String,
    val menuItemPrice: Float,
    val menuItemType: String
)
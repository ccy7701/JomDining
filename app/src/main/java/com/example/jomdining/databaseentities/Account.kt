package com.example.jomdining.databaseentities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "account"
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountID: Int,
    val accountUsername: String,
    val accountPassword: String,
    val accountEmail: String,
    val accountPhoneNumber: String
)

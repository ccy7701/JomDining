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

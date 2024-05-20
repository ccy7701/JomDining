package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountID: Int,
    val accountPassword: String,
    val employeeName: String,
    val employeePosition: String,
    val employeePhoneNumber: String,
    val employeeEmail: String
)
package com.example.jomdining.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AccountDao {
    // Assumption: Login is made with email and password
    // BUT, userAuth has to happen somewhere else...
    @Query("SELECT * FROM account WHERE employeeEmail = :input")
    fun getAccountByEmail(input: String): Account?
}
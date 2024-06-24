package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.jomdining.databaseentities.Account

@Dao
interface AccountDao {
    @Query("""
        SELECT * FROM account
        WHERE accountUsername = :loginUsername
        AND accountPassword = :loginPassword
    """)
    suspend fun getAccountByLoginDetails(loginUsername: String, loginPassword: String): Account

    @Query("""
        INSERT INTO account (accountUsername, accountPassword, accountEmail, accountPhoneNumber)
        VALUES (:accountUsername, :accountPassword, :accountEmail, "")
    """)
    suspend fun createNewAccount(accountUsername: String, accountPassword: String, accountEmail: String): Long
}
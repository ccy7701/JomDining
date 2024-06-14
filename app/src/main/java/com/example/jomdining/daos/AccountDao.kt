package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.Account
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.TransactionsConverter

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionsConverter::class
)
interface AccountDao {
//    // Assumption: Login is made with email and password
//    // BUT, userAuth has to happen somewhere else...
//    @Query("SELECT * FROM account WHERE employeeEmail = :input")
//    fun getAccountByEmail(input: String): Account?

    @Query("""
        SELECT * FROM account
        WHERE accountUsername = :loginUsername
        AND accountPassword = :loginPassword
    """)
    suspend fun getAccountByLoginDetails(loginUsername: String, loginPassword: String): Account
}
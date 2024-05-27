package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredient
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.TransactionConverter
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionConverter::class
)
interface MenuItemIngredientDao {
    // Add a new row to the menu_item_ingredient table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMenuItemIngredient(menuItemIngredient: MenuItemIngredient)

    @Delete
    suspend fun removeMenuItemIngredient(menuItemIngredient: MenuItemIngredient)

    // THIS NEEDS TO BE MODIFIED IN THE FUTURE. IT CURRENTLY PULLS DATA FOR ALL ORDERS!
    @Query("SELECT * FROM menu_item_ingredient ORDER BY menuItemID")
    fun getAllMenuItemIngredients(): Flow<List<MenuItemIngredient>>

    // THIS ALSO DOES NOT LOOK RIGHT!
    @Query("SELECT * FROM menu_item_ingredient WHERE menuItemID = :menuItemID")
    fun getIngredientsForSingleMenuItem(menuItemID: Int): Flow<List<MenuItemIngredient>>
}
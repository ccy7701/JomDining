package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.MenuConverter
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
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMenu(menu: Menu)

    @Delete
    suspend fun removeMenu(menu: Menu)

    @Query("SELECT * FROM menu ORDER BY menuItemID")
    fun getAllMenuItems(): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE menuItemType = :menuItemTypeInput")
    fun getAllMenuItemsByType(menuItemTypeInput: String): Menu?

    // More may be added later
}
package com.example.jomdining.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jomdining.daos.AccountDao
import com.example.jomdining.daos.MenuDao
import com.example.jomdining.daos.MenuItemIngredientDao
import com.example.jomdining.daos.OrderItemDao
import com.example.jomdining.daos.StockDao
import com.example.jomdining.daos.TransactionsDao
import com.example.jomdining.databaseentities.Account
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredient
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.Stock
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.databaseentities.TransactionsConverter
import kotlinx.coroutines.InternalCoroutinesApi

@Database(
    entities = [
        Account::class,
        Menu::class,
        MenuItemIngredient::class,
        OrderItem::class,
        Stock::class,
        Transactions::class
    ],
    version = 1,
    exportSchema = false
)
abstract class JomDiningDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun menuDao(): MenuDao
    abstract fun menuItemIngredientDao(): MenuItemIngredientDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun stockDao(): StockDao
    abstract fun transactionDao(): TransactionsDao

    companion object {
        @Volatile
        private var Instance: JomDiningDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): JomDiningDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    JomDiningDatabase::class.java,
                    "jom_dining_database.db"
                )
                    .createFromAsset(
                        "database/jom_dining_database.db"
                    )
                    .addTypeConverter(AccountConverter())
                    .addTypeConverter(MenuConverter())
                    .addTypeConverter(MenuItemIngredientConverter())
                    .addTypeConverter(OrderItemConverter())
                    .addTypeConverter(StockConverter())
                    .addTypeConverter(TransactionsConverter())
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}
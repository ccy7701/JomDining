package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.AccountConverter
import com.example.jomdining.databaseentities.MenuConverter
import com.example.jomdining.databaseentities.MenuItemIngredientConverter
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.OrderItemConverter
import com.example.jomdining.databaseentities.StockConverter
import com.example.jomdining.databaseentities.TransactionsConverter
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(
    AccountConverter::class,
    MenuConverter::class,
    MenuItemIngredientConverter::class,
    OrderItemConverter::class,
    StockConverter::class,
    TransactionsConverter::class
)
interface OrderItemDao {
    // Add a new row to the menu_item_ingredient table
    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun addOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun removeOrderItem(orderItem: OrderItem)

    @Query("""
        SELECT order_item.transactionID, order_item.menuItemID, order_item.orderItemQuantity, order_item.foodServed
        FROM order_item
        INNER JOIN menu ON order_item.menuItemID = menu.menuItemID
        INNER JOIN transactions ON order_item.transactionID = transactions.transactionID
        WHERE order_item.transactionID = :transactionID
        AND order_item.menuItemID = :menuItemID
    """)
    fun getOrderItemByID(transactionID: Int, menuItemID: Int): OrderItem

    @Query("""
        SELECT * FROM order_item
        INNER JOIN menu AS menu ON order_item.menuItemID = menu.menuItemID
        WHERE transactionID = :transactionID
    """)
    fun getAllOrderItemsByTransactionID(transactionID: Int): Flow<List<OrderItem>>

    @Query("""
        UPDATE order_item
        SET orderItemQuantity = orderItemQuantity + 1
        WHERE transactionID = :transactionID
        AND menuItemID = :menuItemID
    """)
    suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int)

    @Query("""
        UPDATE order_item
        SET orderItemQuantity = orderItemQuantity - 1
        WHERE transactionID = :transactionID
        AND menuItemID = :menuItemID
    """)
    suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
}
package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(
//    AccountConverter::class,
//    MenuConverter::class,
//    MenuItemIngredientConverter::class,
//    OrderItemConverter::class,
//    StockConverter::class,
//    TransactionsConverter::class
)
interface OrderItemDao {
    // Add a new row to the menu_item_ingredient table
    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun addOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun removeOrderItem(orderItem: OrderItem)

    // Get all order items using the current active transactionID
    @Query("""
        SELECT * FROM order_item
        INNER JOIN menu AS menu ON order_item.menuItemID = menu.menuItemID
        WHERE transactionID = :transactionID
    """)
    fun getAllOrderItemsByTransactionID(transactionID: Int): Flow<List<OrderItem>>

    // Get a specific order item based on its attached menuItemID
    @Query("""
        SELECT * FROM order_item
        WHERE order_item.menuItemID = :menuItemID
    """)
    suspend fun getOrderItemByID(menuItemID: Int): OrderItem

    // Get the menu item that corresponds to the order item
    @Query("""
        SELECT menu.* 
        FROM menu
        INNER JOIN order_item ON order_item.menuItemID = menu.menuItemID
        WHERE order_item.menuItemID = :menuItemID
    """)
    suspend fun getCorrespondingMenuItem(menuItemID: Int): Menu

//    @Query("""
//        UPDATE order_item
//        SET orderItemQuantity = orderItemQuantity + 1
//        WHERE transactionID = :transactionID
//        AND menuItemID = :menuItemID
//    """)
//    suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
//
//    @Query("""
//        UPDATE order_item
//        SET orderItemQuantity = orderItemQuantity - 1
//        WHERE transactionID = :transactionID
//        AND menuItemID = :menuItemID
//    """)
//    suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
}
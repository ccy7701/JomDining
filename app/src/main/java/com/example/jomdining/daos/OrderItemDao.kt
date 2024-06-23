package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    // Add a new row to the menu_item_ingredient table
    @Query("""
        INSERT INTO order_item (menuItemID, transactionID, orderItemQuantity, foodServed)
        VALUES (:menuItemID, :transactionID, 1, 0)
    """)
    suspend fun addNewOrderItem(transactionID: Int, menuItemID: Int)

    @Query("""
        DELETE FROM order_item
        WHERE order_item.transactionID = :transactionID
        AND order_item.menuItemID = :menuItemID
    """)
    suspend fun deleteOrderItem(transactionID: Int, menuItemID: Int)

    // Get all order items using the current active transactionID
    @Query("""
        SELECT * FROM order_item
        INNER JOIN menu AS menu ON order_item.menuItemID = menu.menuItemID
        WHERE transactionID = :transactionID
        ORDER BY menu.menuItemType ASC
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

    // Increase the order item quantity (this will be invoked when the [+] button is pressed)
    @Query("""
        UPDATE order_item
        SET orderItemQuantity = orderItemQuantity + 1
        WHERE transactionID = :transactionID
        AND menuItemID = :menuItemID
    """)
    suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int)

    // Decrease the order item quantity (this will be invoked when the [-] button is pressed)
    // The query also includes a check for whether or not orderItemQuantity is already 1 at invocation,
    // This will stop the value from going below 1.
    @Query("""
        UPDATE order_item
        SET orderItemQuantity = CASE
            WHEN orderItemQuantity > 1 THEN
                orderItemQuantity - 1
            ELSE
                orderItemQuantity
        END
        WHERE transactionID = :transactionID
        AND menuItemID = :menuItemID
    """)
    suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int)

    @Query("""
        UPDATE order_item
        SET foodServed = :newFlag
        WHERE transactionID = :connectedTransactionID
        AND menuItemID = :menuItemID
    """)
    suspend fun updateFoodServedFlag(newFlag: Int, connectedTransactionID: Int, menuItemID: Int)
}
package com.example.jomdining.data

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import kotlinx.coroutines.flow.Flow

interface JomDiningRepository {
    /*
        ITEMS UNDER MenuDao
     */
    fun getAllMenuItems(): Flow<List<Menu>>

    /*
        ITEMS UNDER OrderItemDao
     */
    suspend fun addNewOrderItemStream(transactionID: Int, menuItemID: Int)

    suspend fun getOrderItemByID(transactionID: Int, menuItemID: Int): OrderItem

    suspend fun getCorrespondingMenuItemStream(menuItemID: Int): Menu

    fun getAllOrderItemsByTransactionIDStream(transactionID: Int): Flow<List<OrderItem>>

    suspend fun increaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int)

    suspend fun decreaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int)

    /*
        ITEMS UNDER TransactionsDao
     */
    suspend fun getCurrentActiveTransactionStream(transactionID: Int): Transactions
}
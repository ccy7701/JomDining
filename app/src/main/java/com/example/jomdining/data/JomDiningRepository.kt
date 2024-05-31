package com.example.jomdining.data

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import kotlinx.coroutines.flow.Flow

interface JomDiningRepository {
    /*
        ITEMS UNDER MenuDao
     */
    fun getAllMenuItems(): Flow<List<Menu>>

    /*
        ITEMS UNDER OrderItemDao
     */
    suspend fun getOrderItemByID(transactionID: Int, menuItemID: Int): OrderItem

    suspend fun getCorrespondingMenuItem(menuItemID: Int): Menu

    fun getAllOrderItemsByTransactionIDStream(transactionID: Int): Flow<List<OrderItem>>

//    suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
//
//    suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
}
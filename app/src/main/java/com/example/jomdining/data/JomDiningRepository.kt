package com.example.jomdining.data

import com.example.jomdining.databaseentities.OrderItem

interface JomDiningRepository {
    /*
        ITEMS UNDER OrderItemDao
     */
    fun fetchOrderItemByID(transactionID: Int, menuItemID: Int): OrderItem

    suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int)

    suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int)
}
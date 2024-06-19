package com.example.jomdining.ui.components

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions

data class OrderHistoryUi(
    val orderHistoryList: List<Transactions> = listOf()
)

data class OrderHistoryOrderItemsUi(
    val orderHistoryOrderItemsList: List<Pair<OrderItem, Menu>> = listOf()
)
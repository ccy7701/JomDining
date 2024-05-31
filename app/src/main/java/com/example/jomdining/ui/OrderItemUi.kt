package com.example.jomdining.ui

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem

data class OrderItemUi(
    val orderItemsList: List<Pair<OrderItem, Menu>> = listOf()
)
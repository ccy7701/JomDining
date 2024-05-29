package com.example.jomdining.ui

import com.example.jomdining.databaseentities.OrderItem

data class OrderItemUi(
    val orderItems: List<OrderItem> = listOf()
)
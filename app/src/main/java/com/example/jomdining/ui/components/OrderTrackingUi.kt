package com.example.jomdining.ui.components

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions

data class OrderTrackingUi(
    val completeTrackingList: List<Pair<Transactions, List<Pair<OrderItem, Menu>>>> = listOf()
)

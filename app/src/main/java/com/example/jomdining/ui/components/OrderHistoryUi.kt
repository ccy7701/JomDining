package com.example.jomdining.ui.components

import com.example.jomdining.databaseentities.Transactions

data class OrderHistoryUi(
    val orderHistoryList: List<Transactions> = listOf()
)
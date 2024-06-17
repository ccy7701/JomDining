package com.example.jomdining.ui.components

import com.example.jomdining.databaseentities.Transactions

data class TransactionsUi(
    val currentActiveTransactionList: List<Transactions> = listOf()
)
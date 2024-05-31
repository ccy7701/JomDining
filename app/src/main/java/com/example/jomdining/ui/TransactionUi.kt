package com.example.jomdining.ui

import com.example.jomdining.databaseentities.Transactions

data class TransactionsUi(
    val currentActiveTransaction: List<Transactions> = listOf()
)
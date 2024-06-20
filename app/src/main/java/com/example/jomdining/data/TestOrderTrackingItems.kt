package com.example.jomdining.data

import androidx.datastore.preferences.core.Preferences
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions

// Pair(Transactions, List<Pair<OrderItems, Menu>>)
object TestOrderTrackingItems {
    val completeTrackingList = listOf(
        Pair(
            // Transactions object
            Transactions(
                transactionID = 1,
                accountID = 1,
                transactionDateTime = "TEST_DATETIME_1",
                transactionMethod = "Cash",
                transactionTotalPrice = (45.55).toFloat(),
                transactionPayment = (50.00).toFloat(),
                transactionBalance = (4.45).toFloat(),
                tableNumber = 5,
                isActive = 0
            ),
            // Then List of Pair of OrderItem and Menu object
            TestOrderItemsWithMenus.orderItemsWithMenus
        ),
        Pair(
            // Transactions object
            Transactions(
                transactionID = 5,
                accountID = 2,
                transactionDateTime = "TEST_DATETIME_2",
                transactionMethod = "Card",
                transactionTotalPrice = (99.55).toFloat(),
                transactionPayment = (96.00).toFloat(),
                transactionBalance = (0.45).toFloat(),
                tableNumber = 8,
                isActive = 0
            ),
            // Then List of Pair of OrderItem and Menu object
            TestOrderItemsWithMenus.orderItemsWithMenus
        ),
        Pair(
            // Transactions object
            Transactions(
                transactionID = 8,
                accountID = 7,
                transactionDateTime = "TEST_DATETIME_2",
                transactionMethod = "Card",
                transactionTotalPrice = (149.67).toFloat(),
                transactionPayment = (150.00).toFloat(),
                transactionBalance = (0.33).toFloat(),
                tableNumber = 8,
                isActive = 0
            ),
            // Then List of Pair of OrderItem and Menu object
            TestOrderItemsWithMenus.orderItemsWithMenus
        )
    )
}
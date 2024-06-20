package com.example.jomdining.data

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem

object TestOrderItemsWithMenus {
    val orderItemsWithMenus = listOf(
        Pair(
            OrderItem(1, 1, 5, 0),
            Menu(1, "TEST-Chicken Chop", 25.0, "main_course", "images/menu/chickenChop.png", 1),
        ),
        Pair(
            OrderItem(2, 1, 8, 0),
            Menu(2, "TEST-Sirloin Steak", 50.75, "main_course", "images/menu/sirloinSteak.png", 1)
        ),
        Pair(
            OrderItem(3, 1, 3, 0),
            Menu(3, "TEST-Fish and Chips", 26.0, "main_course", "images/menu/fish&Chips.png", 1)
        )
    )
}
package com.example.jomdining.data

import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem

object TempMenuItems {
    val menuItems = listOf(
        Menu(1, "TEST-Chicken Chop", 25.0, "main_course", "images/chickenChop.png"),
        Menu(2, "TEST-Sirloin Steak", 50.75, "main_course", "images/sirloinSteak.png"),
        Menu(3, "TEST-Fish and Chips", 26.0, "main_course", "images/fish&Chips.png"),
        Menu(4, "TEST-Chicken Nuggets", 11.35, "side_dish", "images/chickenNuggets.png"),
        Menu(5, "TEST-French Fries", 7.7, "side_dish", "images/frenchFries.png"),
        Menu(6, "TEST-Onion Rings", 9.95, "side_dish", "images/onionRings.png"),
        Menu(7, "TEST-A&W(L)", 6.13, "beverage", "images/a&w.png"),
        Menu(8, "TEST-Coca-Cola(L)", 6.13, "beverage", "images/cocaCola.png"),
        Menu(9, "TEST-Sprite(L)", 6.13, "beverage", "images/sprite.png"),
        Menu(10, "TEST-Sprite(L)-2", 6.13, "beverage", "images/sprite.png"),
        Menu(11, "TEST-Sprite(L)-3", 6.13, "beverage", "images/sprite.png"),
        Menu(12, "TEST-Sprite(L)-4", 6.13, "beverage", "images/sprite.png"),
        Menu(13, "TEST-Sprite(L)-5", 6.13, "beverage", "images/sprite.png")
    )
}

object TempOrderItemsList {
    val orderItemsList = listOf(
        OrderItem(1, 1, 5, 0),
        OrderItem(2, 1, 7, 0),
        OrderItem(8, 1, 11, 0)
    )
}
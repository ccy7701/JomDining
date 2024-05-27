package com.example.jomdining.data

import com.example.jomdining.databaseentities.Menu

object TempMenuItems {
    val menuItems = listOf(
        Menu(1, "TEST-Chicken Chop", 25.0, "MainCourse"),
        Menu(2, "TEST-Sirloin Steak", 50.75, "MainCourse"),
        Menu(3, "TEST-Fish and Chips", 26.0, "MainCourse"),
        Menu(4, "TEST-Chicken Nuggets", 11.35, "SideDish"),
        Menu(5, "TEST-French Fries", 7.7, "SideDish"),
        Menu(6, "TEST-Onion Rings", 9.95, "SideDish"),
        Menu(7, "TEST-A&W(L)", 6.13, "Beverage"),
        Menu(8, "TEST-Coca-Cola(L)", 6.13, "Beverage"),
        Menu(9, "TEST-Sprite(L)", 6.13, "Beverage")
    )
}
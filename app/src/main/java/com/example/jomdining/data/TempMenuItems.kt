package com.example.jomdining.data

import androidx.compose.ui.res.stringResource
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Menu

object TempMenuItems {
    val menuItems = listOf(
        Menu(1, "TEST-Chicken Chop", 25.0, "main_course", R.drawable.chicken_chop),
        Menu(2, "TEST-Sirloin Steak", 50.75, "MainCourse", R.drawable.sirloin_steak),
        Menu(3, "TEST-Fish and Chips", 26.0, "MainCourse", R.drawable.fish_and_chips),
        Menu(4, "TEST-Chicken Nuggets", 11.35, "SideDish", R.drawable.chicken_nuggets),
        Menu(5, "TEST-French Fries", 7.7, "SideDish", R.drawable.french_fries),
        Menu(6, "TEST-Onion Rings", 9.95, "SideDish", R.drawable.onion_rings),
        Menu(7, "TEST-A&W(L)", 6.13, "Beverage", R.drawable.a_and_w),
        Menu(8, "TEST-Coca-Cola(L)", 6.13, "Beverage", R.drawable.coca_cola),
        Menu(9, "TEST-Sprite(L)", 6.13, "Beverage", R.drawable.sprite),
        Menu(10, "TEST-Sprite(L)-2", 6.13, "Beverage", R.drawable.sprite),
        Menu(11, "TEST-Sprite(L)-3", 6.13, "Beverage", R.drawable.sprite),
        Menu(12, "TEST-Sprite(L)-4", 6.13, "Beverage", R.drawable.sprite),
        Menu(13, "TEST-Sprite(L)-5", 6.13, "Beverage", R.drawable.sprite)
    )
}
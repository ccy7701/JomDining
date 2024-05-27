package com.example.jomdining.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TempTopic (
    @StringRes val name: Int,
    val availableCourse: Int,
    @DrawableRes val imageRes: Int
)
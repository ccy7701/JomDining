package com.example.jomdining.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val menuItemID: Int,
    val menuItemName: String,
    val menuItemPrice: Float,
    val menuItemType: String
)

@ProvidedTypeConverter
class MenuConverter {
    @TypeConverter
    fun stringToMenu(menuJson: String?): Menu? {
        return menuJson?.let {
            Json.decodeFromString(it)
        }
    }

    @TypeConverter
    fun menuToString(menu: Menu?): String {
        return Json.encodeToString(menu)
    }
}
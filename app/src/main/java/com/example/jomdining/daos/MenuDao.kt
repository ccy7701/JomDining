package com.example.jomdining.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jomdining.databaseentities.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun addMenu(menu: Menu)

//    @Delete
//    suspend fun removeMenu(menu: Menu)

    @Query("SELECT * FROM menu ORDER BY menuItemType")
    fun getAllMenuItems(): Flow<List<Menu>>
}
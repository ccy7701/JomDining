package com.example.jomdining

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.jomdining.data.JomDiningDatabase
import com.example.jomdining.data.UserPreferencesRepository

private const val INPUT_PREFERENCE_NAME = "input_string"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = INPUT_PREFERENCE_NAME)

class JomDiningApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    val database: JomDiningDatabase by lazy {
        JomDiningDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}

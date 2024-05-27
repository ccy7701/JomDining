package com.example.jomdining.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val inputString: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "ERROR: Cannot read preferences. ", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[INPUT_STRING] ?: ""
        }

    private companion object {
        val INPUT_STRING = stringPreferencesKey("input_string")
        const val TAG = "UserPreferencesRepository"
    }

    suspend fun saveInputString(inputString: String) {
        dataStore.edit { preferences ->
            preferences[INPUT_STRING] = inputString
        }
    }
}
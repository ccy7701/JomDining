package com.example.jomdining.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    fun saveUserToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun getUserToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
package com.example.smartcampus.util

import android.content.Context
import androidx.core.content.edit

object SessionManager {

    private const val PREF_NAME = "campus_session"
    private const val KEY_LOGIN = "is_logged_in"

    fun saveLogin(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_LOGIN, isLoggedIn) }
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOGIN, false)
    }
}

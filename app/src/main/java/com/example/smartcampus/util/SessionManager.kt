package com.example.smartcampus.util

import android.content.Context
import androidx.core.content.edit

object SessionManager {

    private const val PREF_NAME = "campus_session"
    private const val KEY_LOGIN = "is_logged_in"
    private const val KEY_ROLE = "user_role"
    private const val KEY_USERNAME = "username"
    private const val KEY_NOTIFICATIONS = "notifications_enabled"

    fun saveLogin(context: Context, isLoggedIn: Boolean, role: String?, username: String?) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(KEY_LOGIN, isLoggedIn)
            putString(KEY_ROLE, role)
            putString(KEY_USERNAME, username)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOGIN, false)
    }

    fun getUserRole(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ROLE, null)
    }

    fun getUsername(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USERNAME, null)
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_NOTIFICATIONS, enabled) }
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_NOTIFICATIONS, true)
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}

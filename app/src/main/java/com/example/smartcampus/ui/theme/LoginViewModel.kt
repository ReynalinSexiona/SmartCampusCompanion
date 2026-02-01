package com.example.smartcampus.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.smartcampus.util.SessionManager

class LoginViewModel : ViewModel() {

    fun validateLogin(username: String, password: String, context: Context): Boolean {
        return if (username == "student" && password == "1234") {
            SessionManager.saveLogin(context, true)
            true
        } else {
            false
        }
    }
}

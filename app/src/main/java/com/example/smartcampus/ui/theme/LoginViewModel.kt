package com.example.smartcampus.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.data.local.UserEntity
import com.example.smartcampus.data.local.UserRole
import com.example.smartcampus.util.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    fun validateLogin(username: String, password: String, context: Context, onResult: (Boolean) -> Unit) {
        val userDao = SmartCampusDatabase.getDatabase(context).userDao()
        
        viewModelScope.launch {
            // Pre-populate users if none exist
            if (userDao.getUserCount() == 0) {
                userDao.insertUser(UserEntity(username = "admin", password = "123", role = UserRole.ADMIN))
                userDao.insertUser(UserEntity(username = "student", password = "123", role = UserRole.STUDENT))
            }

            val user = userDao.getUser(username, password)
            if (user != null) {
                // Save login with role AND username
                SessionManager.saveLogin(context, true, user.role.name, user.username)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }
}

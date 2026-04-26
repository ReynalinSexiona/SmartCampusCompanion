package com.example.smartcampus.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.data.local.UserEntity
import com.example.smartcampus.data.local.UserRole
import com.example.smartcampus.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val firestore = FirebaseFirestore.getInstance()

    fun validateLogin(email: String, password: String, context: Context, onResult: (Boolean) -> Unit) {
        val userDao = SmartCampusDatabase.getDatabase(context).userDao()
        
        viewModelScope.launch {
            _isLoading.value = true
            // Pre-populate users if none exist
            if (userDao.getUserCount() == 0) {
                // Set sexionareynalin@gmail.com as ADMIN
                userDao.insertUser(UserEntity(email = "sexionareynalin@gmail.com", password = "123456", role = UserRole.ADMIN))
                userDao.insertUser(UserEntity(email = "student@gmail.com", password = "1234567", role = UserRole.STUDENT))
                userDao.insertUser(UserEntity(email = "aljayrosario@gmail.com", password = "12345678", role = UserRole.STUDENT))
            }

            val user = userDao.getUser(email, password)
            
            if (user != null) {
                // Save to Firestore when someone signs in successfully
                saveUserToFirestore(user)
                
                // Save login with role AND email (using email as identifier)
                SessionManager.saveLogin(context, true, user.role.name, user.email)
                _isLoading.value = false
                onResult(true)
            } else {
                _isLoading.value = false
                onResult(false)
            }
        }
    }

    private suspend fun saveUserToFirestore(user: UserEntity) {
        try {
            val userData = mapOf(
                "email" to user.email,
                "role" to user.role.name,
                "lastLogin" to System.currentTimeMillis()
            )
            // Gamitin ang email bilang Document ID para hindi duplicate
            firestore.collection("users").document(user.email).set(userData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

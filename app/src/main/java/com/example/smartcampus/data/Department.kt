package com.example.smartcampus.data

    data class Department(
val name: String,
val contact: String,
val email: String? = null,
val phone: String? = null,
val officeHours: String? = null,
val faculty: List<String>? = emptyList(),
val courses: List<String>? = emptyList()
)


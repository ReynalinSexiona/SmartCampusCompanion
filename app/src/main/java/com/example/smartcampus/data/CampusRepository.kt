package com.example.smartcampus.data

object CampusRepository {
    fun getDepartments(): List<Department> {
        return listOf(
            Department("Computer Science", "cs@university.edu"),
            Department("Engineering", "eng@university.edu"),
            Department("Business", "bus@university.edu")
        )
    }
}

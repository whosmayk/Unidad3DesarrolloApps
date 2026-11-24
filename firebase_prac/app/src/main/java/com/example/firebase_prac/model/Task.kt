package com.example.firebase_prac.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val priority: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val flag: Boolean = false,
    val userId: String = ""
)

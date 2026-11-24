package com.example.firebase_prac.model.service

import com.example.firebase_prac.model.Task
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val tasks: Flow<List<Task>>
    suspend fun getTask(taskId: String): Task?
    suspend fun save(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
    suspend fun deleteAllForUser(userId: String)
}

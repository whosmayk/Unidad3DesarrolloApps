package com.example.firebase_prac.model.service.impl

import com.example.firebase_prac.model.Task
import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class StorageServiceImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: AccountService = AccountServiceImpl()
) : StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tasks: Flow<List<Task>>
        get() = auth.currentUser.flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                firestore.collection(TASK_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, user.uid)
                    .dataObjects<Task>()
            }
        }

    override suspend fun getTask(taskId: String): Task? {
        return firestore.collection(TASK_COLLECTION).document(taskId).get().await().toObject<Task>()
    }

    override suspend fun save(task: Task) {
        val taskWithUser = task.copy(userId = auth.currentUserId)
        firestore.collection(TASK_COLLECTION).add(taskWithUser).await()
    }

    override suspend fun update(task: Task) {
        firestore.collection(TASK_COLLECTION).document(task.id).set(task).await()
    }

    override suspend fun delete(taskId: String) {
        firestore.collection(TASK_COLLECTION).document(taskId).delete().await()
    }

    override suspend fun deleteAllForUser(userId: String) {
        val matchingTasks = firestore.collection(TASK_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .await()
        for (document in matchingTasks.documents) {
            document.reference.delete().await()
        }
    }

    companion object {
        private const val TASK_COLLECTION = "tasks"
        private const val USER_ID_FIELD = "userId"
    }
}

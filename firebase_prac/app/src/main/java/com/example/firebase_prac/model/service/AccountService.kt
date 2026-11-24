package com.example.firebase_prac.model.service

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<FirebaseUser?>

    suspend fun authenticateAnonymous()
    suspend fun linkAccount(email: String, pass: String)
    suspend fun authenticate(email: String, pass: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAccount(email: String, pass: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}

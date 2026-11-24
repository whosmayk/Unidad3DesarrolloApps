package com.example.firebase_prac.model.service.impl

import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.LogService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val logService: LogService = LogServiceImpl()
) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                trySend(firebaseAuth.currentUser)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticateAnonymous() {
        auth.signInAnonymously().await()
    }

    override suspend fun linkAccount(email: String, pass: String) = trace(LINK_ACCOUNT_TRACE) {
        val credential = EmailAuthProvider.getCredential(email, pass)
        auth.currentUser?.linkWithCredential(credential)?.await()
        Unit
    }

    override suspend fun authenticate(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAccount(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() = trace(DELETE_ACCOUNT_TRACE) {
        auth.currentUser?.delete()?.await()
        Unit
    }

    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccountTrace"
        private const val DELETE_ACCOUNT_TRACE = "deleteAccountTrace"
    }
}

package com.paruyr.scopictask.data.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.paruyr.scopictask.data.Result
import kotlinx.coroutines.tasks.await

/**
 * Class that handles authentication sign out and sign in credentials and retrieves user information.
 */

interface AuthenticationDataSource {
    suspend fun signIn(email: String, password: String): Result<AuthResult>
    suspend fun signUp(email: String, password: String): Result<AuthResult>
    fun signOut()
}

class AuthenticationDataSourceImpl(private var auth: FirebaseAuth) : AuthenticationDataSource {

    override suspend fun signIn(email: String, password: String): Result<AuthResult> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(authResult)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<AuthResult> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(authResult)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}
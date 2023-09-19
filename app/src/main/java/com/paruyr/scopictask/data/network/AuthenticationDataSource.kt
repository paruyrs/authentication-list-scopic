package com.paruyr.scopictask.data.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.paruyr.scopictask.data.Result
import kotlinx.coroutines.tasks.await

/**
 * Class that handles authentication sign out and sign in credentials and retrieves user information.
 */
class AuthenticationDataSource(private var auth: FirebaseAuth) {

    suspend fun signIn(email: String, password: String): Result<AuthResult> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(authResult)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<AuthResult> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(authResult)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
package com.paruyr.scopictask.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.paruyr.scopictask.data.Result
import com.paruyr.scopictask.data.network.AuthenticationDataSource

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class SignInRepository(private val dataSource: AuthenticationDataSource) {
    suspend fun signIn(email: String, password: String): Result<String> {
        return when (val signInAuthResult = dataSource.signIn(email, password)) {
            is Result.Success -> {
                // Sign in success.
                Log.d(TAG, "signInWithEmailAndPassword:success")
                Result.Success(signInAuthResult.data.user!!.email!!)
            }

            is Result.Error -> {
                // Sign in fails.
                Log.w(TAG, "signInWithEmailAndPassword:failure", signInAuthResult.exception)
                Result.Error(signInAuthResult.exception)
            }
        }
    }
}
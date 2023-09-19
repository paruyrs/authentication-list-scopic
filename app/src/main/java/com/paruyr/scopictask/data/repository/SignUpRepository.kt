package com.paruyr.scopictask.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.paruyr.scopictask.data.Result
import com.paruyr.scopictask.data.network.AuthenticationDataSource


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 * in our case this class could be redundant however in real world sign in and sign up repositories may be different
 */

class SignUpRepository(private val dataSource: AuthenticationDataSource) {
    suspend fun signUp(email: String, password: String): Result<String> {
        return when (val signUpAuthResult = dataSource.signUp(email, password)) {
            is Result.Success -> {
                // Sign up success.
                Log.d(TAG, "createUserWithEmail:success")
                Result.Success(signUpAuthResult.data.user!!.email!!)
            }

            is Result.Error -> {
                // Sign up fails.
                Log.w(TAG, "createUserWithEmail:failure", signUpAuthResult.exception)
                Result.Error(signUpAuthResult.exception)
            }
        }
    }
}
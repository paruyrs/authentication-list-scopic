package com.paruyr.scopictask.utils

import android.util.Patterns

interface Validation {
    fun isEmailValid(username: String): Boolean
    fun isPasswordValid(password: String): Boolean
}

class ValidationImpl : Validation {
    // A placeholder username validation check
    override fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    override fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
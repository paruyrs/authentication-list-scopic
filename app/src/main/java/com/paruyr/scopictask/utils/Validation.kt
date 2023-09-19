package com.paruyr.scopictask.utils

import android.util.Patterns

class Validation {
    // A placeholder username validation check
    fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
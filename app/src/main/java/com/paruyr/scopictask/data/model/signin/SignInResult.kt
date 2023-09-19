package com.paruyr.scopictask.data.model.signin

/**
 * Authentication result : success (user details) or error message.
 */
data class SignInResult(
    val success: SignedInUserView? = null,
    val error: Int? = null
)
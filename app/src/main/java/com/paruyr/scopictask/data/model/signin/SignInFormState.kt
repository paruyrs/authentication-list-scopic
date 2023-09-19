package com.paruyr.scopictask.data.model.signin

/**
 * Data validation state of the login form.
 */
data class SignInFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val editedField: SignInField,
    val isDataValid: Boolean = false
)

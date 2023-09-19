package com.paruyr.scopictask.data.model.signup

data class SignUpFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val editedField: SignUpField,
    val isDataValid: Boolean = false
)

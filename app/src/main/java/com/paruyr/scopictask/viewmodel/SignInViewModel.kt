package com.paruyr.scopictask.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.paruyr.scopictask.R
import com.paruyr.scopictask.data.Result
import com.paruyr.scopictask.data.model.signin.SignInField
import com.paruyr.scopictask.data.model.signin.SignInFormState
import com.paruyr.scopictask.data.repository.ConfigRepository
import com.paruyr.scopictask.data.repository.SignInRepository
import com.paruyr.scopictask.utils.Validation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class SignInViewModel(
    private val signInRepository: SignInRepository,
    private val validation: Validation,
    private val configRepository: ConfigRepository
) : BaseViewModel() {

    private val _navigation = MutableSharedFlow<Navigation>()
    val navigation: SharedFlow<Navigation> = _navigation
    private val _showMessage = MutableSharedFlow<String>()
    val showMessage: SharedFlow<String> = _showMessage
    private val _signInForm = MutableSharedFlow<SignInFormState>()
    val signInFormState: SharedFlow<SignInFormState> = _signInForm
    private val _invalidData = MutableSharedFlow<Unit>()
    val invalidData: SharedFlow<Unit> = _invalidData

    fun signInClick(
        email: String,
        password: String,
    ) = commonViewModelScope.launch {
        when (val result = signInRepository.signIn(email, password)) {
            is Result.Success -> {
                configRepository.setLoggedIn(result.data)
                if (configRepository.shouldShowWelcome()) {
                    _navigation.emit(Navigation.Welcome)
                } else {
                    _navigation.emit(Navigation.Home)
                }
            }

            is Result.Error -> {
                _showMessage.emit("Failed to sign in, please try again: $result")
                Log.e(
                    "SignInViewModel",
                    "signInClick: Failed to sign in, please try again:"
                )
                // show error
            }
        }
    }

    fun signUpClick(email: String) = commonViewModelScope.launch {
        _navigation.emit(Navigation.SignUp(email))
    }

    fun signInDataChanged(username: String, password: String, field: SignInField) =
        viewModelScope.launch {
            val isUsernameValid = validation.isEmailValid(username)
            val isPasswordValid = validation.isPasswordValid(password)

            val usernameError = if (!isUsernameValid) R.string.invalid_email else null
            val passwordError = if (!isPasswordValid) R.string.invalid_password else null

            val isDataValid = isUsernameValid && isPasswordValid

            _signInForm.emit(SignInFormState(usernameError, passwordError, field, isDataValid))
        }

    fun passwordDataChanged(newPassword: String) = commonViewModelScope.launch {
        if (!validation.isPasswordValid(newPassword))
            _invalidData.emit(Unit)
    }

    fun emailDataChanged(newEmail: String) = commonViewModelScope.launch {
        if (!validation.isEmailValid(newEmail))
            _invalidData.emit(Unit)
    }

    sealed class Navigation {
        data object Home : Navigation()
        data class SignUp(val email: String) : Navigation()
        data object Welcome : Navigation()
    }
}
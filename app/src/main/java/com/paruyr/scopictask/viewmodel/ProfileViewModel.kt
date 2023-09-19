package com.paruyr.scopictask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paruyr.scopictask.data.repository.ConfigRepository
import com.paruyr.scopictask.data.repository.SignOutRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val signOutRepository: SignOutRepository,
    private val configRepository: ConfigRepository
) : ViewModel() {

    private val _navigation = MutableSharedFlow<Navigation>()
    val navigation: SharedFlow<Navigation> = _navigation

    private val _welcomeUser = MutableStateFlow<String?>(null)
    val welcomeUser: StateFlow<String?> = _welcomeUser

    fun setup() = viewModelScope.launch {
        _welcomeUser.emit(configRepository.getLoggedInUserEmail())
    }

    fun navigateBackToList() = viewModelScope.launch {
        _navigation.emit(Navigation.List)
    }

    fun logoutClicked() = viewModelScope.launch {
        signOutRepository.signOut()
        configRepository.setLoggedOut()
        _navigation.emit(Navigation.Landing)

    }

    sealed class Navigation {
        data object List : Navigation()
        data object Landing : Navigation()
    }
}
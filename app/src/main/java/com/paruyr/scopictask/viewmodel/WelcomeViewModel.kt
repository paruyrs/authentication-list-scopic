package com.paruyr.scopictask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paruyr.scopictask.data.repository.ConfigRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(private val configRepository: ConfigRepository) : ViewModel() {

    private val _navigation = MutableSharedFlow<Navigation>()
    val navigation: SharedFlow<Navigation> = _navigation

    fun setup() = viewModelScope.launch {
        if (!configRepository.isLoggedIn())
            _navigation.emit(Navigation.Landing)
    }

    fun initialized() = viewModelScope.launch {
        configRepository.markWelcomeShown()
    }

    fun listButtonClicked() = viewModelScope.launch {
        _navigation.emit(Navigation.Home)
    }

    sealed class Navigation {
        data object Home : Navigation()
        data object Landing : Navigation()
    }
}
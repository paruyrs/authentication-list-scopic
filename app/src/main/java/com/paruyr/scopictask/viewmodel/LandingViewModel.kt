package com.paruyr.scopictask.viewmodel

import com.paruyr.scopictask.data.model.landing.LandingScreenNavigation
import com.paruyr.scopictask.data.repository.ConfigRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LandingViewModel(
    private val configRepository: ConfigRepository
) : BaseViewModel() {

    private val _navigation = MutableSharedFlow<LandingScreenNavigation>()
    val navigation: SharedFlow<LandingScreenNavigation> = _navigation

    fun setup() = commonViewModelScope.launch {
        when {
            configRepository.isLoggedIn().not() -> {
                _navigation.emit(LandingScreenNavigation.Authentication)
            }

            configRepository.shouldShowWelcome() -> {
                _navigation.emit(LandingScreenNavigation.Welcome)
            }

            else -> {
                _navigation.emit(LandingScreenNavigation.Home)
            }
        }
    }
}
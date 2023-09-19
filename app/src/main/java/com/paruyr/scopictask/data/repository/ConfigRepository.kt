package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.config.ConfigStorage

class ConfigRepository(private val configStorage: ConfigStorage) {

    suspend fun shouldShowWelcome(): Boolean {
        return !configStorage.isWelcomeShown()
    }

    suspend fun setLoggedIn(email: String) {
        configStorage.setLoggedIn(true)
        configStorage.setLoggedInUserEmail(email)
    }

    suspend fun setLoggedOut() {
        configStorage.setLoggedIn(false)
        configStorage.setLoggedInUserEmail("")
    }

    suspend fun getLoggedInUserEmail(): String {
        return configStorage.getLoggedInUserEmail()
    }


    suspend fun markWelcomeShown() {
        configStorage.markWelcomeShown()
    }

    suspend fun isLoggedIn(): Boolean {
        return configStorage.isLoggedIn()
    }
}
package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.config.ConfigStorage

interface ConfigRepository {
    suspend fun shouldShowWelcome(): Boolean
    suspend fun setLoggedIn(email: String)
    suspend fun setLoggedOut()
    suspend fun getLoggedInUserEmail(): String
    suspend fun markWelcomeShown()
    suspend fun isLoggedIn(): Boolean
}

class ConfigRepositoryImpl(private val configStorage: ConfigStorage) : ConfigRepository {

    override suspend fun shouldShowWelcome(): Boolean {
        return !configStorage.isWelcomeShown()
    }

    override suspend fun setLoggedIn(email: String) {
        configStorage.setLoggedIn(true)
        configStorage.setLoggedInUserEmail(email)
    }

    override suspend fun setLoggedOut() {
        configStorage.setLoggedIn(false)
        configStorage.setLoggedInUserEmail("")
    }

    override suspend fun getLoggedInUserEmail(): String {
        return configStorage.getLoggedInUserEmail()
    }

    override suspend fun markWelcomeShown() {
        configStorage.markWelcomeShown()
    }

    override suspend fun isLoggedIn(): Boolean {
        return configStorage.isLoggedIn()
    }
}
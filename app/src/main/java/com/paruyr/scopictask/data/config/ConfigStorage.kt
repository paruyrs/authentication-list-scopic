package com.paruyr.scopictask.data.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull

interface ConfigStorage {
    suspend fun setLoggedIn(loggedIn: Boolean)
    suspend fun isLoggedIn(): Boolean
    suspend fun markWelcomeShown()
    suspend fun isWelcomeShown(): Boolean
    suspend fun setLoggedInUserEmail(userEmail: String)
    suspend fun getLoggedInUserEmail(): String

}

class ConfigStorageImpl(
    private val dataStore: DataStore<Preferences>
) : ConfigStorage {

    companion object {
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("logged_in")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val WELCOME_SHOWN_KEY = booleanPreferencesKey("welcome_shown")
    }

    private suspend fun <T> getValueByKey(key: Preferences.Key<T>): T? {
        return dataStore.data.firstOrNull()?.get(key)
    }

    private suspend fun <T> setValueByKey(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        setValueByKey(IS_LOGGED_IN_KEY, loggedIn)
    }

    override suspend fun isLoggedIn(): Boolean {
        return getValueByKey(IS_LOGGED_IN_KEY) ?: false
    }

    override suspend fun markWelcomeShown() {
        setValueByKey(WELCOME_SHOWN_KEY, true)
    }

    override suspend fun isWelcomeShown(): Boolean {
        return getValueByKey(WELCOME_SHOWN_KEY) ?: false
    }

    override suspend fun setLoggedInUserEmail(userEmail: String) {
        setValueByKey(USER_EMAIL_KEY, userEmail)
    }

    override suspend fun getLoggedInUserEmail(): String {
        return getValueByKey(USER_EMAIL_KEY) ?: ""
    }
}
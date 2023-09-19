package com.paruyr.scopictask.data.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull

class ConfigStorage(
    private val dataStore: DataStore<Preferences>
) {

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

    suspend fun setLoggedIn(loggedIn: Boolean) {
        setValueByKey(IS_LOGGED_IN_KEY, loggedIn)
    }

    suspend fun isLoggedIn(): Boolean {
        return getValueByKey(IS_LOGGED_IN_KEY) ?: false
    }

    suspend fun markWelcomeShown() {
        setValueByKey(WELCOME_SHOWN_KEY, true)
    }

    suspend fun isWelcomeShown(): Boolean {
        return getValueByKey(WELCOME_SHOWN_KEY) ?: false
    }

    suspend fun setLoggedInUserEmail(userEmail: String) {
        setValueByKey(USER_EMAIL_KEY, userEmail)
    }

    suspend fun getLoggedInUserEmail(): String {
        return getValueByKey(USER_EMAIL_KEY) ?: ""
    }
}
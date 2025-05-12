package com.app.recipeapp.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreUserPrefs(
    private val dataStore: DataStore<Preferences>
): UserPreferences {
    private val nameKey = stringPreferencesKey("name")
    private val loggedKey = booleanPreferencesKey("logged")
    private val logoAccountKey = stringPreferencesKey("logo")

    /*
    Functions to manage LOGGED state
     */
    override suspend fun logIn() {
        dataStore.edit { preferences ->
            preferences[loggedKey] = true
        }
    }

    override suspend fun logOut(){
        dataStore.edit { preferences ->
            preferences[loggedKey] = false
        }
    }

    // Function para la lectura de LOGGED
    override fun loggedStatus(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[loggedKey] ?: false
    }

    /*
    Functions to manage the user's name
     */
    override suspend fun setUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[nameKey] = name
        }
    }

    override suspend fun getUserName(): String? {
        val preferences = dataStore.data.first()
        return preferences[nameKey]
    }
    /*
    Functions to manage the account's logo
     */
    override suspend fun setLogoAccount(filepath: String) {
        dataStore.edit { preferences ->
            preferences[logoAccountKey] = filepath
        }
    }
    override suspend fun getLogoAccount(): String? {
        val preferences = dataStore.data.first()
        return  preferences[logoAccountKey]
    }
}
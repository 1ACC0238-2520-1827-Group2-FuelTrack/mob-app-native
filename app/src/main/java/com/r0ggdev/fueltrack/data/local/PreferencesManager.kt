package com.r0ggdev.fueltrack.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fuel_track_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }
    
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }
    
    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userRole: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ROLE_KEY]
    }

    // Synchronous method for use during DI initialization to avoid runBlocking issues
    fun getTokenSync(): String? {
        return try {
            runBlocking { token.first() }
        } catch (e: Exception) {
            // Fallback to null if DataStore fails during initialization
            null
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
        }
    }
    
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Debug method to check stored values
    suspend fun debugStoredValues() {
        val preferences = context.dataStore.data.first()
        println("DEBUG: PreferencesManager - Stored values:")
        println("DEBUG: PreferencesManager - TOKEN_KEY: '${preferences[TOKEN_KEY]}'")
        println("DEBUG: PreferencesManager - USER_ID_KEY: '${preferences[USER_ID_KEY]}'")
        println("DEBUG: PreferencesManager - USER_ROLE_KEY: '${preferences[USER_ROLE_KEY]}'")
    }
    
}


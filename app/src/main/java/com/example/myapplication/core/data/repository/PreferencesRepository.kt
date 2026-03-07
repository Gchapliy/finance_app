package com.example.myapplication.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getAccountId(): Flow<Long?> {
        return dataStore.data.map { prefs ->
            prefs[PreferencesKeys.ACCOUNT_ID]
        }
    }

    suspend fun saveAccountId(accountId: Long) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.ACCOUNT_ID] = accountId
        }
    }
}
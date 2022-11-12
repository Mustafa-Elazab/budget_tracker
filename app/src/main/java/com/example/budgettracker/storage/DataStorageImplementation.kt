package com.example.budgettracker.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.budgettracker.storage.DataStorageImplementation.PreferenceKeys.SELECTED_THEME
import com.example.budgettracker.utils.UIModeDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_Storage")

@Singleton
class DataStorageImplementation @Inject constructor(@ApplicationContext context: Context) :
    DataStorage {

    private val dataStore = context.dataStore

    private object PreferenceKeys {
        val SELECTED_THEME = booleanPreferencesKey("selected_theme")
        val IS_USER_FIRST_TIME = booleanPreferencesKey("user_first_time")
    }

    override val selectedTheme: Flow<Boolean>
        get() =dataStore.data.map { preferences ->
            val uiMode = preferences[SELECTED_THEME] ?: false
            uiMode
        }
    override suspend fun setSelectedTheme(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[SELECTED_THEME] = isNightMode
        }
    }


    override fun isUserFirstTime(): Flow<Boolean> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferenceKeys.IS_USER_FIRST_TIME] ?: true
    }

    override suspend fun setUserFirstTime(isFirstTime: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.IS_USER_FIRST_TIME] = isFirstTime
        }
    }

    override suspend fun putString(key: String, value: String) {

        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = dataStore.data.first()
            return preferences[preferencesKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getInt(key: String): Int? {
        return try {
            val preferencesKey = intPreferencesKey(key)
            val preferences = dataStore.data.first()
            return preferences[preferencesKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferencesKey = booleanPreferencesKey(key)
            val preferences = dataStore.data.first()
            return preferences[preferencesKey]

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
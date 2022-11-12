package com.example.budgettracker.storage

import kotlinx.coroutines.flow.Flow

interface DataStorage {


    val selectedTheme: Flow<Boolean>
    suspend fun setSelectedTheme(isNightMode: Boolean)

    fun isUserFirstTime(): Flow<Boolean>
    suspend fun setUserFirstTime(isFirstTime: Boolean)

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getString(key: String): String?
    suspend fun getInt(key: String): Int?
    suspend fun getBoolean(key: String): Boolean?

}
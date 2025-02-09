package com.app.recipeapp.data.local.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    suspend fun logIn()
    suspend fun logOut()
    fun loggedStatus(): Flow<Boolean>
    suspend fun setUserName(name: String)
    suspend fun getUserName(): String?
}
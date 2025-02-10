package com.app.recipeapp.presentation.login

interface LoginRepository {
    suspend fun login(email:String, password: String): Boolean
}
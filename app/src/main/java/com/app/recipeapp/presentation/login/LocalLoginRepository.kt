package com.app.recipeapp.presentation.login

import kotlinx.coroutines.delay

class LocalLoginRepository: LoginRepository {
    override suspend fun login(email: String, password: String): Boolean{
        delay(2000L)
        return email == "info@mobile.dev" && password == "mobile123"
    }
}
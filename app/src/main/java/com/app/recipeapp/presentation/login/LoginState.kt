package com.app.recipeapp.presentation.login

data class LoginState(
    var email: String = "",
    var password: String = "",
    var showError: Boolean = false,
    var isLoading: Boolean = false,
    var loginSuccess: Boolean = false,
)
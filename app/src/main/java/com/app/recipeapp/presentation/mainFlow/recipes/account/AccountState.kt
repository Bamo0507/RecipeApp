package com.app.recipeapp.presentation.mainFlow.recipes.account

data class AccountState(
    val userName: String = "User",
    val filepath: String = "",
    val isLoading: Boolean = false
)

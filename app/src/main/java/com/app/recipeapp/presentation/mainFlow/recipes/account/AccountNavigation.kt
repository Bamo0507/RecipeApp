package com.app.recipeapp.presentation.mainFlow.recipes.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.serialization.Serializable

@Serializable
data object AccountDestination

fun NavGraphBuilder.accountscreen(
    userPreferences: UserPreferences,
    onLogOutClick: () -> Unit,
    onBackClick: () -> Unit
){
    composable<AccountDestination>{
        AccountRoute(
            onLogOutClick = onLogOutClick,
            userPreferences = userPreferences,
            onBackClick = onBackClick
        )
    }
}
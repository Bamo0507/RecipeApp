package com.app.recipeapp.presentation.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.serialization.Serializable

//Screen Identifier
@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onLoginClick: () -> Unit,
    userPreferences: UserPreferences
){
    composable<LoginDestination>{
        loginRoute(
            onLoginClick = onLoginClick,
            userPreferences = userPreferences
        )
    }
}

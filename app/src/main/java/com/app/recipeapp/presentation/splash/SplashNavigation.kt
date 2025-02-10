package com.app.recipeapp.presentation.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.recipeapp.data.local.preferences.DataStoreUserPrefs
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.serialization.Serializable

@Serializable
data object SplashDestination

fun NavGraphBuilder.splashScreen(
    navController: NavController,
    userPrefs: UserPreferences
){
    composable<SplashDestination>{
        SplashRoute(
            navController = navController,
            userPreferences = userPrefs
        )
    }
}
package com.app.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.presentation.login.LoginDestination
import com.app.recipeapp.presentation.login.loginScreen
import com.app.recipeapp.presentation.mainFlow.recipes.RecipesNavGraph
import com.app.recipeapp.presentation.mainFlow.recipes.recipeList.RecipeListDestination
import com.app.recipeapp.presentation.mainFlow.recipes.recipesGraph
import com.app.recipeapp.presentation.splash.SplashDestination
import com.app.recipeapp.presentation.splash.splashScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    userPreferences: UserPreferences,
){
    NavHost(
        navController = navController,
        startDestination = SplashDestination
    ){
        splashScreen(
            navController = navController,
            userPrefs= userPreferences
        )

        loginScreen(
            onLoginClick = {
                navController.navigate(RecipeListDestination) {
                    //clean backstack
                    popUpTo(LoginDestination) {
                        inclusive = true
                    }
                }
            },
            userPreferences
        )

        recipesGraph(
            onLogOutClick = {
                navController.navigate(LoginDestination){
                    popUpTo(0)
                }
            },
            navController = navController,
            userPreferences = userPreferences
        )

    }
}
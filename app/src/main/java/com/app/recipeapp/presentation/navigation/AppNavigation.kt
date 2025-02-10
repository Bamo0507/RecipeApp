package com.app.recipeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.presentation.login.LoginDestination
import com.app.recipeapp.presentation.login.loginScreen
import com.app.recipeapp.presentation.mainFlow.recipes.recipeList.RecipeListDestination
import com.app.recipeapp.presentation.mainFlow.recipes.recipesGraph

@Composable
fun AppNavigation(
    navController: NavHostController,
    userPreferences: UserPreferences
){
    NavHost(
        navController = navController,
        startDestination = LoginDestination // Swith 2 splash once i have it
    ){
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
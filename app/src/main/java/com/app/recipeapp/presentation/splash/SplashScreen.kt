package com.app.recipeapp.presentation.splash

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.presentation.login.LoginDestination
import com.app.recipeapp.presentation.mainFlow.recipes.RecipesNavGraph
import com.app.recipeapp.presentation.reusableScreens.RecipeLoadingScreen

@Composable
fun SplashRoute (navController: NavController,
                 userPreferences: UserPreferences
){
    val viewModel: SplashViewModel = viewModel(
        factory = SplashViewModel.Companion.Factory(
            userPreferences = userPreferences
        )
    )

    val islogged by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.verifyingLog()
    }

    SplashScreen(
        navController = navController,
        logged = islogged,
        state = state
    )
}

@Composable
fun SplashScreen(
    navController: NavController,
    logged: Boolean?,
    state: SplashState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(logged, state.isLoading) {
        if (!state.isLoading && logged != null) {
            if (logged) {
                navController.navigate(RecipesNavGraph) {
                    popUpTo(SplashDestination) { inclusive = true }
                }
            } else {
                navController.navigate(LoginDestination) {
                    popUpTo(SplashDestination) { inclusive = true }
                }
            }
        }
    }

    if (state.isLoading) {
        RecipeLoadingScreen()
    }
}
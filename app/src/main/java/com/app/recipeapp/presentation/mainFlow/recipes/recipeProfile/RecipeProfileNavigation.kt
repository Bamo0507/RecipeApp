package com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class RecipeProfileDestination(
    val recipeId: Int
)

fun NavController.navigateToProfileScreen(
    destination: RecipeProfileDestination,
    navOptions: NavOptions? = null
) {
    this.navigate(
        destination,
        navOptions
    )
}

fun NavGraphBuilder.profileScreen(
    onNavigateBack: () -> Unit
) {
    composable<RecipeProfileDestination> { backStackEntry ->
        val destination: RecipeProfileDestination = backStackEntry.toRoute()

        RecipeProfileRoute(
            onBackClick = onNavigateBack,
            recipeId = destination.recipeId
        )
    }
}


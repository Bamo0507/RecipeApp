package com.app.recipeapp.presentation.mainFlow.recipes.recipeList

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object RecipeListDestination

fun NavGraphBuilder.recipeListScreen(
    onAccountClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    onRecipeClick: (Int) -> Unit
){
    composable<RecipeListDestination>{
        RecipeListRoute(
            onAccountClick = onAccountClick,
            onAddRecipeClick = onAddRecipeClick,
            onRecipeClick = onRecipeClick
        )
    }
}
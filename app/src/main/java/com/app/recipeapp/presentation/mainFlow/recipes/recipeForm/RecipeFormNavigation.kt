package com.app.recipeapp.presentation.mainFlow.recipes.recipeForm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object RecipeFormDestination

fun NavGraphBuilder.recipeFormScreen(
    onRecipeSaved: () -> Unit,
    onBackClick: () -> Unit
){
    composable<RecipeFormDestination>{
        RecipeFormRoute(
            onRecipeSaved = onRecipeSaved,
            onBackClick = onBackClick
        )
    }
}
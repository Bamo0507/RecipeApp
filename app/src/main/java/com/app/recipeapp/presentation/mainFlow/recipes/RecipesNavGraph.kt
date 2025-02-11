package com.app.recipeapp.presentation.mainFlow.recipes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.presentation.mainFlow.recipes.recipeForm.RecipeFormDestination
import com.app.recipeapp.presentation.mainFlow.recipes.recipeForm.recipeFormScreen
import com.app.recipeapp.presentation.mainFlow.recipes.recipeList.RecipeListDestination
import com.app.recipeapp.presentation.mainFlow.recipes.recipeList.recipeListScreen
import com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile.RecipeProfileDestination
import com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile.navigateToProfileScreen
import com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile.profileScreen
import kotlinx.serialization.Serializable

@Serializable
data object RecipesNavGraph

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
    onLogOutClick: () -> Unit,
    userPreferences: UserPreferences
){
    //Nested nav
    navigation<RecipesNavGraph>(startDestination = RecipeListDestination){
        //Recipe list
        recipeListScreen(
            //Pending
            onAddRecipeClick = {
                navController.navigate(RecipeFormDestination)
            },
            onRecipeClick = { recipe ->
                navController.navigateToProfileScreen(
                    destination = RecipeProfileDestination(
                        recipeId = recipe
                    )
                )
            },
            onAccountClick = {},
        )

        //Form Screen
        recipeFormScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onRecipeSaved = {
                navController.navigateUp()
            }
        )

        //Recipe Profile
        profileScreen(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}
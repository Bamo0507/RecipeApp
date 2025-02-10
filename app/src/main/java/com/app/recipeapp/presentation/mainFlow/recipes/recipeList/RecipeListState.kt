package com.app.recipeapp.presentation.mainFlow.recipes.recipeList

import com.app.recipeapp.data.model.Recipe

data class RecipeListState(
    val fullRecipeList: List<Recipe> = emptyList(),
    val recipeList: List<Recipe> = emptyList(),
    val filter: String = "All", // by default show everything
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
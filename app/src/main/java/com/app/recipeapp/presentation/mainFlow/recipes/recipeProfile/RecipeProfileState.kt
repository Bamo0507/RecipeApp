package com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile

import com.app.recipeapp.data.model.Recipe

data class RecipeProfileState(
    var recipe: Recipe? = null,
    var isLoading: Boolean = false
)

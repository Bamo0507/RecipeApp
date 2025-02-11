package com.app.recipeapp.presentation.mainFlow.recipes.recipeForm

data class RecipeFormState(
    val title: String = "",
    val description: String = "",
    val prepTime: String = "",
    val isFavorite: Boolean = false,
    val imagePath: String = "",
    //Errors
    val titleError: String? = null,
    val descriptionError: String? = null,
    val prepTimeError: String? = null,

)
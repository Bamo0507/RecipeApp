package com.app.recipeapp.data.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val prepTime: Int,
    val isFavorite: Boolean,
    val imagePath: String
)
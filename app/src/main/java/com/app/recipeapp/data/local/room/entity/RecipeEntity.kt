package com.app.recipeapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val prepTime: Int,
    val isFavorite: Boolean,
    val imagePath: String
)
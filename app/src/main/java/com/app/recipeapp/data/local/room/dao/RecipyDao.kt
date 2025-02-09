package com.app.recipeapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.recipeapp.data.local.room.entity.RecipeEntity

@Dao
interface RecipeDao {
    //Method to insert to the local data base
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    //Method to get All Recipes (for list screen)
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    //Method to get one of the recipes (profile screen)
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity

}
package com.app.recipeapp.data.local.room.repository

import android.content.Context
import com.app.recipeapp.data.local.room.dao.RecipeDao
import com.app.recipeapp.data.local.room.di.Dependencies
import com.app.recipeapp.data.local.room.entity.RecipeEntity

class RecipeRepository(context: Context) {
    private val recipeDao: RecipeDao = Dependencies.provideDatabase(context).recipeDao()

    //Method to insert a recipe to the db
    suspend fun insertRecipe(recipe: RecipeEntity){
        recipeDao.insertRecipe(recipe)
    }

    //Method to get all the recipes
    suspend fun getAllRecipes(): List<RecipeEntity> {
        return recipeDao.getAllRecipes()
    }

    //Method to get one of the recipes
    suspend fun getRecipeById(id: Int): RecipeEntity {
        return recipeDao.getRecipeById(id)
    }

    //Method to update favorite state
    suspend fun updateFavoriteStatus(id: Int, status: Boolean){
        return recipeDao.updateFavoriteStatus(id, status)
    }


}
package com.app.recipeapp.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.recipeapp.data.local.room.dao.RecipeDao
import com.app.recipeapp.data.local.room.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
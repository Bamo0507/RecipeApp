package com.app.recipeapp.data.local.room.di

import android.content.Context
import androidx.room.Room
import com.app.recipeapp.data.local.room.database.AppDataBase

object Dependencies {
    private var database: AppDataBase? = null

    private fun buildDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "app_database.db"
        ).build()
    }

    fun provideDatabase(context: Context): AppDataBase {
        return database ?: synchronized(this){
            database ?: buildDataBase(context).also { database = it }
        }
    }
}
package com.app.recipeapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.app.recipeapp.data.local.preferences.DataStoreUserPrefs
import com.app.recipeapp.navigation.AppNavigation
import com.app.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = DataStoreUserPrefs(dataStore)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            RecipeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        userPreferences = userPreferences
                    )
                }
            }
        }
    }
}
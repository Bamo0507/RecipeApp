package com.app.recipeapp.presentation.mainFlow.recipes.recipeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.recipeapp.data.local.room.repository.RecipeRepository
import com.app.recipeapp.data.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeListViewModel(
    var recipeRepository: RecipeRepository
): ViewModel() {

    //Manage the state
    private val _uiState: MutableStateFlow<RecipeListState> = MutableStateFlow(
        RecipeListState()
    )
    val uiState = _uiState.asStateFlow()

    // Retrieve all the recipes
    fun getAllRecipes() {
        viewModelScope.launch {
            // Start loading
            _uiState.update { it.copy(isLoading = true) }

            delay(1500) // Simulate loading delay

            val recipeListFromRepo = recipeRepository.getAllRecipes()

            // Map repository objects to your Recipe model
            val recipes = recipeListFromRepo.map {
                Recipe(
                    title = it.title,
                    description = it.description,
                    prepTime = it.prepTime,
                    isFavorite = it.isFavorite,
                    imagePath = it.imagePath
                )
            }

            // Update the state with the full list and the filtered list (based on current filter and search query)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    fullRecipeList = recipes,
                    recipeList = applyFilters(recipes, state.filter, state.searchQuery)
                )
            }
        }
    }

    // Update favorite state and refresh the list
    fun updateFavorite(id: Int, favorite: Boolean) {
        viewModelScope.launch {
            recipeRepository.updateFavoriteStatus(id, favorite)
            delay(1000) // Allow time for update
            getAllRecipes() // Re-fetch recipes (which will also reapply filters)
        }
    }

    // Update the search query and filter the recipe list accordingly
    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                recipeList = applyFilters(state.fullRecipeList, state.filter, query)
            )
        }
    }

    // Change filter to Favorites and update the displayed list
    fun filterToFavs() {
        _uiState.update { state ->
            state.copy(
                filter = "Favorites",
                recipeList = applyFilters(state.fullRecipeList, "Favorites", state.searchQuery)
            )
        }
    }

    // Change filter to order by PrepTime and update the displayed list
    fun filterToPrepTime() {
        _uiState.update { state ->
            state.copy(
                filter = "PrepTime",
                recipeList = applyFilters(state.fullRecipeList, "PrepTime", state.searchQuery)
            )
        }
    }

    // Change filter back to All and update the displayed list
    fun filterToAll() {
        _uiState.update { state ->
            state.copy(
                filter = "All",
                recipeList = applyFilters(state.fullRecipeList, "All", state.searchQuery)
            )
        }
    }

    // Function to apply filters
    private fun applyFilters(
        recipes: List<Recipe>,
        filter: String,
        query: String
    ): List<Recipe> {
        var filtered = recipes

        when (filter) {
            "Favorites" -> {
                filtered = filtered.filter { it.isFavorite }
            }
            "PrepTime" -> {
                filtered = filtered.sortedBy { it.prepTime }
            }
        }

        // Apply search query filter
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }

        return filtered
    }

    //Factory
    companion object{
        class Factory(
            private val recipeRepository: RecipeRepository
        ): ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CASE")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecipeListViewModel(recipeRepository) as T
            }
        }
    }

}
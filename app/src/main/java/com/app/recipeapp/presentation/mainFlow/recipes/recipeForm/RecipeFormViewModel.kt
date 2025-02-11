package com.app.recipeapp.presentation.mainFlow.recipes.recipeForm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.recipeapp.data.local.room.entity.RecipeEntity
import com.app.recipeapp.data.local.room.repository.RecipeRepository
import com.app.recipeapp.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeFormViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeFormState())
    val uiState = _uiState.asStateFlow()

    // Functions 2 update each field
    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle, titleError = null) }
    }

    fun onDescriptionChanged(newDescription: String) {
        _uiState.update { it.copy(description = newDescription, descriptionError = null) }
    }

    fun onPrepTimeChanged(newPrepTime: String) {
        _uiState.update { it.copy(prepTime = newPrepTime, prepTimeError = null) }
    }

    fun onToggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun onImageSelected(path: String) {
        _uiState.update { it.copy(imagePath = path) }
    }

    // Function to validate that all the fields are completed
    private fun validateForm(): Boolean {
        var isValid = true
        var titleError: String? = null
        var descriptionError: String? = null
        var prepTimeError: String? = null

        val state = _uiState.value

        if (state.title.isBlank()) {
            titleError = "Title is required."
            isValid = false
        }
        if (state.description.isBlank()) {
            descriptionError = "Description is required."
            isValid = false
        }
        if (state.prepTime.isBlank()) {
            prepTimeError = "Preparation time is required."
            isValid = false
        } else {
            val prepTimeInt = state.prepTime.toIntOrNull()
            if (prepTimeInt == null || prepTimeInt <= 0) {
                prepTimeError = "Preparation time must be a positive int"
                isValid = false
            }
        }

        // Update state
        _uiState.update { currentState ->
            currentState.copy(
                titleError = titleError,
                descriptionError = descriptionError,
                prepTimeError = prepTimeError
            )
        }
        return isValid
    }

    // Function to save a recipe
    fun saveRecipe(onSuccess: () -> Unit) {
        if (validateForm()) {
            val state = _uiState.value
            val prepTimeInt = state.prepTime.toInt()

            val recipe = Recipe(
                id = 0,
                title = state.title,
                description = state.description,
                prepTime = prepTimeInt,
                isFavorite = state.isFavorite,
                imagePath = state.imagePath
            )
            Log.d("RecipeForm", "Image Path: ${state.imagePath}")

            // map 2 entity
            val recipeEntity = recipe.toEntity()
            viewModelScope.launch {
                recipeRepository.insertRecipe(recipeEntity)
                onSuccess()
            }
        }
    }

    // Function 2 map from model class to entity
    private fun Recipe.toEntity(): RecipeEntity {
        return RecipeEntity(
            id = 0, //It's auto generated
            title = this.title,
            description = this.description,
            prepTime = this.prepTime,
            isFavorite = this.isFavorite,
            imagePath = this.imagePath
        )
    }

    companion object {
        class Factory(private val recipeRepository: RecipeRepository) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecipeFormViewModel(recipeRepository) as T
            }
        }
    }
}
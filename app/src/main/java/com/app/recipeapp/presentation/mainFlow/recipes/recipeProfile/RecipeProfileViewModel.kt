package com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.app.recipeapp.data.local.room.repository.RecipeRepository
import com.app.recipeapp.data.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeProfileViewModel(
    private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    //State variables
    private val _uiState: MutableStateFlow<RecipeProfileState> = MutableStateFlow(
        RecipeProfileState()
    )
    val uiState = _uiState.asStateFlow()

    //Take the data class info
    private val recipeProfile = savedStateHandle.toRoute<RecipeProfileDestination>()

    //Retrieve info 4 Recipe
    fun getRecipeInfo(){
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            delay(1500)

            val infoRecipeE = recipeRepository.getRecipeById(recipeProfile.recipeId)

            //map 2 model class
            val infoRecipeM = Recipe(
                id = infoRecipeE.id,
                title = infoRecipeE.title,
                description = infoRecipeE.description,
                prepTime = infoRecipeE.prepTime,
                imagePath = infoRecipeE.imagePath,
                isFavorite = infoRecipeE.isFavorite
            )

            //update info
            _uiState.update { state ->
                state.copy(
                    recipe = infoRecipeM,
                    isLoading = false
                )
            }
        }
    }

    //update Fav status
    fun updateFavStatus() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    recipe = state.recipe?.copy(isFavorite = !state.recipe!!.isFavorite)
                )
            }

            //update info in room
            _uiState.value.recipe?.let {
                recipeRepository.updateFavoriteStatus(it.id,
                    _uiState.value.recipe!!.isFavorite)
            }

        }
    }




}
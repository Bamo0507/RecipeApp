package com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.savedstate.SavedStateRegistryOwner
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
                    recipe = infoRecipeM
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

            getRecipeInfo()

        }
    }

    companion object{
        class Factory(
            private val context: Context,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle?
        ): AbstractSavedStateViewModelFactory(owner, defaultArgs){
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(
                key: String, modelClass: Class<T>, handle: SavedStateHandle
            ): T {
                val repository = RecipeRepository(context)
                return RecipeProfileViewModel(repository, handle) as T
            }
        }
    }



}
package com.app.recipeapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState: MutableStateFlow<SplashState> = MutableStateFlow(
        SplashState()
    )
    val uiState = _uiState.asStateFlow()

    val isLoggedIn = userPreferences.loggedStatus()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            verifyingLog()
        }
    }

    suspend fun verifyingLog(){
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
            delay(2000)
            _uiState.update { state ->
                state.copy(
                    isLoading = false
                )
            }
        }
    }

    companion object{
        class Factory(
            private val userPreferences: UserPreferences
        ): ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CASE")
            override fun <T: ViewModel> create(modelClass: Class<T>): T{
                return SplashViewModel(userPreferences = userPreferences) as T
            }
        }
    }
}

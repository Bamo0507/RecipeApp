package com.app.recipeapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    val userPreferences: UserPreferences,
    private val loginRepository: LoginRepository
): ViewModel() {
    //Manage State
    private val _uiState = MutableStateFlow<LoginState>(LoginState())
    val uiState = _uiState.asStateFlow()

    //onLogin click
    fun onLoginClick(){
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            //Delay time 4 verifying
            delay(4000)

            if(loginRepository.login(_uiState.value.email, _uiState.value.password)){
                userPreferences.setUserName(_uiState.value.email)
                userPreferences.logIn()

                _uiState.update { state ->
                    state.copy(
                        showError = false,
                        loginSuccess = true,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { state->
                    state.copy(
                        isLoading = false,
                        showError = true
                    )
                }
            }
        }
    }

    //Email Changed
    fun onEmailChanged(name: String){
        _uiState.update { state ->
            state.copy(
                email = name,
            )
        }
    }

    //Password Changed
    fun onPassChanged(name: String){
        _uiState.update { state ->
            state.copy(
                password = name
            )
        }
    }

    //Factory to inject UserPreferences
    companion object {
        class Factory(
            private val userPreferences: UserPreferences,
            private val loginRepository: LoginRepository
        ): ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userPreferences, loginRepository) as T
            }
        }
    }


}
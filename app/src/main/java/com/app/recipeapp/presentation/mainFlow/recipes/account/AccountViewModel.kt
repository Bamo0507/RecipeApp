package com.app.recipeapp.presentation.mainFlow.recipes.account

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.recipeapp.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userPreferences: UserPreferences
): ViewModel() {
    //State
    private val _uiState: MutableStateFlow<AccountState> = MutableStateFlow(
        AccountState()
    )
    val uiState = _uiState.asStateFlow()

    //From the start get both
    init {
        getUserName()
        getLogoAccount()
    }

    //Retrieve user
    fun getUserName(){
        viewModelScope.launch {
            val userName = userPreferences.getUserName()
            Log.d("Account", "esto se guardo: ${userName}")
            _uiState.update { state ->
                state.copy(
                    userName = userName ?: "jj",
                    isLoading = false
                )
            }
        }
    }
    //Retrieve logo
    fun getLogoAccount(){
        viewModelScope.launch {
            val filepath = userPreferences.getLogoAccount()
            _uiState.update { state ->
                state.copy(
                    filepath = filepath ?: ""
                )
            }
        }
    }

    //LogOut
    fun userLogOut(){
        viewModelScope.launch {
            userPreferences.logOut()
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
        }
    }

    //Change profile
    fun setLogoAccount(filepath: String){
        viewModelScope.launch {
            userPreferences.setLogoAccount(filepath)
            _uiState.update { state ->
                state.copy(
                    filepath = filepath
                )
            }
        }
    }

    companion object {
        class Factory(
            private val userPreferences: UserPreferences
        ): ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return AccountViewModel(userPreferences) as T
            }
        }
    }
}
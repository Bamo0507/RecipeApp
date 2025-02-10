package com.app.recipeapp.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Style
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.recipeapp.ui.theme.RecipeAppTheme
import com.app.recipeapp.R
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.presentation.reusableScreens.RecipeLoadingScreen

@Composable
fun loginRoute(
    onLoginClick: () -> Unit,
    userPreferences: UserPreferences
){
    val loginRepository = LocalLoginRepository()
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Companion.Factory(
            userPreferences,
            loginRepository
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.loginSuccess){
        if(state.loginSuccess){
            onLoginClick()
        }
    }

    loginScreen(
        state = state,
        onNameChange = { viewModel.onEmailChanged(it) },
        onPasswordChange = {viewModel.onPassChanged(it)},
        onLogIn = {viewModel.onLoginClick()}
    )
}

@Composable
fun loginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogIn: () -> Unit
){
    if(state.isLoading){
        RecipeLoadingScreen()
    } else{
        Column(
            modifier = modifier.fillMaxSize()
        ){
            // Background image with text at the bottom
            Box(
                modifier = Modifier.weight(0.45f)
            ) {
                // Main Image
                Image(
                    painter = painterResource(id = R.drawable.logbackground), // Your image
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Blurred effect at the bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )

                // Text positioned in the bottom-left corner
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 26.dp, bottom = 18.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            //Outlined Text Boxes
            OutlinedTextField(
                value = state.email,
                onValueChange = { onNameChange(it) },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                placeholder = { Text("username@gmail.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )

            // Password Text Field
            OutlinedTextField(
                value = state.password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Password") },
                placeholder = { Text("pass123") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
            )

            // Just to make it more appealing :)
            Spacer(modifier = Modifier.height(26.dp))

            //Button 2 login
            Button(
                onClick = { onLogIn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.height(42.dp)){
                    Text(
                        text = stringResource(R.string.loginButton),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            //Error message
            if(state.showError){
                Text(
                    text = stringResource(R.string.loginError),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.loginFooter),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun loginScreenPreview() {
    RecipeAppTheme {
        Surface {
            loginScreen(
                modifier = Modifier.fillMaxSize(),
                state = LoginState(email = "", password = ""), // Mock state
                onNameChange = {}, // Dummy function
                onPasswordChange = {}, // Dummy function
                onLogIn = {} // Dummy function
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun loginScreenErrorPreview() {
    RecipeAppTheme {
        Surface {
            loginScreen(
                modifier = Modifier.fillMaxSize(),
                state = LoginState(email = "", password = "", showError = true), // Mock state
                onNameChange = {}, // Dummy function
                onPasswordChange = {}, // Dummy function
                onLogIn = {} // Dummy function
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun loginScreenDarkPreview() {
    RecipeAppTheme(darkTheme = true) {
        Surface {
            loginScreen(
                modifier = Modifier.fillMaxSize(),
                state = LoginState(email = "", password = ""),
                onNameChange = {},
                onPasswordChange = {},
                onLogIn = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun loginScreenDarkErrorPreview() {
    RecipeAppTheme(darkTheme = true) {
        Surface {
            loginScreen(
                modifier = Modifier.fillMaxSize(),
                state = LoginState(email = "", password = "", showError = true),
                onNameChange = {},
                onPasswordChange = {},
                onLogIn = {}
            )
        }
    }
}
package com.app.recipeapp.presentation.mainFlow.recipes.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.recipeapp.data.local.preferences.UserPreferences
import com.app.recipeapp.R

@Composable
fun AccountRoute(
    onLogOutClick: () -> Unit,
    userPreferences: UserPreferences,
    onBackClick: () -> Unit
){
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModel.Companion.Factory(
            userPreferences = userPreferences
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AccountScreen(
        onLogOutClick = {
                        onLogOutClick()
                        viewModel.userLogOut()
                        },
        state = state,
        changePath = {filepath -> viewModel.setLogoAccount(filepath)},
        onBackClick = onBackClick
    )
}

@Composable
fun AccountScreen(
    onLogOutClick: () -> Unit,
    state: AccountState,
    changePath: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_background),
                contentDescription = "City Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = "go back",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Here i have to include the image
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                ) {
                    if (state.filepath == "") {
                        Image(
                            painter = painterResource(R.drawable.account_default),
                            contentDescription = "Default image to show",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            model = when {
                                state.filepath.startsWith("http") -> state.filepath
                                state.filepath.startsWith("file://") -> state.filepath
                                state.filepath.startsWith("content://") -> state.filepath
                                else -> "file://${state.filepath}"
                            },
                            contentDescription = "Image Selected",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = state.userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(88.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccountOption(
                icon = Icons.Default.ExitToApp,
                label = stringResource(id = R.string.logoutScreen),
                iconTint = MaterialTheme.colorScheme.primary
            ) {
                onLogOutClick()
            }
        }
    }
}

@Composable
fun AccountOption(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    navAction: () -> Unit = {}
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navAction() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}
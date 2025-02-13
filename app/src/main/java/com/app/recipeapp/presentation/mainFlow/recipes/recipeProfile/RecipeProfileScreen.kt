package com.app.recipeapp.presentation.mainFlow.recipes.recipeProfile

import android.icu.text.UnicodeSet.SpanCondition
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.recipeapp.data.model.Recipe
import com.app.recipeapp.ui.theme.RecipeAppTheme
import com.app.recipeapp.R

@Composable
fun RecipeProfileRoute(
    onBackClick: () -> Unit,
    recipeId: Int
){
    val context = LocalContext.current
    val owner = LocalSavedStateRegistryOwner.current

    val viewModel: RecipeProfileViewModel = viewModel(
        factory = RecipeProfileViewModel.Companion.Factory(
            context,
            owner,
            defaultArgs = bundleOf("recipeId" to recipeId)
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Add scrollable, just in case the description is too long
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getRecipeInfo()
    }

    RecipeProfileScreen(
        onBackClick = onBackClick,
        onFavClick = {viewModel.updateFavStatus()},
        state = state,
        scrollState = scrollState
    )

}

@Composable
fun RecipeProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFavClick: () -> Unit,
    state: RecipeProfileState,
    scrollState: ScrollState
) {


    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Display Image
                if(state.recipe?.imagePath != ""){
                    AsyncImage(
                        model = state.recipe?.imagePath ?: "default_image_url_or_path",
                        contentDescription = "Image Selected",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    )
                } else {
                    Image(
                        painterResource(R.drawable.list_image),
                        contentDescription = "Stock image if filepath not found",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))

                    )
                }

                // Back & Favorite Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIos,
                            contentDescription = "Go Back",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = onFavClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = if (state.recipe?.isFavorite == true) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recipe Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .offset(y = (-50).dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.recipe?.title ?: "Recipe Name",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recipe Time Info
                    RecipeInfoItem(Icons.Outlined.AccessTime, "${state.recipe?.prepTime ?: 0} min")

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Recipe Description
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 48.dp)
            ) {
                Text(
                    text = stringResource(R.string.profileDesc),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                state.recipe?.let {
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }

            // Fade effect to indicate scrolling
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(MaterialTheme.colorScheme.background.copy(alpha = 0.5f), MaterialTheme.colorScheme.background)
                        )
                    )
            )
        }
    }
}




@Composable
fun RecipeInfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun RecipeProfileScreenPreview() {
    RecipeAppTheme {
        Surface{
            RecipeProfileScreen(
                modifier = Modifier.fillMaxSize(),
                onFavClick = {},
                onBackClick = {},
                state = RecipeProfileState(
                    recipe = Recipe(
                        id = 1,
                        title = "Pasta",
                        description = "Pasta con crema y pollo, banada en hongos",
                        prepTime = 100,
                        isFavorite = false,
                        imagePath = "",
                    )
                ),
                scrollState = rememberScrollState()
            )
        }
    }

}
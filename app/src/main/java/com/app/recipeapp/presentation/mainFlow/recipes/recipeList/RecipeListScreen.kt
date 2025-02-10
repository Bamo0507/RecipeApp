package com.app.recipeapp.presentation.mainFlow.recipes.recipeList

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Query
import coil.compose.AsyncImage
import com.app.recipeapp.data.local.room.entity.RecipeEntity
import com.app.recipeapp.data.local.room.repository.RecipeRepository
import com.app.recipeapp.data.model.Recipe
import com.app.recipeapp.presentation.mainFlow.recipes.recipeList.RecipeListViewModel
import com.app.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RecipeListRoute(
    onAccountClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    onRecipeClick: (Int) -> Unit
){
    val context = LocalContext.current

    val viewModel: RecipeListViewModel = viewModel(
        factory = RecipeListViewModel.Companion.Factory(
            recipeRepository = RecipeRepository(context)
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAllRecipes()
    }

    RecipeListScreen(
        state = state,
        onAccountClick = onAccountClick,
        onAddRecipeClick = onAddRecipeClick,
        onRecipeClick = onRecipeClick,
        onSearchQuery = {viewModel.updateSearchQuery(it)},
        onFilterAll = {viewModel.filterToAll()},
        onFilterToFavs = {viewModel.filterToFavs()},
        onFilterToPrepTime = {viewModel.filterToPrepTime()},
        updateFavorite = { id, status -> viewModel.updateFavorite(id, status) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    state: RecipeListState,
    onAccountClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    onSearchQuery: (String) -> Unit,
    onFilterAll: () -> Unit,
    onFilterToFavs: () -> Unit,
    onFilterToPrepTime: () -> Unit,
    updateFavorite: (id: Int, status: Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(0.3f))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Recipes",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                actions = {
                    IconButton(onClick = { onAccountClick }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRecipeClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "Search Recipes",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filter Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterText(
                    text = "All",
                    selected = state.filter == "All",
                    onClick = onFilterAll
                )
                FilterText(
                    text = "Favorites",
                    selected = state.filter == "Favorites",
                    onClick = onFilterToFavs
                )
                FilterText(
                    text = "PrepTime",
                    selected = state.filter == "PrepTime",
                    onClick = onFilterToPrepTime
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recipes made
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.recipeList) { recipe ->
                    RecipeGridItem(
                        recipe = recipe,
                        onFavoriteClick = { updateFavorite(recipe.id, !recipe.isFavorite) },
                        onRecipeClick = { onRecipeClick(recipe.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterText(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
            )
        )
        // Underline indicator if selected
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun RecipeGridItem(
    recipe: Recipe,
    onFavoriteClick: () -> Unit,
    onRecipeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecipeClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = if (recipe.imagePath.startsWith("file://") || recipe.imagePath.startsWith("http"))
                        recipe.imagePath
                    else
                        "file://${recipe.imagePath}", 
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (recipe.isFavorite) Color.Red else Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Recipe title
            Text(
                text = recipe.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            // Prep time
            Text(
                text = "${recipe.prepTime} min",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


private val sampleRecipes = listOf(
    Recipe(
        id = 1,
        title = "Spaghetti",
        description = "Deliciosa pasta italiana",
        prepTime = 30,
        isFavorite = false,
        imagePath = ""
    ),
    Recipe(
        id = 2,
        title = "Sushi",
        description = "Rollos frescos",
        prepTime = 45,
        isFavorite = false,
        imagePath = ""
    ),
    Recipe(
        id = 3,
        title = "Burger",
        description = "Hamburguesa jugosa",
        prepTime = 20,
        isFavorite = false,
        imagePath = ""
    )
)

@Preview(
    name = "RecipeListScreen - All (Light Mode)",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun RecipeListScreenPreview() {
    RecipeAppTheme {
        RecipeListScreen(
            state = RecipeListState(
                recipeList = sampleRecipes,
                filter = "Favorites",
                searchQuery = "",
                isLoading = false
            ),
            onAccountClick = {},
            onAddRecipeClick = {},
            onRecipeClick = { /* Recibe el id, por ejemplo: */ },
            onSearchQuery = {},
            onFilterAll = {},
            onFilterToFavs = {},
            onFilterToPrepTime = {},
            updateFavorite = { _, _ -> }
        )
    }
}

@Preview(
    name = "RecipeListScreen - All (Light Mode)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun RecipeListScreenDarkPreview() {
    RecipeAppTheme {
        RecipeListScreen(
            state = RecipeListState(
                recipeList = sampleRecipes,
                filter = "Favorites",
                searchQuery = "",
                isLoading = false
            ),
            onAccountClick = {},
            onAddRecipeClick = {},
            onRecipeClick = { /* Recibe el id, por ejemplo: */ },
            onSearchQuery = {},
            onFilterAll = {},
            onFilterToFavs = {},
            onFilterToPrepTime = {},
            updateFavorite = { _, _ -> }
        )
    }
}
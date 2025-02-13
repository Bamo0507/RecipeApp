package com.app.recipeapp.presentation.mainFlow.recipes.recipeForm

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.recipeapp.data.local.room.repository.RecipeRepository
import com.app.recipeapp.ui.theme.RecipeAppTheme
import java.io.File
import com.app.recipeapp.R

@Composable
fun RecipeFormRoute(
    onRecipeSaved: () -> Unit,
    onBackClick: () -> Unit
){
    val context = LocalContext.current

    val viewModel: RecipeFormViewModel = viewModel(
        factory = RecipeFormViewModel.Companion.Factory(
            recipeRepository = RecipeRepository(context)
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    RecipeFormScreen(
        onBack = onBackClick,
        state = state,
        onImageSelected = {image -> viewModel.onImageSelected(image.toString())},
        onTitleChange = { viewModel.onTitleChanged(it)},
        onDescriptionChange = { viewModel.onDescriptionChanged(it) },
        onPrepTimeChange = {viewModel.onPrepTimeChanged(it)},
        onToggleFavorite = {viewModel.onToggleFavorite()},
        onSaveRecipe = {viewModel.saveRecipe(onRecipeSaved)},
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    state: RecipeFormState,
    onBack: () -> Unit,
    onImageSelected: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrepTimeChange: (String) -> Unit,
    onToggleFavorite: (Boolean) -> Unit,
    onSaveRecipe: () -> Unit,
    context: Context
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val savedPath = copyImageToInternalStorage(context, selectedUri)
            savedPath?.let { onImageSelected(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(R.string.newRecipe),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIos,
                            contentDescription = "Go back",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*
            Set of outlined text fields for the forms
             */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = { Text(stringResource(R.string.recipeTitle)) },
                    isError = state.titleError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Title,
                            contentDescription = "Title Field",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true
                )
                if (state.titleError != null) {
                    Text(
                        text = state.titleError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescriptionChange,
                    label = { Text(stringResource(R.string.recipeDesc)) },
                    isError = state.descriptionError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    trailingIcon = { Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = "Decription Field",
                        tint = MaterialTheme.colorScheme.primary)
                    },
                    singleLine = true
                )
                if (state.descriptionError != null) {
                    Text(
                        text = state.descriptionError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.prepTime,
                    onValueChange = onPrepTimeChange,
                    label = { Text(stringResource(R.string.recipePrep)) },
                    isError = state.prepTimeError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    trailingIcon = { Icon(
                        imageVector = Icons.Default.AccessAlarm,
                        contentDescription = "Prep Time Field",
                        tint = MaterialTheme.colorScheme.primary)
                    },
                    singleLine = true
                )
                if (state.prepTimeError != null) {
                    Text(
                        text = state.prepTimeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isFavorite,
                        onCheckedChange = onToggleFavorite
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.recipeFav))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Pick Image")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.recipeImage))
                }

                if (state.imagePath.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        AsyncImage(
                            model = when {
                                state.imagePath.startsWith("http") -> state.imagePath
                                state.imagePath.startsWith("file://") -> state.imagePath
                                state.imagePath.startsWith("content://") -> state.imagePath
                                else -> "file://${state.imagePath}"
                            },
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }

                // Button for saving recipe
                Button(
                    onClick = onSaveRecipe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "Pick Image")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.recipeSave))
                }
            }

        }
    }
}

@Preview(
    name = "RecipeFormScreen Preview - Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun RecipeFormScreenPreviewLight() {
    RecipeAppTheme {
        RecipeFormScreen(
            state = RecipeFormState(
                title = "Ejemplo Título",
                description = "Ejemplo Descripción",
                prepTime = "45",
                isFavorite = false,
                imagePath = "https://via.placeholder.com/300",
                titleError = null,
                descriptionError = null,
                prepTimeError = null
            ),
            onBack = {},
            onImageSelected = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPrepTimeChange = {},
            onToggleFavorite = {},
            onSaveRecipe = {},
            context = LocalContext.current
        )
    }
}

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val file = File(context.filesDir, "recipe_${System.currentTimeMillis()}.jpg")

    file.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return file.absolutePath
}

@Preview(
    name = "RecipeFormScreen Preview - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun RecipeFormScreenPreviewDark() {
    RecipeAppTheme {
        RecipeFormScreen(
            state = RecipeFormState(
                title = "Ejemplo Título",
                description = "Ejemplo Descripción",
                prepTime = "45",
                isFavorite = true,
                imagePath = "https://via.placeholder.com/300",
                titleError = null,
                descriptionError = null,
                prepTimeError = null
            ),
            onBack = {},
            onImageSelected = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPrepTimeChange = {},
            onToggleFavorite = {},
            onSaveRecipe = {},
            context = LocalContext.current
        )
    }
}
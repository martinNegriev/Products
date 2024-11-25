package com.example.productstask.favorites.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productstask.R
import com.example.productstask.favorites.presentation.viewmodel.FavoritesViewModel
import com.example.productstask.productsSearch.presentation.screen.ErrorScreen
import com.example.productstask.productsSearch.presentation.screen.ProductItem
import com.example.productstask.ui.NestedAppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    navController: NavController,
) {
    NestedAppScaffold(navController = navController, title = { }, topAppBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.favorites_hint)) }, actions = {})
    }) {
        FavoritesList(viewModel = viewModel)
    }
}

@Composable
fun FavoritesList(viewModel: FavoritesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var reload by remember { mutableStateOf(false) }
    LaunchedEffect(reload) {
        viewModel.getFavorites()
        reload = false
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (uiState.error != null) {
        ErrorScreen(uiState.error ?: stringResource(R.string.generic_error))
    } else {
        if (uiState.favorites.isNotEmpty()) {
            LazyColumn {
                items(uiState.favorites) { item ->
                    ProductItem(item = item, viewModel = viewModel) {
                        reload = true
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.no_favorites_found),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

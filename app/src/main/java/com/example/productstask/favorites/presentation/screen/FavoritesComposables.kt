package com.example.productstask.favorites.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        FavoritesList(viewModel = viewModel, navController = navController)
    }
}

@Composable
fun FavoritesList(
    viewModel: FavoritesViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    var orientation by remember { mutableIntStateOf(configuration.orientation) }

    LaunchedEffect(configuration) {
        snapshotFlow {
            configuration.orientation
        }.collect { orientation = it }
    }

    var reload by remember { mutableStateOf(false) }
    LaunchedEffect(reload) {
        viewModel.getFavorites()
        reload = false
    }
    if (uiState.error != null) {
        ErrorScreen(uiState.error ?: stringResource(R.string.generic_error))
    } else {
        if (uiState.favorites.isNotEmpty()) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                LazyColumn {
                    items(uiState.favorites) { item ->
                        ProductItem(
                            item = item,
                            navController = navController,
                            viewModel = viewModel,
                        ) {
                            reload = true
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(uiState.favorites.size) { index ->
                        ProductItem(
                            item = uiState.favorites[index],
                            navController = navController,
                            viewModel = viewModel,
                        ) {
                            reload = true
                        }
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

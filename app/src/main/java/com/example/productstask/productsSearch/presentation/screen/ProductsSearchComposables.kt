package com.example.productstask.productsSearch.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.productstask.R
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import com.example.productstask.productsSearch.presentation.viewModel.ToggleFavoriteViewModel
import com.example.productstask.ui.NestedAppScaffold
import com.example.productstask.ui.ScreenRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchScreen(
    viewModel: ProductsSearchViewModel = viewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    NestedAppScaffold(navController = navController, title = { }, topAppBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.search_hint)) }, actions = {
            IconButton(onClick = { navController.navigate(ScreenRoutes.FAVORITES.route) }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    tint = Color.Red,
                    contentDescription = stringResource(R.string.search_hint),
                )
            }
        })
    }) {
        Column {
            ProductsSearchBar { query ->
                viewModel.getProducts(refresh = true, query = query)
            }
            ProductsList(viewModel = viewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchBar(onSearch: (String?) -> Unit) {
    var text by remember { mutableStateOf("") }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
    SearchBar(
        modifier =
            Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                .fillMaxWidth(),
        windowInsets = WindowInsets(top = 0.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            onSearch.invoke(text)
        },
        active = false,
        onActiveChange = {},
        placeholder = {
            Text(text = stringResource(R.string.search_placeholder))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_hint),
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onSearch.invoke(null)
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_hint),
                    )
                }
            }
        },
    ) {}
    HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
}

@Composable
fun ProductsList(
    viewModel: ProductsSearchViewModel,
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

    LaunchedEffect(Unit) {
        viewModel.getProducts(refresh = false)
        viewModel.getProducts(refresh = true)
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (uiState.error != null) {
        ErrorScreen(uiState.error ?: stringResource(R.string.generic_error))
    } else {
        if (uiState.products.isNotEmpty()) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                LazyColumn {
                    items(uiState.products) { item ->
                        ProductItem(
                            item = item,
                            navController = navController,
                            viewModel = viewModel,
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(uiState.products.size) { index ->
                        ProductItem(
                            item = uiState.products[index],
                            navController = navController,
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = message,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(start = 16.dp, end = 16.dp),
            fontSize = 20.sp,
        )
    }
}

@Composable
fun ProductItem(
    item: ProductEntity,
    viewModel: ToggleFavoriteViewModel,
    navController: NavController? = null,
    onToggle: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier.clickable(enabled = navController != null) {
                navController?.navigate(ScreenRoutes.PRODUCT_DETAILS.route + "/${item.id}")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                model = item.thumbnail,
                contentDescription = "",
            )
            Text(text = item.title, fontSize = 20.sp)
            Text(text = item.description, fontSize = 16.sp)
        }
        IconButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp),
            onClick = {
                viewModel.toggleFavorite(id = item.id, isFavorite = !item.favorite)
                onToggle()
            },
        ) {
            Icon(
                imageVector = if (item.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                tint = if (item.favorite) Color.Red else Color.Black,
                contentDescription = stringResource(R.string.toggle_favorite),
            )
        }
    }
}

package com.example.productstask.productsSearch.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productstask.R
import com.example.productstask.productsSearch.data.remote.model.Product
import com.example.productstask.productsSearch.presentation.state.ProductsSearchUiState
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import com.example.productstask.ui.NestedAppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchScreen(
    viewModel: ProductsSearchViewModel = viewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    NestedAppScaffold(navController = navController, title = { }, topAppBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.search_hint)) }, actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    tint = Color.Red,
                    contentDescription = stringResource(R.string.search_hint),
                )
            }
        })
    }) {
        ProductsSearchBar(uiState = uiState)
        ProductsList(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchBar(uiState: ProductsSearchUiState) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    HorizontalDivider(thickness = 1.dp)

    SearchBar(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        windowInsets = WindowInsets(top = 16.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
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
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_hint),
                    )
                }
            }
        },
    ) {}

}

@Composable
fun ProductsList(viewModel: ProductsSearchViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = uiState.error ?: stringResource(R.string.generic_error),
                modifier = Modifier.align(Alignment.Center).padding(start = 16.dp, end = 16.dp),
                fontSize = 20.sp
            )
        }
    } else {
        if (uiState.products.isNotEmpty()) {
            LazyColumn {
                items(uiState.products) { item ->
                    ProductItem(item)
                }
            }
        }
    }
}

@Composable
fun ProductItem(item: Product) {
    Column {
        Text(text = item.title)
        Text(text = item.description)
    }
}
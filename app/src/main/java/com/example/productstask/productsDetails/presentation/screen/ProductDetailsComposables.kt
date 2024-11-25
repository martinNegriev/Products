package com.example.productstask.productsDetails.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.productstask.productsDetails.presentation.viewmodel.ProductDetailsViewModel
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import com.example.productstask.productsSearch.presentation.screen.ProductItem
import com.example.productstask.ui.NestedAppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = viewModel(),
    navController: NavController,
    id: Int,
) {
    val configuration = LocalConfiguration.current
    var orientation by remember { mutableIntStateOf(configuration.orientation) }

    LaunchedEffect(configuration) {
        snapshotFlow {
            configuration.orientation
        }.collect { orientation = it }
    }

    NestedAppScaffold(navController = navController, title = { }, topAppBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.details)) }, actions = {})
    }) {
        LaunchedEffect(Unit) {
            viewModel.getProductById(listOf(id))
        }
        viewModel.uiState.collectAsState().value?.let {
            LazyColumn {
                item {
                    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        ProductItem(viewModel = viewModel, item = it) {
                            viewModel.toggleFavorite(id = it.id, isFavorite = !it.favorite)
                        }
                    } else {
                        ProductItemDetailsLandscape(viewModel = viewModel, item = it) {
                            viewModel.toggleFavorite(id = it.id, isFavorite = !it.favorite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemDetailsLandscape(
    viewModel: ProductDetailsViewModel,
    item: ProductEntity,
    onToggle: () -> Unit = {},
) {
    Row {
        Box(modifier = Modifier.weight(1f)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                model = item.thumbnail,
                contentDescription = "",
            )
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
        Column(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
            Text(text = item.title, fontSize = 20.sp)
            Text(text = item.description, fontSize = 16.sp)
        }
    }
}

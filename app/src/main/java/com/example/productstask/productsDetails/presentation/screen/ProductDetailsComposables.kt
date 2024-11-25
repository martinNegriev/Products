package com.example.productstask.productsDetails.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productstask.R
import com.example.productstask.productsDetails.presentation.viewmodel.ProductDetailsViewModel
import com.example.productstask.productsSearch.presentation.screen.ProductItem
import com.example.productstask.ui.NestedAppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = viewModel(),
    navController: NavController,
    id: Int,
) {
    NestedAppScaffold(navController = navController, title = { }, topAppBar = {
        TopAppBar(title = { Text(text = stringResource(R.string.details)) }, actions = {})
    }) {
        LaunchedEffect(Unit) {
            viewModel.getProductById(id)
        }
        viewModel.uiState.collectAsState().value?.let {
            LazyColumn {
                item {
                    ProductItem(viewModel = viewModel, navController = null, item = it)
                }
            }
        }
    }
}

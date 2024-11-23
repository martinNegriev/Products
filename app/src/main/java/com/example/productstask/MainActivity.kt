package com.example.productstask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productstask.productsSearch.presentation.screen.ProductsSearchScreen
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import com.example.productstask.ui.ScreenRoutes
import com.example.productstask.ui.theme.ProductsTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var productsSearchViewModel: ProductsSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductsTaskTheme {
                Navigation(
                    productsSearchViewModel = productsSearchViewModel,
                )
            }
        }
    }
}

@Composable
fun Navigation(productsSearchViewModel: ProductsSearchViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenRoutes.PRODUCTS_SEARCH.route) {
        composable(ScreenRoutes.PRODUCTS_SEARCH.route) {
            ProductsSearchScreen(navController = navController, viewModel = productsSearchViewModel)
        }

        composable(ScreenRoutes.FAVORITES.route) {
        }
        composable(ScreenRoutes.PRODUCT_DETAILS.route) {
        }
    }
}

package com.example.productstask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.productstask.favorites.presentation.screen.FavoritesScreen
import com.example.productstask.favorites.presentation.viewmodel.FavoritesViewModel
import com.example.productstask.productsDetails.presentation.screen.ProductDetailsScreen
import com.example.productstask.productsDetails.presentation.viewmodel.ProductDetailsViewModel
import com.example.productstask.productsSearch.presentation.screen.ErrorScreen
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

    @Inject
    lateinit var favoritesViewModel: FavoritesViewModel

    @Inject
    lateinit var productDetailsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductsTaskTheme {
                Navigation(
                    productsSearchViewModel = productsSearchViewModel,
                    favoritesViewModel = favoritesViewModel,
                    productDetailsViewModel = productDetailsViewModel,
                )
            }
        }
    }
}

@Composable
fun Navigation(
    productsSearchViewModel: ProductsSearchViewModel,
    favoritesViewModel: FavoritesViewModel,
    productDetailsViewModel: ProductDetailsViewModel,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenRoutes.PRODUCTS_SEARCH.route) {
        composable(ScreenRoutes.PRODUCTS_SEARCH.route) {
            ProductsSearchScreen(navController = navController, viewModel = productsSearchViewModel)
        }

        composable(ScreenRoutes.FAVORITES.route) {
            FavoritesScreen(navController = navController, viewModel = favoritesViewModel)
        }
        composable(
            route = ScreenRoutes.PRODUCT_DETAILS.route + "/{id}",
            arguments =
                listOf(
                    navArgument("id") {
                        defaultValue = -1
                        type = IntType
                    },
                ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            if (id != -1) {
                ProductDetailsScreen(navController = navController, viewModel = productDetailsViewModel, id = id)
            } else {
                ErrorScreen(message = stringResource(R.string.product_not_found))
            }
        }
    }
}

package com.example.productstask.favorites

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productstask.BaseTest
import com.example.productstask.favorites.presentation.viewmodel.FavoritesViewModel
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesTest : BaseTest() {
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var productsSearchViewModel: ProductsSearchViewModel

    @Before
    override fun setup() {
        super.setup()
        favoritesViewModel = FavoritesViewModel(favoritesRepository = favoritesRepository)
        productsSearchViewModel =
            ProductsSearchViewModel(
                productsSearchRepository = productsSearchRepository,
                favoritesRepository = favoritesRepository,
            )
    }

    @Test
    fun `test getFavorites`() =
        runTest {
            withTimeout(5000) {
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    favoritesViewModel.toggleFavorite(id = 1, isFavorite = true)
                    favoritesViewModel.getFavorites()
                    favoritesViewModel.uiState.distinctUntilChangedBy {
                        assert(it.favorites.size == 1)
                        assert(it.favorites.first().favorite)
                    }
                }
            }
        }

    @Test
    fun `test getFavorites when empty`() =
        runTest {
            withTimeout(5000) {
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    favoritesViewModel.getFavorites()
                    favoritesViewModel.uiState.distinctUntilChangedBy {
                        assert(it.error?.equals("No favorites found") == true)
                    }
                }
            }
        }
}

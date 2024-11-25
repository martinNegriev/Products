package com.example.productstask.productDetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productstask.BaseTest
import com.example.productstask.productsDetails.presentation.viewmodel.ProductDetailsViewModel
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDetailsTest : BaseTest() {
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var productsSearchViewModel: ProductsSearchViewModel

    @Before
    override fun setup() {
        super.setup()
        productDetailsViewModel =
            ProductDetailsViewModel(
                favoritesRepository = favoritesRepository,
                productsSearchRepository = productsSearchRepository,
            )
        productsSearchViewModel =
            ProductsSearchViewModel(
                productsSearchRepository = productsSearchRepository,
                favoritesRepository = favoritesRepository,
            )
    }

    @Test
    fun `test getProductById`() =
        runTest {
            withTimeout(8000) {
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    productDetailsViewModel.getProductById(ids = listOf(1))
                    productDetailsViewModel.uiState.distinctUntilChangedBy {
                        assert(it?.id == 1)
                    }
                }
            }
        }

    @Test
    fun `test toggleFavorite`() =
        runTest {
            withTimeout(5000) {
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    val testProduct = it.products.firstOrNull()
                    testProduct?.let { product ->
                        productDetailsViewModel.toggleFavorite(id = product.id, isFavorite = !product.favorite)
                    }
                    productDetailsViewModel.uiState.distinctUntilChangedBy { product ->
                        assert(product?.favorite == !testProduct?.favorite!!)
                    }
                }
            }
        }
}

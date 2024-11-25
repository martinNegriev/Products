package com.example.productstask.productsSearch

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productstask.BaseTest
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductsSearchTest : BaseTest() {
    private lateinit var productsSearchViewModel: ProductsSearchViewModel

    @Before
    override fun setup() {
        super.setup()
        productsSearchViewModel =
            ProductsSearchViewModel(
                productsSearchRepository = productsSearchRepository,
                favoritesRepository = favoritesRepository,
            )
    }

    @Test
    fun `test getProducts`() =
        runTest {
            withTimeout(5000) {
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    assert(it.products.isNotEmpty())
                }
            }
        }

    @Test
    fun `test toggleFavorite`() =
        runTest {
            withTimeout(5000) {
                var testProduct: ProductEntity? = null
                productsSearchViewModel.getProducts(refresh = true)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    testProduct = it.products.firstOrNull()
                }
                testProduct?.let {
                    productsSearchViewModel.toggleFavorite(id = it.id, isFavorite = !it.favorite)
                }
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    assert(it.products.firstOrNull()?.favorite == !testProduct?.favorite!!)
                }
            }
        }

    @Test
    fun `test getProductsBySearchCriteria`() =
        runTest {
            withTimeout(5000) {
                val query = "essence"
                productsSearchViewModel.getProducts(refresh = true, query = query)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    assert(
                        it.products
                            .firstOrNull()
                            ?.title
                            ?.contains(query) == true,
                    )
                }
            }
        }

    @Test
    fun `test getProductsByWrongSearchCriteria`() =
        runTest {
            withTimeout(5000) {
                val query = "dummyNonExistentQuery"
                productsSearchViewModel.getProducts(refresh = true, query = query)
                productsSearchViewModel.uiState.distinctUntilChangedBy {
                    assert(
                        it.error?.contains(query) == true,
                    )
                }
            }
        }
}

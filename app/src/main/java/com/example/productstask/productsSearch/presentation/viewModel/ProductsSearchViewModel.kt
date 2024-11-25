package com.example.productstask.productsSearch.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productstask.R
import com.example.productstask.favorites.data.FavoritesRepository
import com.example.productstask.productsSearch.data.ProductsSearchRepository
import com.example.productstask.productsSearch.presentation.state.ProductsSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsSearchViewModel
    @Inject
    constructor(
        private val productsSearchRepository: ProductsSearchRepository,
        private val favoritesRepository: FavoritesRepository,
        private val context: Context,
    ) : ViewModel(),
        ToggleFavoriteViewModel {
        private val _uiState = MutableStateFlow(ProductsSearchUiState())
        val uiState: StateFlow<ProductsSearchUiState> = _uiState

        fun getProducts(refresh: Boolean = false) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                try {
                    val products = productsSearchRepository.getProducts(refresh)
                    if (products != null) {
                        _uiState.update {
                            it.copy(isLoading = false, products = products)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = context.getString(R.string.no_products_found),
                            )
                        }
                    }
                } catch (unknownHostException: java.net.UnknownHostException) {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
            }
        }

        override fun toggleFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch {
                favoritesRepository.toggleFavorite(id = id, isFavorite = isFavorite)
                _uiState.update {
                    it.copy(
                        products =
                            it.products.map { product ->
                                if (product.id == id) {
                                    product.copy(favorite = !product.favorite)
                                } else {
                                    product
                                }
                            },
                    )
                }
            }
        }
    }

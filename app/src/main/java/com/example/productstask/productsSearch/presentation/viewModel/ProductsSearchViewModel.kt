package com.example.productstask.productsSearch.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productstask.R
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
        private val context: Context,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProductsSearchUiState())
        val uiState: StateFlow<ProductsSearchUiState> = _uiState

        fun getProducts() {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                try {
                    val products = productsSearchRepository.getProducts().body()
                    if (products != null) {
                        _uiState.update {
                            it.copy(isLoading = false, products = products.products)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = context.getString(R.string.products_search_error),
                            )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
            }
        }
    }

package com.example.productstask.productsDetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productstask.favorites.data.FavoritesRepository
import com.example.productstask.productsSearch.data.ProductsSearchRepository
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import com.example.productstask.productsSearch.presentation.viewModel.ToggleFavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
        private val productsSearchRepository: ProductsSearchRepository,
    ) : ViewModel(),
        ToggleFavoriteViewModel {
        private val _uiState = MutableStateFlow<ProductEntity?>(null)
        val uiState: StateFlow<ProductEntity?> = _uiState

        override fun toggleFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch {
                favoritesRepository.toggleFavorite(id = id, isFavorite = isFavorite)
                _uiState.update {
                    it?.copy(favorite = isFavorite)
                }
            }
        }

        fun getProductById(ids: List<Int>) {
            viewModelScope.launch {
                _uiState.update { productsSearchRepository.getProductsById(ids).first() }
            }
        }
    }

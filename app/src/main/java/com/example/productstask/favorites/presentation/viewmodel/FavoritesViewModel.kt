package com.example.productstask.favorites.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productstask.favorites.data.FavoritesRepository
import com.example.productstask.favorites.presentation.state.FavoritesUiState
import com.example.productstask.productsSearch.presentation.viewModel.ToggleFavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
    ) : ViewModel(),
        ToggleFavoriteViewModel {
        private val _uiState = MutableStateFlow(FavoritesUiState())
        val uiState: StateFlow<FavoritesUiState> = _uiState

        fun getFavorites() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val favorites = favoritesRepository.getFavorites()
                    _uiState.update { it.copy(isLoading = false, favorites = favorites) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }

        override fun toggleFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch {
                viewModelScope.launch {
                    favoritesRepository.toggleFavorite(id = id, isFavorite = isFavorite)
                    _uiState.update {
                        it.copy(
                            favorites =
                                it.favorites.map { product ->
                                    if (product.id == id) {
                                        product.copy(
                                            favorite = !product.favorite,
                                        )
                                    } else {
                                        product
                                    }
                                },
                        )
                    }
                }
            }
        }
    }

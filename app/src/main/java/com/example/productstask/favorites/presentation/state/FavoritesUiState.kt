package com.example.productstask.favorites.presentation.state

import com.example.productstask.productsSearch.data.local.entity.ProductEntity

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<ProductEntity> = emptyList(),
    val error: String? = null,
)

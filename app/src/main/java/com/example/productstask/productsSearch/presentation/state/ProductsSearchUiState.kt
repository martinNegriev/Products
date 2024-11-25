package com.example.productstask.productsSearch.presentation.state

import com.example.productstask.productsSearch.data.local.entity.ProductEntity

data class ProductsSearchUiState(
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

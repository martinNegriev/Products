package com.example.productstask.productsSearch.presentation.state

import com.example.productstask.productsSearch.data.remote.model.Product

data class ProductsSearchUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

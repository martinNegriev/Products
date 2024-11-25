package com.example.productstask.productsSearch.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("products") val products: List<Product>,
)

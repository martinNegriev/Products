package com.example.productstask.productsSearch.data.remote

import com.example.productstask.productsSearch.data.remote.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ProductsSearchServices {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/x-www-form-urlencoded",
    )
    @GET("/products")
    suspend fun getProducts(): Call<List<Product>>
}

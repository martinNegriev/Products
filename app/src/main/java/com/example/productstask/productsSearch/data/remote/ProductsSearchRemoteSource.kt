package com.example.productstask.productsSearch.data.remote

import com.example.productstask.productsSearch.data.remote.model.ProductsResponse
import retrofit2.Response
import javax.inject.Inject

class ProductsSearchRemoteSource
    @Inject
    constructor(
        private val servicesInstance: ServicesInstance,
    ) {
        init {
            servicesInstance.setUpProductsSearch()
        }

        suspend fun getProducts(): Response<ProductsResponse> = servicesInstance.productSearchServices.getProducts()

        suspend fun getProductsByQuery(query: String): Response<ProductsResponse> =
            servicesInstance.productSearchServices.getProductsByQuery(query = query)
    }

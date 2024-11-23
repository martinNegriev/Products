package com.example.productstask.productsSearch.data.remote

import com.example.productstask.productsSearch.data.remote.model.Product
import com.example.productstask.service.ServicesInstance
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ProductsSearchRemoteSource
    @Inject
    constructor(
        private val servicesInstance: ServicesInstance) {
        init {
            servicesInstance.setUpProductsSearch()
        }

        suspend fun getProducts(): Response<List<Product>> {
            return servicesInstance.productSearchServices.getProducts().execute()
        }
    }

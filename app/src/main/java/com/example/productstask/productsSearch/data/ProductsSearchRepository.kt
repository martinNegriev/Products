package com.example.productstask.productsSearch.data

import android.content.Context
import com.example.productstask.R
import com.example.productstask.productsSearch.data.local.ProductsSearchLocalSource
import com.example.productstask.productsSearch.data.remote.ProductsSearchRemoteSource
import com.example.productstask.productsSearch.data.remote.model.ProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsSearchRepository
@Inject
constructor(
    private val productsSearchRemoteSource: ProductsSearchRemoteSource,
    private val productsSearchLocalSource: ProductsSearchLocalSource,
    val context: Context
) {

    suspend fun getProducts(): Response<ProductsResponse> {
        return withContext(Dispatchers.IO) {
            val response = productsSearchRemoteSource.getProducts()
            if (response.isSuccessful) {
                val products = response.body()
                products?.let {
//                    productsSearchLocalSource.saveProducts(it)
                }
            } else {
                throw Exception(context.getString(R.string.products_search_error))
            }
            response
        }
    }

}

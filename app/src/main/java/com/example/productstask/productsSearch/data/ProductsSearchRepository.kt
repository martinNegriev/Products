package com.example.productstask.productsSearch.data

import android.content.Context
import com.example.productstask.R
import com.example.productstask.productsSearch.data.local.ProductsSearchLocalSource
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import com.example.productstask.productsSearch.data.remote.ProductsSearchRemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsSearchRepository
    @Inject
    constructor(
        private val productsSearchRemoteSource: ProductsSearchRemoteSource,
        private val productsSearchLocalSource: ProductsSearchLocalSource,
        private val context: Context,
    ) {
        suspend fun getProducts(refresh: Boolean = false): List<ProductEntity>? =
            withContext(Dispatchers.IO) {
                if (refresh) {
                    val response = productsSearchRemoteSource.getProducts()
                    var productEntities: List<ProductEntity>? = null
                    if (response.isSuccessful) {
                        val products = response.body()
                        products?.let {
                            productEntities =
                                it.products.map { product ->
                                    ProductEntity(
                                        id = product.id,
                                        title = product.title,
                                        description = product.description,
                                        thumbnail = product.thumbnail,
                                        favorite = false,
                                    )
                                }
                            productEntities?.let { entities ->
                                productsSearchLocalSource.saveProducts(entities)
                            }
                        }
                    } else {
                        throw Exception(context.getString(R.string.products_search_error))
                    }
                }
                productsSearchLocalSource.getProducts()
            }
    }

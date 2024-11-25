package com.example.productstask.productsSearch.data.local

import com.example.productstask.productsSearch.data.local.db.AppDatabase
import com.example.productstask.productsSearch.data.local.entity.ProductEntity

class ProductsSearchLocalSource(
    private val appDatabase: AppDatabase,
) {
    fun getProducts(): List<ProductEntity> = appDatabase.productsDao().getProducts()

    fun saveProducts(products: List<ProductEntity>) {
        appDatabase.productsDao().insertProducts(*products.toTypedArray())
    }
}

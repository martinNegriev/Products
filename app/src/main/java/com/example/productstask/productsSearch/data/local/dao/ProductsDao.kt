package com.example.productstask.productsSearch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.productstask.productsSearch.data.local.entity.ProductEntity

@Dao
interface ProductsDao {
    @Query(
        "SELECT pe.id, pe.title, pe.description, pe.thumbnail, fe.favorite " +
            "FROM ProductEntity pe LEFT JOIN FavoriteEntity fe ON pe.id = fe.id",
    )
    fun getProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(vararg products: ProductEntity)

    @Query(
        "SELECT pe.id, pe.title, pe.description, pe.thumbnail, fe.favorite " +
            "FROM ProductEntity pe LEFT JOIN FavoriteEntity fe ON pe.id = fe.id " +
            "WHERE pe.id IN (:ids)",
    )
    fun getProductsById(ids: List<Int>): List<ProductEntity>

    @Query("DELETE FROM ProductEntity")
    fun deleteAllProducts()
}

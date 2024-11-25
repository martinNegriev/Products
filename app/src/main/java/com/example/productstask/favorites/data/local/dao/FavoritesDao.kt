package com.example.productstask.favorites.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.productstask.favorites.data.local.entity.FavoriteEntity
import com.example.productstask.productsSearch.data.local.entity.ProductEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoriteEntity)

    @Query(
        "SELECT pe.id, pe.title, pe.description, pe.thumbnail, fe.favorite " +
            "FROM FavoriteEntity fe LEFT JOIN ProductEntity pe ON fe.id = pe.id " +
            "WHERE fe.favorite = 1",
    )
    fun getFavorites(): List<ProductEntity>
}

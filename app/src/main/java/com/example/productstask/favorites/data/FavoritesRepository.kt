package com.example.productstask.favorites.data

import com.example.productstask.favorites.data.local.entity.FavoriteEntity
import com.example.productstask.productsSearch.data.local.db.AppDatabase
import com.example.productstask.productsSearch.data.local.entity.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesRepository(
    private val appDatabase: AppDatabase,
) {
    suspend fun getFavorites(): List<ProductEntity> =
        withContext(Dispatchers.IO) {
            appDatabase.favoritesDao().getFavorites()
        }

    suspend fun toggleFavorite(
        id: Int,
        isFavorite: Boolean,
    ) {
        withContext(Dispatchers.IO) {
            appDatabase.favoritesDao().insertFavorite(FavoriteEntity(id = id, favorite = isFavorite))
        }
    }
}

package com.example.productstask.productsSearch.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productstask.favorites.data.local.dao.FavoritesDao
import com.example.productstask.favorites.data.local.entity.FavoriteEntity
import com.example.productstask.productsSearch.data.local.dao.ProductsDao
import com.example.productstask.productsSearch.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, FavoriteEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao

    abstract fun favoritesDao(): FavoritesDao
}

package com.example.productstask.favorites.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val favorite: Boolean,
)

package com.example.productstask.productsSearch.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val favorite: Boolean,
)

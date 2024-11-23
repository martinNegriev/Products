package com.example.productstask.productsSearch.di

import android.content.Context
import com.example.productstask.productsSearch.data.ProductsSearchRepository
import com.example.productstask.productsSearch.data.local.ProductsSearchLocalSource
import com.example.productstask.productsSearch.data.remote.ProductsSearchRemoteSource
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import com.example.productstask.service.ServicesInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductsSearchModule {
    @Provides
    fun provideProductsSearchViewModel(
        productsSearchRepository: ProductsSearchRepository,
        @ApplicationContext context: Context,
    ): ProductsSearchViewModel = ProductsSearchViewModel(productsSearchRepository, context)

    @Singleton
    @Provides
    fun provideProductsSearchRepository(
        productsSearchRemoteSource: ProductsSearchRemoteSource,
        productsSearchLocalSource: ProductsSearchLocalSource,
        @ApplicationContext context: Context,
    ): ProductsSearchRepository = ProductsSearchRepository(productsSearchRemoteSource, productsSearchLocalSource, context)

    @Singleton
    @Provides
    fun provideProductsSearchRemoteSource(servicesInstance: ServicesInstance): ProductsSearchRemoteSource =
        ProductsSearchRemoteSource(servicesInstance)

    @Singleton
    @Provides
    fun provideProductsSearchLocalSource(): ProductsSearchLocalSource = ProductsSearchLocalSource()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideServicesInstance(gsonConverterFactory: GsonConverterFactory): ServicesInstance = ServicesInstance(gsonConverterFactory)
}

package com.example.productstask.productsSearch.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.productstask.favorites.data.FavoritesRepository
import com.example.productstask.favorites.presentation.viewmodel.FavoritesViewModel
import com.example.productstask.productsSearch.data.ProductsSearchRepository
import com.example.productstask.productsSearch.data.local.ProductsSearchLocalSource
import com.example.productstask.productsSearch.data.local.db.AppDatabase
import com.example.productstask.productsSearch.data.remote.ProductsSearchRemoteSource
import com.example.productstask.productsSearch.data.remote.ServicesInstance
import com.example.productstask.productsSearch.presentation.viewModel.ProductsSearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        favoritesRepository: FavoritesRepository,
        @ApplicationContext context: Context,
    ): ProductsSearchViewModel = ProductsSearchViewModel(productsSearchRepository, favoritesRepository, context)

    @Provides
    fun provideFavoritesViewModel(favoritesRepository: FavoritesRepository): FavoritesViewModel = FavoritesViewModel(favoritesRepository)

    @Singleton
    @Provides
    fun provideProductsSearchRepository(
        productsSearchRemoteSource: ProductsSearchRemoteSource,
        productsSearchLocalSource: ProductsSearchLocalSource,
        @ApplicationContext context: Context,
    ): ProductsSearchRepository = ProductsSearchRepository(productsSearchRemoteSource, productsSearchLocalSource, context)

    @Singleton
    @Provides
    fun provideFavoritesRepository(appDatabase: AppDatabase): FavoritesRepository = FavoritesRepository(appDatabase)

    @Singleton
    @Provides
    fun provideProductsSearchRemoteSource(servicesInstance: ServicesInstance): ProductsSearchRemoteSource =
        ProductsSearchRemoteSource(servicesInstance)

    @Singleton
    @Provides
    fun provideProductsSearchLocalSource(appDatabase: AppDatabase): ProductsSearchLocalSource = ProductsSearchLocalSource(appDatabase)

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideServicesInstance(gsonConverterFactory: GsonConverterFactory): ServicesInstance = ServicesInstance(gsonConverterFactory)

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ) = Room
        .databaseBuilder(context, AppDatabase::class.java, "app_database")
        .addMigrations(
            MIGRATION_1_2,
    ).build()

    companion object {
        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `FavoriteEntity` " +
                            "(`id` INTEGER NOT NULL, 'favorite' INTEGER NOT NULL, PRIMARY KEY(`id`))")
                }
            }
    }
}

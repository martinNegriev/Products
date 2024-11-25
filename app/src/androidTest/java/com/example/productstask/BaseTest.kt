package com.example.productstask

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.productstask.favorites.data.FavoritesRepository
import com.example.productstask.productsSearch.data.ProductsSearchRepository
import com.example.productstask.productsSearch.data.local.ProductsSearchLocalSource
import com.example.productstask.productsSearch.data.local.db.AppDatabase
import com.example.productstask.productsSearch.data.remote.ProductsSearchRemoteSource
import com.example.productstask.productsSearch.data.remote.ServicesInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import retrofit2.converter.gson.GsonConverterFactory

open class BaseTest {
    lateinit var context: Context
    lateinit var db: AppDatabase
    lateinit var servicesInstance: ServicesInstance
    lateinit var productsSearchRepository: ProductsSearchRepository
    lateinit var favoritesRepository: FavoritesRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        servicesInstance =
            ServicesInstance(gsonConverterFactory = GsonConverterFactory.create())
        productsSearchRepository =
            ProductsSearchRepository(
                productsSearchRemoteSource = ProductsSearchRemoteSource(servicesInstance = servicesInstance),
                productsSearchLocalSource = ProductsSearchLocalSource(appDatabase = db),
                context = context,
            )
        favoritesRepository = FavoritesRepository(appDatabase = db)
    }
}

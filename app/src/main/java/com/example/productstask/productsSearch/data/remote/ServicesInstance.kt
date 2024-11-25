package com.example.productstask.productsSearch.data.remote

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Singleton
class ServicesInstance
    @Inject
    constructor(
        private val gsonConverterFactory: GsonConverterFactory,
    ) {
        lateinit var productSearchServices: ProductsSearchServices
        private lateinit var retrofit: Retrofit

        companion object {
            private const val BASE_URL = "https://dummyjson.com"
        }

        @SuppressLint("TrustAllX509TrustManager")
        fun setUpProductsSearch() {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String,
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String,
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    },
                )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

            retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .client(
                        OkHttpClient()
                            .newBuilder()
                            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                            .readTimeout(10000L, TimeUnit.MILLISECONDS)
                            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                            .hostnameVerifier { _, _ -> true }
                            .build(),
                    ).build()
            productSearchServices = retrofit.create(ProductsSearchServices::class.java)
        }
    }

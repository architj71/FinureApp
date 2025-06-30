package com.example.finure.di

import com.example.finure.data.network.AlphaVantageApi
import com.example.finure.data.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger-Hilt module that provides network-related dependencies.
 * Includes Retrofit client, API interface, and repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.alphavantage.co/"

    /**
     * Provides a Retrofit instance with the base URL and Gson converter.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Provides the AlphaVantage API interface implementation.
     */
    @Provides
    @Singleton
    fun provideAlphaVantageApi(retrofit: Retrofit): AlphaVantageApi =
        retrofit.create(AlphaVantageApi::class.java)

    /**
     * Provides the StockRepository which uses the AlphaVantage API.
     */
    @Provides
    @Singleton
    fun provideStockRepository(api: AlphaVantageApi): StockRepository =
        StockRepository(api)
}

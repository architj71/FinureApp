package com.finure.app.data.di

import com.finure.app.data.network.AlphaVantageApi
import com.finure.app.data.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.alphavantage.co/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAlphaVantageApi(retrofit: Retrofit): AlphaVantageApi =
        retrofit.create(AlphaVantageApi::class.java)

    @Provides
    @Singleton
    fun provideStockRepository(api: AlphaVantageApi): StockRepository =
        StockRepository(api)
}
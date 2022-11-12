package com.vision.andorid.composepaging.di

import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.repository.BeerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    const val BASE_URL = "https://api.punkapi.com/v2/"


    @Provides
    fun provideAPiService(): ApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideBeerRepo(apiService: ApiService):BeerRepo{
        return BeerRepo(apiService)
    }


}














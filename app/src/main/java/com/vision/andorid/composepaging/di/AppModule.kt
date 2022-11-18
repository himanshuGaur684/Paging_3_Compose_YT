package com.vision.andorid.composepaging.di

import android.content.Context
import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.repository.BeerRepo
import com.vision.andorid.composepaging.room.BeerDAO
import com.vision.andorid.composepaging.room.BeerDatabase
import com.vision.andorid.composepaging.room.RemoteKeyDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
    fun provideBeerRepo(
        apiService: ApiService,
        beerDAO: BeerDAO,
        remoteKeyDAO: RemoteKeyDAO
    ): BeerRepo {
        return BeerRepo(apiService, beerDAO = beerDAO, remoteKeyDAO)
    }


    @Provides
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
       return BeerDatabase.getInstance(context)
    }



    @Provides
    fun provideBeerDAO(beerDatabase: BeerDatabase): BeerDAO = beerDatabase.getBeerDao()

    @Provides
    fun provideRemoteKeyDao(beerDatabase: BeerDatabase) :RemoteKeyDAO = beerDatabase.getRemoteKeyDAO()


}














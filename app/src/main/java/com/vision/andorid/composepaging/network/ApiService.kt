package com.vision.andorid.composepaging.network

import com.vision.andorid.composepaging.model.BeerDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // https://api.punkapi.com/v2/beers?page=2&per_page=10

    @GET("beers")
    suspend fun getBeerList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<BeerDTO>

}
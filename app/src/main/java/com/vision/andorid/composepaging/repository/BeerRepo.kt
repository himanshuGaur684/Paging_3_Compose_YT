package com.vision.andorid.composepaging.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.paging.BeerPagingSource
import kotlinx.coroutines.flow.Flow

class BeerRepo(private val apiService: ApiService) {

    fun getBeerStream(): Flow<PagingData<BeerDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10
            ), pagingSourceFactory = {
                BeerPagingSource(apiService)
            }
        ).flow
    }


}
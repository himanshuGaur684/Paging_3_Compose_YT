package com.vision.andorid.composepaging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.paging.BeerRemoteMediator
import com.vision.andorid.composepaging.room.BeerDAO
import com.vision.andorid.composepaging.room.RemoteKeyDAO
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class BeerRepo(
    private val apiService: ApiService,
    private val beerDAO: BeerDAO,
    private val remoteKeyDAO: RemoteKeyDAO
) {

    fun getBeerStream(): Flow<PagingData<BeerDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5
            ),
            remoteMediator =
            BeerRemoteMediator(
                apiService = apiService,
                beerDao = beerDAO,
                remoteKeyDAO = remoteKeyDAO
            ),
            initialKey = 1,
            pagingSourceFactory = {
                beerDAO.getBeerList()//BeerPagingSource(apiService)
            }
        ).flow
    }
}
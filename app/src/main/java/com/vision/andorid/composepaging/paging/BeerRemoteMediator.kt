package com.vision.andorid.composepaging.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.model.RemoteKey
import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.room.BeerDAO
import com.vision.andorid.composepaging.room.RemoteKeyDAO

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val remoteKeyDAO: RemoteKeyDAO,
    private val beerDAO: BeerDAO,
    private val apiService: ApiService
) : RemoteMediator<Int, BeerDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerDTO>
    ): MediatorResult {

        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getClosestPosition(state)
                remoteKey?.next?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.APPEND -> {
                val remoteKey = getLastPosition(state)
                val nextKey = remoteKey?.next ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }

        val response = apiService.getBeerList(loadKey, state.config.pageSize)

        val endOfPagination = response.size < state.config.pageSize

        if (loadType == LoadType.REFRESH) {
            beerDAO.nukeTable()
            remoteKeyDAO.nukeTable()
        }

        val prev = if (loadKey == 1) null else loadKey - 1
        val next = if (endOfPagination) null else loadKey + 1

        response.map {
            remoteKeyDAO.insert(
                RemoteKey(
                    next = next,
                    prev = prev,
                    id = it.id
                )
            )
        }
        beerDAO.insertList(response)
        return MediatorResult.Success(endOfPagination)
    }

    suspend fun getClosestPosition(state: PagingState<Int, BeerDTO>): RemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                remoteKeyDAO.getRemoteKey(it.id)
            }
        }
    }

    suspend fun getLastPosition(state: PagingState<Int, BeerDTO>): RemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteKeyDAO.getRemoteKey(it.id)
        }
    }

}
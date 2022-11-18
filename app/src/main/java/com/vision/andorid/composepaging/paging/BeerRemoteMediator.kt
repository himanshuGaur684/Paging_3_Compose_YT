package com.vision.andorid.composepaging.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.model.BeerRemoteKey
import com.vision.andorid.composepaging.network.ApiService
import com.vision.andorid.composepaging.room.BeerDAO
import com.vision.andorid.composepaging.room.RemoteKeyDAO

const val INITIAL_PAGE = 1;

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val apiService: ApiService,
    private val beerDao: BeerDAO,
    private val remoteKeyDAO: RemoteKeyDAO
) : RemoteMediator<Int, BeerDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerDTO>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getClosestRemoteKey(state)
                    remoteKey?.next?.minus(1) ?: INITIAL_PAGE
                }
                LoadType.APPEND -> {
                    val remoteKey = getLastKey(state)
                    val nextPage = remoteKey?.next
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    nextPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
            }
            Log.d("TAG", "initial log loadKey: ${loadKey} ")
            val remoteResponse =
                apiService.getBeerListMediator(loadKey, perPage = state.config.pageSize)

            if (remoteResponse.isSuccessful) {
                remoteResponse.body()?.let { response ->
                    Log.d("TAG", "load:${loadType} -->  ${response}")
                    val endOfPagination = response.size < state.config.pageSize

                    if (loadType == LoadType.REFRESH) {
                        beerDao.nukeTable()
                        remoteKeyDAO.nukeTable()
                    }

                    val prev = if (loadKey == INITIAL_PAGE) null else loadKey - 1
                    val next = if (endOfPagination) null else loadKey + 1

                    response.map {
                        remoteKeyDAO.insertRemoteKey(
                            BeerRemoteKey(
                                prev = prev,
                                next = next,
                                id = it.id
                            )
                        )
                    }
                    beerDao.insertBeerList(response)
                    MediatorResult.Success(endOfPagination)
                }
                return MediatorResult.Success(endOfPaginationReached = false)
            } else {
                Log.d("TAG", " Error during loading load: ${remoteResponse.errorBody().toString()}")
                MediatorResult.Error(java.lang.Exception(remoteResponse.errorBody().toString()))
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("TAG", "load: ${e.toString()}")

            MediatorResult.Error(e)
        }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, BeerDTO>): BeerRemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                remoteKeyDAO.getRemoteKey(it.id)
            }
        }
    }

    private suspend fun getLastKey(state: PagingState<Int, BeerDTO>): BeerRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteKeyDAO.getRemoteKey(it.id)
        }
    }


}
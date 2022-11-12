package com.vision.andorid.composepaging.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.network.ApiService
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class BeerPagingSource (private val apiService: ApiService): PagingSource<Int, BeerDTO>() {
    override fun getRefreshKey(state: PagingState<Int, BeerDTO>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(
                1
            ) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerDTO> {
        val position = params.key?:1
        return try {
            delay(3000)
            val remoteData = apiService.getBeerList(position,params.loadSize)
            val nextKey = if(remoteData.size<params.loadSize){
                null
            }else{
                position+1
            }
            LoadResult.Page(
                remoteData,
                prevKey = if(position==1) null else position-1,
                nextKey = nextKey
            )
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }

    }
}
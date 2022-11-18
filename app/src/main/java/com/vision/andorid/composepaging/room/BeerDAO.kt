package com.vision.andorid.composepaging.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vision.andorid.composepaging.model.BeerDTO

@Dao
interface BeerDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insertBeerList(beer: List<BeerDTO>)

    @Query("DELETE FROM BeerDTO")
    suspend fun nukeTable()

    @Query("SELECT * FROM BeerDTO")
    fun getBeerList(): PagingSource<Int, BeerDTO>

}
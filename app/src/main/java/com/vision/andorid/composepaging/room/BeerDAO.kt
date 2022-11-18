package com.vision.andorid.composepaging.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vision.andorid.composepaging.model.BeerDTO

@Dao
interface BeerDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list:List<BeerDTO>)

    @Query("DELETE FROM BeerDTO")
    suspend fun nukeTable()

    @Query("SELECT * FROM BeerDTO")
    fun getBeerList():PagingSource<Int,BeerDTO>


}
package com.vision.andorid.composepaging.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vision.andorid.composepaging.model.BeerRemoteKey

@Dao
interface RemoteKeyDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insertRemoteKey(remoteKey: BeerRemoteKey)

    @Query("DELETE FROM BeerRemoteKey")
    suspend fun nukeTable()

    @Query("SELECT * FROM BeerRemoteKey WHERE id==:id")
    suspend fun getRemoteKey(id: Int): BeerRemoteKey

}
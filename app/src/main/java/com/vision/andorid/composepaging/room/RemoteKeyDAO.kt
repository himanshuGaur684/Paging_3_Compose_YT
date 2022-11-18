package com.vision.andorid.composepaging.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vision.andorid.composepaging.model.RemoteKey

@Dao
interface RemoteKeyDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("DELETE FROM RemoteKey")
    suspend fun nukeTable()

    @Query("SELECT * FROM RemoteKey WHERE id==:id")
    suspend fun getRemoteKey(id: Int): RemoteKey

}
package com.vision.andorid.composepaging.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.model.BeerRemoteKey
@Database(entities = [BeerRemoteKey::class,BeerDTO::class], version = 1, exportSchema = false)
abstract class BeerDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): BeerDatabase {
            return Room.databaseBuilder(context, BeerDatabase::class.java, "beer_db").build()
        }
    }

    abstract fun getBeerDao():BeerDAO

    abstract fun getRemoteKeyDAO():RemoteKeyDAO


}
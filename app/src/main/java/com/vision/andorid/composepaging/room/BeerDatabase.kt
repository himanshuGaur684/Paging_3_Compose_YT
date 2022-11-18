package com.vision.andorid.composepaging.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.model.RemoteKey

@Database(entities = [BeerDTO::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class BeerDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): BeerDatabase {
            return Room.databaseBuilder(context, BeerDatabase::class.java, "beer_db").build()
        }
    }

    abstract fun getBeerDAO(): BeerDAO

    abstract fun getRemoteDAO(): RemoteKeyDAO

}
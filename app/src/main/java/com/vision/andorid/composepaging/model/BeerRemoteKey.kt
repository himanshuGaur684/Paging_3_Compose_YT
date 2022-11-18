package com.vision.andorid.composepaging.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeerRemoteKey(
    val prev: Int?,
    val next: Int?,
    @PrimaryKey(autoGenerate = false)
    val id: Int
)

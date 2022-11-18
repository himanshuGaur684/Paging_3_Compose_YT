package com.vision.andorid.composepaging.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    val next: Int?,
    val prev: Int?,
    @PrimaryKey(autoGenerate = false)
    val id: Int
)

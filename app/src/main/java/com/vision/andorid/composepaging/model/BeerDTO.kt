package com.vision.andorid.composepaging.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeerDTO(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val brewers_tips: String,
    val contributed_by: String,
    val description: String,
    val first_brewed: String,
    val image_url: String,
    val name: String,
    val tagline: String,
)
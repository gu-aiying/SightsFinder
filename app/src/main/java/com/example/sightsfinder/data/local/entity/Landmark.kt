package com.example.sightsfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Landmark(

    val imageUrl: String?,
    val description: String?,
    val location: String?,
    val distance: Long,

    @PrimaryKey (autoGenerate = true)
    val id:Int,
)

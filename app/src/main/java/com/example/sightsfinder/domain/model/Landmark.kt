package com.example.sightsfinder.domain.model

data class Landmark(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val distanceMeters: Int
)

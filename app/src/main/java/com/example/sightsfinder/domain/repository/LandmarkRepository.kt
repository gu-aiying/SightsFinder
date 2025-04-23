package com.example.sightsfinder.domain.repository

import com.example.sightsfinder.domain.model.Landmark

interface LandmarkRepository {
    suspend fun getNearbyLandmarks(lat: Double, lon: Double): List<Landmark>
}
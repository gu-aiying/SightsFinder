package com.example.sightsfinder.domain.repository

import com.example.sightsfinder.domain.model.Landmark
import com.example.sightsfinder.domain.model.LandmarkInfo

interface LandmarkRepository {
    suspend fun getNearbyLandmarks(lat: Double, lon: Double): List<Landmark>

    suspend fun getLandmarkInfo(name: String): LandmarkInfo
}
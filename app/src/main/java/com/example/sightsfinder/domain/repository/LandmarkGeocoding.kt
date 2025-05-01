package com.example.sightsfinder.domain.repository

import com.example.sightsfinder.domain.model.GeocodingRequest
import com.example.sightsfinder.domain.model.GeocodingResult
import com.yandex.mapkit.geometry.BoundingBox

interface LandmarkGeocoding {
    suspend fun getLandmarkLocation(request: GeocodingRequest): Result<GeocodingResult>
}
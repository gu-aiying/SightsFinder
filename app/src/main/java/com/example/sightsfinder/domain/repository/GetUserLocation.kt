package com.example.sightsfinder.domain.repository

import com.example.sightsfinder.domain.model.Coordinate
import kotlinx.coroutines.flow.StateFlow

interface GetUserLocation {
    suspend fun updateLocation(coordinate: Coordinate)
    suspend fun getUserLocation(): StateFlow<Coordinate?>
}
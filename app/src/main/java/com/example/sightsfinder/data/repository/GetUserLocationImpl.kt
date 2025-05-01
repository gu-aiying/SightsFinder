package com.example.sightsfinder.data.repository

import com.example.sightsfinder.domain.model.Coordinate
import com.example.sightsfinder.domain.repository.GetUserLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetUserLocationImpl : GetUserLocation {
    private var _currentLocation = MutableStateFlow<Coordinate?>(null)
    private val currentLocation = _currentLocation.asStateFlow()

    override suspend fun updateLocation(coordinate: Coordinate) {
        _currentLocation.value = coordinate
    }

    override suspend fun getUserLocation(): StateFlow<Coordinate?> {
        return currentLocation
    }


}
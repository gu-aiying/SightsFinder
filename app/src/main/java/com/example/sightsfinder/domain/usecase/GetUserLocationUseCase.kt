package com.example.sightsfinder.domain.usecase

import com.example.sightsfinder.domain.model.Coordinate
import com.example.sightsfinder.domain.repository.GetUserLocation
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val getUserLocation: GetUserLocation
) {
    suspend operator fun invoke(): StateFlow<Coordinate?> {
        return getUserLocation.getUserLocation()
    }
}
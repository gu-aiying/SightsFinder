package com.example.sightsfinder.domain.usecase

import com.example.sightsfinder.domain.model.Coordinate
import com.example.sightsfinder.domain.repository.GetUserLocation
import javax.inject.Inject

class UpdateUserLocationUseCase @Inject constructor(
    val getUserLocation: GetUserLocation
) {
    suspend operator fun invoke(coordinate: Coordinate){
        getUserLocation.updateLocation(coordinate)
    }
}
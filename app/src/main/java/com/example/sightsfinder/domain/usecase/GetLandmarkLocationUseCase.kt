package com.example.sightsfinder.domain.usecase

import com.example.sightsfinder.domain.model.GeocodingRequest
import com.example.sightsfinder.domain.model.GeocodingResult
import com.example.sightsfinder.domain.repository.LandmarkGeocoding
import javax.inject.Inject

class GetLandmarkLocationUseCase @Inject constructor(
    private val landmarkGeocoding: LandmarkGeocoding
) {

    suspend operator fun invoke(request: GeocodingRequest): Result<GeocodingResult> {
        return landmarkGeocoding.getLandmarkLocation(request)
    }

}
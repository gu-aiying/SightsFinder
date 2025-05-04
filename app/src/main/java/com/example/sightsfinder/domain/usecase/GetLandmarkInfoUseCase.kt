package com.example.sightsfinder.domain.usecase

import com.example.sightsfinder.domain.model.LandmarkInfo
import com.example.sightsfinder.domain.repository.LandmarkRepository
import javax.inject.Inject

class GetLandmarkInfoUseCase @Inject constructor(
    private val repository: LandmarkRepository
) {
    suspend operator fun invoke(name: String): LandmarkInfo {
        return repository.getLandmarkInfo(name)
    }
}

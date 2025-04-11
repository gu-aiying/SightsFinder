package com.example.sightsfinder.domain.usecase

import com.example.sightsfinder.domain.model.ClassifyResult
import com.example.sightsfinder.domain.model.ClassifyRequest
import com.example.sightsfinder.domain.repository.LandmarkClassifier
import javax.inject.Inject

class ClassifyLandmarkUseCase @Inject constructor(
    private val landmarkClassifier: LandmarkClassifier
){
    suspend operator fun invoke(request: ClassifyRequest): Result<ClassifyResult> {
        return landmarkClassifier.classify(request)
    }
}
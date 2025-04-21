package com.example.sightsfinder.domain.repository

import com.example.sightsfinder.domain.model.ClassifyResult
import com.example.sightsfinder.domain.model.ClassifyRequest

interface LandmarkClassifier {

//    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
    suspend fun classify(request: ClassifyRequest): Result<ClassifyResult>
}
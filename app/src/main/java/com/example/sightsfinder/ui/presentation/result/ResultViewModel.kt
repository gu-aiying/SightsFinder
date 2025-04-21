package com.example.sightsfinder.ui.presentation.result

import androidx.lifecycle.ViewModel
import com.example.sightsfinder.domain.model.ClassifyRequest
import com.example.sightsfinder.domain.model.ClassifyResult
import com.example.sightsfinder.domain.usecase.ClassifyLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    val classifyLandmarkUseCase: ClassifyLandmarkUseCase
): ViewModel() {

    var classifyResult: Result<ClassifyResult>? = null

    suspend fun classify(imageUrl: String) {
        val request = ClassifyRequest(
            imageUrl = imageUrl
        )
        classifyResult = classifyLandmarkUseCase(request)
    }

}
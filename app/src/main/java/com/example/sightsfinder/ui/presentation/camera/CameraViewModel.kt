package com.example.sightsfinder.ui.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import com.example.sightsfinder.domain.model.ClassifyRequest
import com.example.sightsfinder.domain.model.ClassifyResult
import com.example.sightsfinder.domain.usecase.ClassifyLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    val classifyLandmarkUseCase: ClassifyLandmarkUseCase
): ViewModel() {

    var classifyResult: Result<ClassifyResult>? = null

    suspend fun classify(image: Bitmap) {
        val request = ClassifyRequest(
            image = image
        )
        classifyResult = classifyLandmarkUseCase(request)
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
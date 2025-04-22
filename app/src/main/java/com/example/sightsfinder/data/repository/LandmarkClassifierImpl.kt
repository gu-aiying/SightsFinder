package com.example.sightsfinder.data.repository

import android.content.Context
import androidx.core.graphics.scale
import com.example.sightsfinder.domain.model.ClassifyRequest
import com.example.sightsfinder.domain.model.ClassifyResult
import com.example.sightsfinder.domain.repository.LandmarkClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class LandmarkClassifierImpl(
    private val context: Context,
    private val threshold: Float = 0.75f,
    private val maxResult: Int = 1
) : LandmarkClassifier {

    private var classifier: ImageClassifier? = null

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResult)
            .setScoreThreshold(threshold)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "EuropeLandmark.tflite",
                options
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

//    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
//        return when (rotation) {
//            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
//            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
//            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
//            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
//        }
//    }

    // override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
    override suspend fun classify(request: ClassifyRequest): Result<ClassifyResult> {
        if (classifier == null) {
            setupClassifier()
        }
        val imageProcessor = ImageProcessor.Builder().build()

        val bitmap = request.image.scale(321, 321)

        val tensorImage = imageProcessor.process(
            TensorImage.fromBitmap(bitmap)
        )

        val imageProcessingOptions = ImageProcessingOptions.builder()
//            .setOrientation(getOrientationFromRotation(rotation))
            .build()

        val results = classifier?.classify(tensorImage, imageProcessingOptions)

        val resultsList = results?.flatMap { classifications ->
            classifications.categories.map { category ->
                ClassifyResult(
                    name = category.displayName,
                    score = category.score.toString()
                )
            }
        }?.distinctBy { it.name } ?: emptyList()



        if (resultsList.isEmpty()) {
            return  Result.failure(NoSuchElementException("Failed to classify the landmark"))
        } else {
            val classifyResult = ClassifyResult(
                name = resultsList[0].name,
                score = resultsList[0].score
            )
            return Result.success(classifyResult)
        }

    }
}
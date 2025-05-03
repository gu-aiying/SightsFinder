package com.example.sightsfinder.data.repository

import com.example.sightsfinder.domain.model.Landmark
import com.example.sightsfinder.domain.repository.LandmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.location.Location
import androidx.media3.common.util.Log
import com.example.sightsfinder.data.remote.RetrofitInstance
import com.example.sightsfinder.domain.model.LandmarkInfo

class LandmarkRepositoryImpl : LandmarkRepository {

    override suspend fun getNearbyLandmarks(lat: Double, lon: Double): List<Landmark> = withContext(Dispatchers.IO) {
        val query = """
            [out:json];
            node[tourism=attraction](around:3000,$lat,$lon);
            out;
        """.trimIndent()

        val response = RetrofitInstance.api.getNearbyLandmarks(query)

        return@withContext response.elements.mapNotNull { el ->
            val name = el.tags?.get("name") ?: return@mapNotNull null

            val distance = FloatArray(1)
            Location.distanceBetween(lat, lon, el.lat, el.lon, distance)

            val unsplashImage = try {
                RetrofitInstance.unsplashApi.searchPhotos(name).results.firstOrNull()?.urls?.small
            } catch (e: Exception) {
                null
            }

            val wikipediaImage = if (unsplashImage == null) {
                try {
                    val pages = RetrofitInstance.wikipediaApi.getImageForTitle(title = name).query.pages
                    pages.values.firstOrNull()?.thumbnail?.source
                } catch (e: Exception) {
                    null
                }
            } else null

            val imageUrl = unsplashImage ?: wikipediaImage

            Landmark(
                name = name,
                latitude = el.lat,
                longitude = el.lon,
                imageUrl = imageUrl,
                distanceMeters = distance[0].toInt()
            )
        }.sortedBy { it.distanceMeters }
    }

    override suspend fun getLandmarkInfo(name: String): LandmarkInfo = withContext(Dispatchers.IO) {
        val unsplashImage = try {
            RetrofitInstance.unsplashApi.searchPhotos(name).results.firstOrNull()?.urls?.small
        } catch (e: Exception) {
            null
        }

        val wikipediaImage = if (unsplashImage == null) {
            try {
                val pages = RetrofitInstance.wikipediaApi.getImageForTitle(title = name).query.pages
                pages.values.firstOrNull()?.thumbnail?.source
            } catch (e: Exception) {
                null
            }
        } else null

        val image = unsplashImage ?: wikipediaImage

        val description = try {
            RetrofitInstance.wikipediaApi
                .getDescriptionForTitle(title = name)
                .query.pages.values.firstOrNull()?.extract
                ?: "Описание достопримечательности временно недоступно"
        } catch (e: Exception) {
            "Описание достопримечательности временно недоступно"
        }

        return@withContext LandmarkInfo(imageUrl = image, description = description)
    }
}

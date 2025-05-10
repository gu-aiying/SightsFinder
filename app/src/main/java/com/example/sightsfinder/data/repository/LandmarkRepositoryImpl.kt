package com.example.sightsfinder.data.repository

import com.example.sightsfinder.domain.model.Landmark
import com.example.sightsfinder.domain.repository.LandmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.location.Location
import android.util.Log
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

            val wikipediaImage = try {
                val pages = RetrofitInstance.wikipediaApi.getImageForTitle(title = name).query.pages
                pages.values.firstOrNull()?.thumbnail?.source
            } catch (e: Exception) {
                null
            }

            val unsplashImage = if (wikipediaImage == null) {
                try {
                    RetrofitInstance.unsplashApi.searchPhotos(name).results.firstOrNull()?.urls?.small
                } catch (e: Exception) {
                    null
                }
            } else null

            val imageUrl = wikipediaImage ?: unsplashImage

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
        val wikipediaImage = try {
            val pages = RetrofitInstance.wikipediaApi.getImageForTitle(title = name).query.pages
            pages.values.firstOrNull()?.thumbnail?.source
        } catch (e: Exception) {
            null
        }

        val unsplashImage = if (wikipediaImage == null) {
            try {
                RetrofitInstance.unsplashApi.searchPhotos(name).results.firstOrNull()?.urls?.small
            } catch (e: Exception) {
                null
            }
        } else null

        val imageUrl = wikipediaImage ?: unsplashImage

        val normalizedTitle = name.replace(" ", "_")

        val description = try {
            val ruResponse = RetrofitInstance.wikipediaApi.getDescriptionForTitle(title = normalizedTitle)
            val ruExtract = ruResponse.query.pages.values.firstOrNull()?.extract
            if (!ruExtract.isNullOrBlank()) ruExtract else null
        } catch (e: Exception) {
            null
        } ?: try {
            val enResponse = RetrofitInstance.wikipediaApiEn.getDescriptionForTitle(title = normalizedTitle)
            val enExtract = enResponse.query.pages.values.firstOrNull()?.extract
            if (!enExtract.isNullOrBlank()) enExtract else null
        } catch (e: Exception) {
            null
        } ?: "Описание достопримечательности временно недоступно"

        return@withContext LandmarkInfo(imageUrl = imageUrl, description = description)
    }
}

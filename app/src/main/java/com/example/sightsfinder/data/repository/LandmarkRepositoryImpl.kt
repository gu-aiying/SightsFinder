package com.example.sightsfinder.data.repository

import com.example.sightsfinder.domain.model.Landmark
import com.example.sightsfinder.domain.repository.LandmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import android.location.Location

class LandmarkRepositoryImpl : LandmarkRepository {

    override suspend fun getNearbyLandmarks(lat: Double, lon: Double): List<Landmark> = withContext(Dispatchers.IO) {
        val query = """
            [out:json];
            node[tourism=attraction](around:3000,$lat,$lon);
            out;
        """.trimIndent()

        val url = "https://overpass-api.de/api/interpreter?data=${URLEncoder.encode(query, "UTF-8")}"
        val response = URL(url).readText()
        val json = JSONObject(response)
        val elements = json.getJSONArray("elements")

        val result = mutableListOf<Landmark>()

        for (i in 0 until elements.length()) {
            val el = elements.getJSONObject(i)
            val name = el.optJSONObject("tags")?.optString("name") ?: continue
            val latitude = el.getDouble("lat")
            val longitude = el.getDouble("lon")

            val distance = FloatArray(1)
            Location.distanceBetween(lat, lon, latitude, longitude, distance)

            result.add(
                Landmark(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    imageUrl = null,
                    distanceMeters = distance[0].toInt()
                )
            )
        }

        result.sortedBy { it.distanceMeters }
    }
}

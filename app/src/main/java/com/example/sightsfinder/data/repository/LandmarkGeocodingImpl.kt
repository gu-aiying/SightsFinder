package com.example.sightsfinder.data.repository

import android.location.Address
import android.location.Geocoder
import com.example.sightsfinder.domain.model.GeocodingRequest
import com.example.sightsfinder.domain.model.GeocodingResult
import com.example.sightsfinder.domain.repository.LandmarkGeocoding
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LandmarkGeocodingImpl(
    private val europeBoundingBox: BoundingBox = BoundingBox(
        Point(34.5, -25.0),
        Point(71.5, 60.0)
    ),
    private val maxResult: Int = 1
) : LandmarkGeocoding {

    override suspend fun getLandmarkLocation(request: GeocodingRequest): Result<GeocodingResult> {
        val geocoder = Geocoder(request.context, Locale.getDefault())

        return suspendCoroutine { continuation ->
            val listener = object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val landmarkLocation = GeocodingResult(
                            addresses[0].latitude,
                            addresses[0].longitude,
                            addresses[0].getAddressLine(0)
                        )
                        continuation.resume(Result.success(landmarkLocation))
                    } else {
                        continuation.resume(Result.failure(Exception("No results")))
                    }
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume(Result.failure(Exception(errorMessage)))
                }
            }

            geocoder.getFromLocationName(
                request.query, // landmark name
                maxResult, // max result
                europeBoundingBox.southWest.latitude,
                europeBoundingBox.southWest.longitude,
                europeBoundingBox.northEast.latitude,
                europeBoundingBox.northEast.longitude, // set bounding box within which the search is implemented, it is optional
                listener
            )
        }
    }
}
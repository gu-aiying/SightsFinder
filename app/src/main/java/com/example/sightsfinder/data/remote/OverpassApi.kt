package com.example.sightsfinder.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassApi {
    @GET("interpreter")
    suspend fun getNearbyLandmarks(
        @Query("data", encoded = true) query: String
    ): OverpassResponse
}
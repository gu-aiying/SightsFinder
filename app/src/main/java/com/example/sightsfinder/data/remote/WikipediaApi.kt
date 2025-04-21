package com.example.sightsfinder.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WikipediaApi {
    @GET("w/api.php")
    suspend fun getImageForTitle(
        @Query("action") action: String = "query",
        @Query("format") format: String = "json",
        @Query("prop") prop: String = "pageimages",
        @Query("piprop") piprop: String = "thumbnail",
        @Query("pithumbsize") pithumbsize: Int = 300,
        @Query("titles") title: String
    ): WikiImageResponse
}

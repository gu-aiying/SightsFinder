package com.example.sightsfinder.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {
    @Headers("Accept-Version: v1", "Authorization: Client-ID t1SHR2oItc8NPuZrs7GV3wYcywZDIXEXng-skzvpoeU")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1
    ): UnsplashSearchResponse
}
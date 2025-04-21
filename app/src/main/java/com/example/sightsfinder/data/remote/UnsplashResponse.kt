package com.example.sightsfinder.data.remote

data class UnsplashSearchResponse(
    val results: List<UnsplashPhoto>
)

data class UnsplashPhoto(
    val urls: UnsplashPhotoUrls
)

data class UnsplashPhotoUrls(
    val small: String
)

package com.example.sightsfinder.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: OverpassApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OverpassApi::class.java)
    }

    val wikipediaApi: WikipediaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikipediaApi::class.java)
    }

    val wikipediaApiEn: WikipediaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikipediaApi::class.java)
    }

    val unsplashApi: UnsplashApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }
}

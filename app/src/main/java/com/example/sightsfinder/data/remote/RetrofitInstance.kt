package com.example.sightsfinder.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val api: OverpassApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OverpassApi::class.java)
    }

    val wikipediaApi: WikipediaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikipediaApi::class.java)
    }

    val unsplashApi: UnsplashApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }
}

package com.example.sightsfinder.domain.model

import android.content.Context

data class GeocodingRequest (
    val context: Context,
    val query: String
)
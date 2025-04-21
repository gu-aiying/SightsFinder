package com.example.sightsfinder.data.remote

data class OverpassResponse(
    val elements: List<Element>
)

data class Element(
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>?
)

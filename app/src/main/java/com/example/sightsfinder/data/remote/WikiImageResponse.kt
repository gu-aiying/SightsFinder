package com.example.sightsfinder.data.remote

data class WikiImageResponse(
    val query: Query
) {
    data class Query(
        val pages: Map<String, Page>
    )

    data class Page(
        val thumbnail: Thumbnail?
    )

    data class Thumbnail(
        val source: String
    )
}

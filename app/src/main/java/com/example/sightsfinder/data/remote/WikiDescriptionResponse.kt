package com.example.sightsfinder.data.remote

data class WikiDescriptionResponse(
    val query: Query
) {
    data class Query(
        val pages: Map<String, Page>
    )

    data class Page(
        val extract: String?
    )
}

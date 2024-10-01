package com.makashovadev.filmsearcher.data.dto

import com.google.gson.annotations.SerializedName

// Файл, который приходит ответом от API:
data class TmdbResultsDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val tmdbFilms: List<TmdbFilm>,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("total_results")
    val total_results: Int
)
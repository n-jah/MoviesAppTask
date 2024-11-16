package com.example.moviesapptask.model
data class MoviesResponse(
    val page: Int,
    val results: List<Movie>
)

data class Movie(
    val adult: Boolean= false,
    val backdrop_path: String?= null,
    val genre_ids: List<Int> = emptyList(),
    val id: Int = 0,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String ? = null,
    val popularity: Double ?= null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val video: Boolean?= false,
    val vote_average: Double? = null,
    val vote_count: Int?= null
)

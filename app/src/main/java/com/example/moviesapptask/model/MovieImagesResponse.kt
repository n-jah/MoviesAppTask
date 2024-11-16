package com.example.moviesapptask.model
data class MovieImagesResponse(
    val id: Int,
    val backdrops: List<Image>,
    val posters: List<Image>
) {
    data class Image(
        val aspect_ratio: Double,
        val file_path: String, // Ensure this is included
        val height: Int,
        val width: Int,
        val iso_639_1: String?,
        val vote_average: Double,
        val vote_count: Int
    )
}
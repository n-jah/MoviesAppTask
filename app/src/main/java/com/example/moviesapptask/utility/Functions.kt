package com.example.moviesapptask.utility

import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieEntity

// Mapper function to convert Movie (API model) to MovieEntity (Room model)
fun Movie.toEntity(category: String, timestamp: Long): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title?: "",
        overview = this.overview?: "",
        posterPath = this.poster_path?: "",
        releaseDate = this.release_date?: "",
        voteAverage = this.vote_average?:0.0 ,
        category = category,
        timestamp = timestamp
    )
}

// Mapper function to convert MovieEntity (Room model) to Movie (API model)
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.posterPath,
        release_date = this.releaseDate,
        vote_average = this.voteAverage

    )
}

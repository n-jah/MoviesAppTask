package com.example.moviesapptask.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val category: String, // To distinguish between popular, top-rated, etc.
    val timestamp: Long // To track when the movie was cached
)

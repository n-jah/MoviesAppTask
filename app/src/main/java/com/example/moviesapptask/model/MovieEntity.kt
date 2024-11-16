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
    val category: String, // Distinguish categories like popular or top-rated
    val timestamp: Long // To track cache time
)

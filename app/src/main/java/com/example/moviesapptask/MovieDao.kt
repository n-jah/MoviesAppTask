package com.example.moviesapptask

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapptask.model.MovieEntity

@Dao
interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
       fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE category = :category")
     fun getMoviesByCategory(category: String): List<MovieEntity>

    @Query("DELETE FROM movies WHERE timestamp < :validTimestamp")
     fun deleteOldMovies(validTimestamp: Long)
}

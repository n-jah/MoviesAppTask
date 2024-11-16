package com.example.moviesapptask.DataBase

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

    // Search movies by keyword (for the Discover Movie feature)
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :keyword || '%' OR overview LIKE '%' || :keyword || '%'")


    fun searchMoviesByKeyword(keyword: String): List<MovieEntity>
    @Query("DELETE FROM movies WHERE timestamp < :validTimestamp")
     fun deleteOldMovies(validTimestamp: Long)
}

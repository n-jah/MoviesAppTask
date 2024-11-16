package com.example.moviesapptask.repository

import com.example.moviesapptask.MovieDao
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.networking.MoviesApi
import com.example.moviesapptask.utility.toEntity
import com.example.moviesapptask.utility.toMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val movieDao: MovieDao

): MoviesRepository {

    private val CACHE_EXPIRY_TIME = 4 * 60 * 60 * 1000L // 4 hours in milliseconds

    override suspend fun getPopularMovies(): Result<MoviesResponse> {

        // First, check the cache
        val currentTime = System.currentTimeMillis()
        val cachedMovies = withContext(Dispatchers.IO){

            movieDao.getMoviesByCategory("popular")
        }

        if (cachedMovies.isNotEmpty()) {

            val timpstamp = cachedMovies.first().timestamp
            if (currentTime - timpstamp < CACHE_EXPIRY_TIME) {

                val movies = cachedMovies.map { it.toMovie() }
                return Result.Success(MoviesResponse(1, movies))
            }else{
                withContext(Dispatchers.IO) {
                    movieDao.deleteOldMovies(timpstamp)
                }

            }
        }



        return try {
            val response = moviesApi.getPopularMovies()
            if (response.isSuccessful) {
                response.body()?.let {

                    val movies = it.results.map { movie->
                        movie.toEntity("popular", currentTime)
                    }
                    withContext(Dispatchers.IO) {
                        movieDao.insertMovies(movies)
                    }
                    // Return the data as MoviesResponse (map from MovieEntity to ~Movie)
                    val moviesResponse = MoviesResponse(1,movies.map {
                        it.toMovie() }
                    )

                    Result.Success(it)
                } ?: Result.Error("Empty response body")
            } else {
                Result.Error("Error code: ${response.code()}")
            }
        } catch (e: IOException) {
            Result.Error("Couldn't load data: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Unexpected error: ${e.message}")
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val response = moviesApi.getMovieDetails(movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error("Empty response body")
                } else {
                Result.Error("Error code: ${response.code()}")
            }
        } catch (e: IOException) {
            Result.Error("Couldn't load data: ${e.message}")
        }
    }
}

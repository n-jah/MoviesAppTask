package com.example.moviesapptask.repository

import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.networking.MoviesApi
import java.io.IOException

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi
): MoviesRepository {
    override suspend fun getPopularMovies(): Result<MoviesResponse> {
        return try {
            val response = moviesApi.getPopularMovies()
            if (response.isSuccessful) {
                response.body()?.let {
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

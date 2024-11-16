package com.example.moviesapptask.repository

import com.example.moviesapptask.DataBase.MovieDao
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieImagesResponse
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
) : MoviesRepository {

    private val CACHE_EXPIRY_TIME = 4 * 60 * 60 * 1000L // 4 hours in milliseconds

    // Fetch popular movies
    override suspend fun getPopularMovies(): Result<MoviesResponse> {
        return getMoviesFromCacheOrApi("popular")
    }

    // Fetch now playing movies
    override suspend fun getNowPlayingMovies(): Result<MoviesResponse> {
        return getMoviesFromCacheOrApi("Now Playing")
    }

    // Fetch top-rated movies
    override suspend fun getTopRatedMovies(): Result<MoviesResponse> {
        return getMoviesFromCacheOrApi("Top Rated")
    }

    // Fetch upcoming movies
    override suspend fun getUpcomingMovies(): Result<MoviesResponse> {
        return getMoviesFromCacheOrApi("Upcoming")
    }

    // Search movies by keyword
    override suspend fun searchMoviesByKeyword(keyword: String): Result<MoviesResponse> {
        val currentTime = System.currentTimeMillis()
        val cachedMovies = withContext(Dispatchers.IO) {
            movieDao.searchMoviesByKeyword(keyword)
        }

        if (cachedMovies.isNotEmpty()) {
            return Result.Success(MoviesResponse(1, cachedMovies.map { it.toMovie() }))
        }

        return try {
            val response = moviesApi.discoverMoviesByKeyword(keyword)
            if (response.isSuccessful) {
                response.body()?.let {
                    val movies = it.results.map { movie -> movie.toEntity("search", currentTime) }
                    withContext(Dispatchers.IO) {
                        movieDao.insertMovies(movies)
                    }
                    Result.Success(MoviesResponse(1, movies.map { it.toMovie() }))
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

    // Fetch movie details by movie ID
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

    // Helper function to get movies either from cache or API
    private suspend fun getMoviesFromCacheOrApi(category: String): Result<MoviesResponse> {
        val currentTime = System.currentTimeMillis()
        val cachedMovies = withContext(Dispatchers.IO) {
            movieDao.getMoviesByCategory(category)
        }

        if (cachedMovies.isNotEmpty()) {
            val timestamp = cachedMovies.first().timestamp
            // Check if the cache is still valid
            if (currentTime - timestamp < CACHE_EXPIRY_TIME) {
                // Cache is valid, return cached data
                return Result.Success(MoviesResponse(1, cachedMovies.map { it.toMovie() }))
            } else {
                // Cache is expired, delete old movies
                withContext(Dispatchers.IO) {
                    movieDao.deleteOldMovies(timestamp)
                }
            }
        }

        // Fetch data from the API
        return try {
            val response = when (category) {
                "popular" -> moviesApi.getPopularMovies()
                "Now Playing" -> moviesApi.getNowPlayingMovies()
                "Top Rated" -> moviesApi.getTopRatedMovies()
                "Upcoming" -> moviesApi.getUpcomingMovies()
                else -> throw IllegalArgumentException("Unknown category: $category")
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    val movies = it.results.map { movie -> movie.toEntity(category, currentTime) }
                    withContext(Dispatchers.IO) {
                        movieDao.insertMovies(movies)
                    }
                    // Return the data as MoviesResponse
                    return Result.Success(MoviesResponse(1, movies.map { it.toMovie() }))
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

    override suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse> {
        return try {
            val response = moviesApi.getMovieImages(movieId)
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
}

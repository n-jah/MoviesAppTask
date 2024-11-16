package com.example.moviesapptask.repository

import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieImagesResponse
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import retrofit2.Response

interface MoviesRepository {

    suspend fun getPopularMovies(): Result<MoviesResponse>
    suspend fun getNowPlayingMovies(): Result<MoviesResponse>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun searchMoviesByKeyword(keyword: String): Result<MoviesResponse>
    suspend fun getTopRatedMovies(): Result<MoviesResponse>
    suspend fun getUpcomingMovies(): Result<MoviesResponse>
    suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse>



}
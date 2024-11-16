package com.example.moviesapptask.networking

import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieImagesResponse
import com.example.moviesapptask.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<MoviesResponse>
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<MoviesResponse>
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): Response<MoviesResponse>

    @GET("discover/movie")
    suspend fun discoverMoviesByKeyword(
        @Query("with_keywords") keyword: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<MoviesResponse>
    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int  // No api_key required here
    ): Response<MovieImagesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<Movie>

}
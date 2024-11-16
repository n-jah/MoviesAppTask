package com.example.moviesapptask.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieImagesResponse
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.io.IOException

enum class MovieCategory { POPULAR, NOW_PLAYING, TOP_RATED, UPCOMING }

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    // LiveData for holding the movies list for different categories
    private val _movies = MutableLiveData<Result<MoviesResponse>>(Result.Loading())
    val movies: LiveData<Result<MoviesResponse>> = _movies

    // LiveData for holding the movie images
    private val _movieImages = MutableLiveData<Result<MovieImagesResponse>>()
    val movieImages: LiveData<Result<MovieImagesResponse>> = _movieImages

    // LiveData for holding movie details
    private val _movieDetails = MutableLiveData<Result<Movie>>()
    val movieDetails: LiveData<Result<Movie>> = _movieDetails

    // LiveData for holding the search results
    private val _searchMovies = MutableLiveData<Result<MoviesResponse>>()
    val searchMovies: LiveData<Result<MoviesResponse>> = _searchMovies

    // Common method to fetch movies by category and handle errors
    private fun fetchMoviesByCategory(category: MovieCategory) {
        viewModelScope.launch {
            _movies.postValue(Result.Loading())  // Emit Loading state

            try {
                val result = when (category) {
                    MovieCategory.POPULAR -> moviesRepository.getPopularMovies()
                    MovieCategory.NOW_PLAYING -> moviesRepository.getNowPlayingMovies()
                    MovieCategory.TOP_RATED -> moviesRepository.getTopRatedMovies()
                    MovieCategory.UPCOMING -> moviesRepository.getUpcomingMovies()
                }
                _movies.postValue(result)  // Emit result (either success or error)
            } catch (e: IOException) {
                _movies.postValue(Result.Error("Network Error: ${e.message}"))  // Handle network error
            } catch (e: Exception) {
                _movies.postValue(Result.Error("Unexpected Error: ${e.message}"))  // Handle other errors
            }
        }
    }
    // Public methods for each movie category
     fun fetchPopularMovies() {
        fetchMoviesByCategory(MovieCategory.POPULAR)
    }

     fun fetchNowPlayingMovies() {
        fetchMoviesByCategory(MovieCategory.NOW_PLAYING)
    }

     fun fetchTopRatedMovies() {
        fetchMoviesByCategory(MovieCategory.TOP_RATED)
    }

     fun fetchUpcomingMovies() {
        fetchMoviesByCategory(MovieCategory.UPCOMING)
    }



    // Search movies by keyword
    fun searchMoviesByKeyword(keyword: String) {
        viewModelScope.launch {
            _searchMovies.postValue(Result.Loading())  // Emit Loading state
            val result = moviesRepository.searchMoviesByKeyword(keyword)

            when (result) {
                is Result.Success -> {
                    _searchMovies.postValue(result)  // Emit Success state
                }
                is Result.Error -> {
                    _searchMovies.postValue(result)  // Emit Error state
                }
                is Result.Loading -> {
                    _searchMovies.postValue(result)  // Keep Loading state if necessary
                }
            }
        }
    }

    // Fetch movie details by movieId
    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.postValue(Result.Loading())  // Emit Loading state
            val result = moviesRepository.getMovieDetails(movieId)

            when (result) {
                is Result.Success -> {
                    _movieDetails.postValue(result)  // Emit Success state
                }
                is Result.Error -> {
                    _movieDetails.postValue(result)  // Emit Error state
                }
                is Result.Loading -> {
                    _movieDetails.postValue(result)  // Keep Loading state if necessary
                }
            }
        }
    }
    //Fetch movie Images by Id
    fun getMovieImages(movieId: Int){
        viewModelScope.launch {

            val result = moviesRepository.getMovieImages(movieId)
            when (result) {
                is Result.Success -> {
                    Log.d("Imagex", result.data.toString())
                    _movieImages.postValue(result)  // Emit Success state
                }
                is Result.Error -> {
                    Log.d("Imagex", result.message.toString())
                    _movieImages.postValue(result)  // Emit Error state
                }
                is Result.Loading -> {
                    Log.d("Imagex", "Loading")
                    _movieImages.postValue(result)  // Keep Loading state if necessary
                }
            }
        }

    }
}

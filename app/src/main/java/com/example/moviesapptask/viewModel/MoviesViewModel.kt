package com.example.moviesapptask.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.repository.MoviesRepository
import com.example.moviesapptask.repository.MoviesRepositoryImpl
import kotlinx.coroutines.launch
class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val _movies = MutableLiveData<Result<MoviesResponse>>(Result.Loading())
    val movies: LiveData<Result<MoviesResponse>> = _movies

    private val _movieDetails = MutableLiveData<Result<Movie>>()
    val movieDetails: LiveData<Result<Movie>> = _movieDetails

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _movies.postValue(Result.Loading())  // Emit Loading state
            val result = moviesRepository.getPopularMovies()

            when (result) {
                is Result.Success -> {
                    _movies.postValue(result)  // Emit Success state
                }
                is Result.Error -> {
                    _movies.postValue(result)  // Emit Error state
                }
                is Result.Loading -> {
                    // Optional: Keep loading as the state if necessary
                    _movies.postValue(result)
                }
            }
        }
    }

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
                    // Optional: Keep loading as the state if necessary
                    _movieDetails.postValue(result)
                }
            }

        }
    }
}

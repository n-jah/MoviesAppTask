package com.example.moviesapptask.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapptask.R
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MoviesResponse
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.networking.RetrofitInstance
import com.example.moviesapptask.repository.MoviesRepositoryImpl
import com.example.moviesapptask.viewModel.MoviesViewModel
import com.example.moviesapptask.viewModel.MoviesViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val moviesApi = RetrofitInstance.api
        val moviesRepository =
            MoviesRepositoryImpl(moviesApi)  // Make sure to initialize MoviesApi properly
        val viewModelFactory = MoviesViewModelFactory(moviesRepository)

        // Create ViewModel instance using ViewModelFactory
        moviesViewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)

        // Observe LiveData for movie results
        moviesViewModel.movies.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading view
                    showLoading()
                }

                is Result.Success -> {
                    // Show the list of movies
                    showMovies(result.data)
                }

                is Result.Error -> {
                    // Show the error message
                    showError(result.message)
                }
            }
        })


        // Fetch popular movies when activity starts
        moviesViewModel.fetchPopularMovies()
        findViewById<TextView>(R.id.textView).setOnClickListener {
            moviesViewModel.getMovieDetails(912649)
            observeMovieDetails()
        }

    }
    private fun observeMovieDetails() {
        moviesViewModel.movieDetails.observe(this, Observer { result->
            when (result) {
                is Result.Loading -> {
                    // Show loading view
                    showLoading()
                }

                is Result.Error -> {
                    // Show the error message
                    showError(result.message)

                }
                is Result.Success -> {

                    showMovieDetails(result.data)

                }
            }

        })
    }

    private fun showMovieDetails(data: Movie?) {
        Toast.makeText(this, data?.title + " " + data?.overview, Toast.LENGTH_LONG).show()
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

    }


    private fun showLoading() {
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    }

    private fun showMovies(moviesResponse: MoviesResponse?) {
        // Hide the loading spinner
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

        val buffor = StringBuilder()
        moviesResponse?.results?.forEach {

            buffor.append(it.id.toString() + " " + it.title + " \n ")

        }
        buffor.append(moviesResponse?.results?.size)

        Log.d("Movies", buffor.toString())


        findViewById<TextView>(R.id.textView).text = buffor.toString()

    }

    private fun showError(message: String?) {
        // Hide the loading spinner
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

        // Display an error message (Toast, Snackbar, etc.)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

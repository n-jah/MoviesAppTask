package com.example.moviesapptask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapptask.DataBase.MovieDatabase
import com.example.moviesapptask.R
import com.example.moviesapptask.databinding.FragmentSearchBinding
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.networking.RetrofitInstance
import com.example.moviesapptask.repository.MoviesRepositoryImpl
import com.example.moviesapptask.ui.adapter.MovieAdapter
import com.example.moviesapptask.utility.navigateToSearchFragment
import com.example.moviesapptask.viewModel.MoviesViewModel
import com.example.moviesapptask.viewModel.MoviesViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Initialize ViewModel
        initViewModel()
        // Set up RecyclerView
        setupRecyclerView()

        //fouces on search bar
        binding.searchView.requestFocus()


        // Set up SearchView listener to listen for user input
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        searchMovies(it)

                    }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isNotEmpty()) {
                        searchMovies(it)
                    }
                }
                return true
            }
        })

        return binding.root
    }

    private fun initViewModel() {
        val moviesApi = RetrofitInstance.api
        val movieDao = MovieDatabase.getDatabase(requireContext()).movieDao()
        val moviesRepository = MoviesRepositoryImpl(moviesApi, movieDao)
        val viewModelFactory = MoviesViewModelFactory(moviesRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
    }

    private fun setupRecyclerView() {

        movieAdapter = MovieAdapter { movie -> openDetails(movie) }

        binding.recyclerViewSearchResults.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter
        }
    }

    private fun searchMovies(query: String) {
        // Trigger the search API call with the entered query
        viewModel.searchMoviesByKeyword(query)
        // Observe search results
        observeSearchResults()
    }

    private fun observeSearchResults() {
        viewModel.searchMovies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    movieAdapter.submitList(result.data?.results)
                    if (result.data?.results.isNullOrEmpty()) {
                        showNoResultsMessage()

                    }
                    binding.progressBar2.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }

                is Result.Loading -> {
                    // Handle loading state if needed

                    binding.progressBar2.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun showNoResultsMessage() {
        Snackbar.make(binding.root, "No movies found", Snackbar.LENGTH_LONG).show()
    }

    private fun openDetails(movie: Movie) {
        // Navigate to movie details fragment or activity
        val fragment = MovieDetailsFragment().apply {
            arguments = Bundle().apply { putParcelable("movie", movie) }
        }
        navigateToSearchFragment(false, fragment, parentFragmentManager)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

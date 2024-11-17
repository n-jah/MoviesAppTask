package com.example.moviesapptask.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapptask.DataBase.MovieDatabase
import com.example.moviesapptask.R
import com.example.moviesapptask.databinding.FragmentHomeBinding
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.networking.RetrofitInstance
import com.example.moviesapptask.repository.MoviesRepositoryImpl
import com.example.moviesapptask.ui.adapter.MovieAdapter
import com.example.moviesapptask.utility.navigateToSearchFragment
import com.example.moviesapptask.viewModel.MoviesViewModel
import com.example.moviesapptask.viewModel.MoviesViewModelFactory
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize View Binding
        _binding = FragmentHomeBinding.bind(view)

        // Initialize ViewModel
        initViewModel()

        // Set up RecyclerView and Adapter
        setupRecyclerView()

        // Set up Tabs
        setupTabs()

        // Observe LiveData for movies
        observeMovies()

        // Search navigation
        search()

        // Handle tab selection
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = tab?.tag as? String ?: "popular"
                fetchMoviesByCategory(category)


            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Load the first category by default
        if (binding.tabLayout.tabCount > 0) {
            val firstTabCategory = binding.tabLayout.getTabAt(0)?.tag as? String ?: "popular"
            fetchMoviesByCategory(firstTabCategory) // Fetch movies for the first tab
            binding.tabLayout.getTabAt(0)?.select() // Select the first tab
        }
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
        binding.recyclerViewMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter
        }
    }

    private fun setupTabs() {
        val isNightMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resources.configuration.isNightModeActive
        } else {
            // For older versions, assuming light mode as default (or you can provide another fallback)
            false
        }

        val selectedTabIndicatorColor = if (isNightMode) {
            resources.getColor(R.color.white, null)  // Use white for dark mode
        } else {
            resources.getColor(R.color.black, null)  // Use black for light mode
        }

        binding.tabLayout.setSelectedTabIndicatorColor(selectedTabIndicatorColor)

        val selectedTextColor = if (isNightMode) {
            resources.getColor(R.color.white, null)  // Use white for dark mode
        } else {
            resources.getColor(R.color.black, null)  // Use black for light mode
        }

        binding.tabLayout.setTabTextColors( resources.getColor(android.R.color.darker_gray), selectedTextColor)

        val categories = listOf(
            "Popular" to "popular",
            "Top Rated" to "top_rated",
            "Upcoming" to "upcoming",
            "Now Playing" to "now_playing"
        )
        for ((label, tag) in categories) {
            val tab = binding.tabLayout.newTab().setText(label).setTag(tag)
            binding.tabLayout.addTab(tab)
        }
    }

    private fun fetchMoviesByCategory(category: String) {
        when (category) {
            "popular" -> viewModel.fetchPopularMovies()
            "top_rated" -> viewModel.fetchTopRatedMovies()
            "upcoming" -> viewModel.fetchUpcomingMovies()
            "now_playing" -> viewModel.fetchNowPlayingMovies()
            else -> viewModel.fetchPopularMovies()
        }
    }

    private fun observeMovies() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            when (movies) {
                is Result.Success -> {

                    movieAdapter.submitList(movies.data?.results)
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    showError(movies.message)
                }
                is Result.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun openDetails(movie: Movie) {
        val fragment = MovieDetailsFragment().apply {
            arguments = Bundle().apply { putParcelable("movie", movie) }
        }
        navigateToSearchFragment(false, fragment, parentFragmentManager)
        parentFragmentManager.beginTransaction()
       .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(context,"aaaa"+ message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        if (movieAdapter.itemCount == 0){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun search(){

        // Function to navigate with the animation direction
        val searchIcone = binding.iconSearch
        searchIcone.setOnClickListener {
            navigateToSearchFragment(false, SearchFragment(), parentFragmentManager)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

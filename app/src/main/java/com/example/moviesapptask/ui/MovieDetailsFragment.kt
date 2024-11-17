package com.example.moviesapptask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapptask.DataBase.MovieDatabase
import com.example.moviesapptask.R
import com.example.moviesapptask.databinding.FragmentMovieDetailsBinding
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.networking.RetrofitInstance
import com.example.moviesapptask.repository.MoviesRepositoryImpl
import com.example.moviesapptask.ui.adapter.ImagesPagerAdapter
import com.example.moviesapptask.model.Result
import com.example.moviesapptask.viewModel.MoviesViewModel
import com.example.moviesapptask.viewModel.MoviesViewModelFactory
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesViewModel
    private lateinit var imagesAdapter: ImagesPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        // Get the movie data from arguments
        val movie = arguments?.getParcelable<Movie>("movie")

        if (movie == null) {
            Toast.makeText(requireContext(), "Movie data is missing", Toast.LENGTH_SHORT).show()
            return binding.root

        }
        binding.viewPagerImages.visibility = View.GONE
        movie.let {
            if (it.release_date.toString().isBlank()) {
                binding.textViewReleaseDate.visibility = View.GONE
            } else {
                binding.textViewReleaseDate.visibility = View.VISIBLE
                binding.textViewReleaseDate.text = "Release Date: ${movie.release_date.toString()}"
            }
            binding.textViewTitle.text = movie.title
            binding.textViewVote.text = getString(R.string.avragevote, movie.vote_average.toString())
            binding.textViewOverview.text = movie.overview
            val imageUrl = "https://image.tmdb.org/t/p/w500${it.poster_path.toString()}"
            Glide.with(this).load(imageUrl).into(binding.imageViewPoster)
        }
        // Initialize ViewModel
        initViewModel()
        // Initialize ViewPager2 adapter
        imagesAdapter = ImagesPagerAdapter(emptyList())
        binding.viewPagerImages.adapter = imagesAdapter

        // Display movie details and fetch movie images
        fetchMovieImages(movie.id)

        // Handle back press manually
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }



        return binding.root
    }

    private fun initViewModel() {
        val moviesApi = RetrofitInstance.api
        val movieDao = MovieDatabase.getDatabase(requireContext()).movieDao()
        val moviesRepository = MoviesRepositoryImpl(moviesApi, movieDao)
        val viewModelFactory = MoviesViewModelFactory(moviesRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
    }


    private fun fetchMovieImages(movieId: Int) {
        // Fetch and observe images
        viewModel.getMovieImages(movieId)
        viewModel.movieImages.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> handleImageSuccess(result.data?.backdrops?.map { it.file_path })
                is Result.Error -> handleImageError(result.message)
                is Result.Loading -> true
            }
        }
    }

    private fun handleImageSuccess(images: List<String>?) {
        if (!images.isNullOrEmpty()) {
            imagesAdapter.updateImages(images)
            binding.viewPagerImages.visibility = View.VISIBLE
            binding.imageViewPoster.visibility = View.GONE
        } else {
            binding.viewPagerImages.visibility = View.GONE
            binding.imageViewPoster.visibility = View.VISIBLE
        }
    }

    private fun handleImageError(message: String?) {
        Toast.makeText(requireContext(), "Error fetching images: $message", Toast.LENGTH_SHORT).show()
        binding.viewPagerImages.visibility = View.GONE
        binding.imageViewPoster.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

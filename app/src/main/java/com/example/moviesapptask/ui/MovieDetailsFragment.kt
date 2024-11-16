package com.example.moviesapptask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviesapptask.R
import com.example.moviesapptask.databinding.FragmentMovieDetailsBinding
import com.example.moviesapptask.model.Movie

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        // Get movie data passed through arguments

        val movie = arguments?.getParcelable<Movie>("movie")


        // Display movie details
        movie?.let {
            binding.textViewTitle.text = it.title
            binding.textViewVote.text = getString(R.string.avragevote, it.vote_average.toString())
            binding.textViewOverview.text = it.overview

            if (it.release_date.toString().isEmpty()) {
                binding.textViewReleaseDate.visibility = View.GONE
            } else {
                binding.textViewReleaseDate.visibility = View.VISIBLE
                binding.textViewReleaseDate.text = (getString(R.string.release_date )+ it.release_date.toString())
            }

            // Load movie poster image using Glide
            val imageUrl = "https://image.tmdb.org/t/p/w500${it.poster_path.toString()}"
            Glide.with(this).load(imageUrl).into(binding.imageViewPoster)
        }

        // Handle back press manually
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

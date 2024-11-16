package com.example.moviesapptask.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.moviesapptask.R
import com.example.moviesapptask.model.Movie
import com.example.moviesapptask.model.MovieEntity

// Mapper function to convert Movie (API model) to MovieEntity (Room model)
fun Movie.toEntity(category: String, timestamp: Long): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title?: "",
        overview = this.overview?: "",
        posterPath = this.poster_path?: "",
        releaseDate = this.release_date?: "",
        voteAverage = this.vote_average?:0.0 ,
        category = category,
        timestamp = timestamp
    )
}

// Mapper function to convert MovieEntity (Room model) to Movie (API model)
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.posterPath,
        release_date = this.releaseDate,
        vote_average = this.voteAverage

    )
}
fun navigateToSearchFragment(
    isLeftAnimation: Boolean,
    fragment: Fragment,
    parentFragmentManager: FragmentManager
) {
    val transaction = parentFragmentManager.beginTransaction()
    if (!isLeftAnimation) {
        transaction.setCustomAnimations(
            R.anim.fragment_enter,     // Enter from the right
            R.anim.fragment_exit,      // Exit to the left
            R.anim.fragment_pop_enter, // Enter from the left (for back stack)
            R.anim.fragment_pop_exit   // Exit to the right (for back stack)
        )
    } else {
        transaction.setCustomAnimations(
            R.anim.fragment_pop_enter, // Enter from the left
            R.anim.fragment_pop_exit,  // Exit to the right
            R.anim.fragment_enter,     // Enter from the right (for back stack)
            R.anim.fragment_exit       // Exit to the left (for back stack)
        )
    }
    transaction.replace(R.id.fragment_container, fragment)
        .addToBackStack(null)  // Add this transaction to back stack for back navigation
        .commit()  // Commit the transaction
}

package com.ny.moviebrowserapp.presentation

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
}
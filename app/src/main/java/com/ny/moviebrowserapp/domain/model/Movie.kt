package com.ny.moviebrowserapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?, // Full URL to the poster
    val overview: String,
    val releaseDate: String?,
    val voteAverage: Double,
    val backdropUrl: String?, // Full URL to the backdrop
    val isFavorite: Boolean = false // Added for local favorite status
)
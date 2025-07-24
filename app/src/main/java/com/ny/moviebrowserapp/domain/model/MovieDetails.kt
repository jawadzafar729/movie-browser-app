package com.ny.moviebrowserapp.domain.model

import com.google.gson.annotations.SerializedName
import com.ny.moviebrowserapp.data.remote.model.Genre

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val genres: List<Genre>?,
    val runtime: Int?,
    val tagline: String?,
    val status: String?
    // Add more fields as needed from the API response
)
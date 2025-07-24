package com.ny.moviebrowserapp.data.mapper

import com.ny.moviebrowserapp.data.remote.model.MovieDto
import com.ny.moviebrowserapp.domain.model.Movie

// Define a constant for the base image URL (you can get this from TMDB config API, but for simplicity)
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
const val BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w1280/" // Larger for backdrops

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterPath?.let { BASE_IMAGE_URL + it },
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        backdropUrl = backdropPath?.let { BASE_BACKDROP_URL + it }
    )
}
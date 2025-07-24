package com.ny.moviebrowserapp.data.mapper

import com.ny.moviebrowserapp.data.remote.model.MovieDetailsDto
import com.ny.moviebrowserapp.domain.model.MovieDetails

fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        posterPath = posterPath?.let { BASE_IMAGE_URL + it },
        backdropPath = backdropPath?.let { BASE_BACKDROP_URL + it },
        genres = genres,
        runtime = runtime,
        tagline = tagline,
        status = status
    )
}
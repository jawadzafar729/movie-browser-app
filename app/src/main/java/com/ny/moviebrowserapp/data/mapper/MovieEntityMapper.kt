package com.ny.moviebrowserapp.data.mapper

import com.ny.moviebrowserapp.data.local.MovieEntity
import com.ny.moviebrowserapp.domain.model.Movie

// Mappers for Room:
fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterUrl,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        backdropUrl = backdropUrl,
        isFavorite = isFavorite
    )
}
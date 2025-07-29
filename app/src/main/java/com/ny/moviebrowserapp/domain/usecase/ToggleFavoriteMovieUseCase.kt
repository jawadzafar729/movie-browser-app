package com.ny.moviebrowserapp.domain.usecase

import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie, isFavorite: Boolean) {
        repository.toggleFavoriteMovie(movie, isFavorite)
    }
}
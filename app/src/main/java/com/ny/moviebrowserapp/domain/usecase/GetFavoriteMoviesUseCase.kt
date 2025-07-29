package com.ny.moviebrowserapp.domain.usecase

import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(private val repository: MovieRepository) {

    operator fun invoke(): Flow<List<Movie>>{
        return repository.getFavoriteMovies()
    }
}
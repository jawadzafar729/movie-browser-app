package com.ny.moviebrowserapp.domain.usecase

import androidx.paging.PagingData
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(): Flow<PagingData<Movie>>{
        return repository.getPopularMovies()
    }
}
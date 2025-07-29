package com.ny.moviebrowserapp.domain.usecase

import androidx.paging.PagingData
import com.ny.moviebrowserapp.domain.model.MovieDetails
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MovieRepository){

    suspend operator fun invoke(movieId: Int): Flow<MovieDetails?>{
        return repository.getMovieDetails(movieId)
    }
}
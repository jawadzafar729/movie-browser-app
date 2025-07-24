package com.ny.moviebrowserapp.domain.repository

import androidx.paging.PagingData
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): MovieDetails // Or MovieDetails domain model
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavoriteMovie(movie: Movie, isFavorite: Boolean)
}
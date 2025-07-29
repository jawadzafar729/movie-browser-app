package com.ny.moviebrowserapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ny.moviebrowserapp.data.local.MovieDao
import com.ny.moviebrowserapp.data.local.MovieEntity
import com.ny.moviebrowserapp.data.mapper.toDomain
import com.ny.moviebrowserapp.data.mapper.toEntity
import com.ny.moviebrowserapp.data.source.MovieLocalDataSource
import com.ny.moviebrowserapp.data.source.MoviePagingSource
import com.ny.moviebrowserapp.data.source.MovieRemoteDataSource
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.model.MovieDetails
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import com.ny.moviebrowserapp.presentation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,

) : MovieRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource("popular", remoteDataSource, localDataSource) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto -> movieDto.toDomain() }
        }
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource("top_rated", remoteDataSource, localDataSource) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto -> movieDto.toDomain() }
        }
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource("search", remoteDataSource, localDataSource, query) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto -> movieDto.toDomain() }
        }
    }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetails?> {
        // 1. Create a flow for the remote details (one-time fetch)
        val remoteDetailsFlow = flow {
            try {
                val details = remoteDataSource.getMovieDetails(movieId).toDomain()
                emit(details)
            } catch (e: Exception) {
                // Log the error for debugging
                // Log.e("MovieRepositoryImpl", "Error fetching remote details for movie $movieId: ${e.message}", e)
                emit(null) // Emit null or an error wrapper if remote fetch fails
            }
        }

        // 2. Get a flow that observes the favorite status from the local database
        val localMovieEntityFlow: Flow<MovieEntity?> = localDataSource.getMovieById(movieId) // Directly use the Flow from DAO

        // 3. Combine both flows: remote details (one-time) and local favorite status (reactive)
        return combine(remoteDetailsFlow, localMovieEntityFlow) { remoteDetails, localMovieEntity ->
            if (remoteDetails == null) {
                null // If remote fetch failed, no details to show
            } else {
                // Augment the remote details with the local favorite status
                val isFavorite = localMovieEntity?.isFavorite ?: false
                remoteDetails.copy(isFavorite = isFavorite)
            }
        }.distinctUntilChanged()
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }


    override suspend fun toggleFavoriteMovie(movie: Movie, isFavorite: Boolean) {
        val existingMovie = localDataSource.getMovieByIdOnce(movie.id)
        if (existingMovie != null) {
            localDataSource.updateMovie(existingMovie.copy(isFavorite = isFavorite))
        } else {
            localDataSource.saveMovie(movie.toEntity(type = "favorite").copy(isFavorite = isFavorite))
        }
    }
}
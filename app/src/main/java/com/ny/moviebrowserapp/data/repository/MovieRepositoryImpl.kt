package com.ny.moviebrowserapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ny.moviebrowserapp.data.local.MovieDao
import com.ny.moviebrowserapp.data.mapper.toDomain
import com.ny.moviebrowserapp.data.mapper.toEntity
import com.ny.moviebrowserapp.data.source.MovieLocalDataSource
import com.ny.moviebrowserapp.data.source.MoviePagingSource
import com.ny.moviebrowserapp.data.source.MovieRemoteDataSource
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.model.MovieDetails
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,
    private val movieDao: MovieDao // Direct access to DAO for some operations, or abstract into localDataSource more
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

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {

        val remoteDetailsDto = remoteDataSource.getMovieDetails(movieId)
        val remoteMovie = remoteDetailsDto.toDomain() // You'll need a MovieDetailsDto to Movie mapper
        // Optionally, save specific details to local cache here if desired
        return remoteMovie
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }


    override suspend fun toggleFavoriteMovie(movie: Movie, isFavorite: Boolean) {
        val existingMovie = localDataSource.getMovieById(movie.id)
        if (existingMovie != null) {
            localDataSource.updateMovie(existingMovie.copy(isFavorite = isFavorite))
        } else {
            // If it's a favorite and not in cache, save it.
            // You might want to distinguish cached "popular" movies from explicit "favorites"
            // For simplicity now, we'll just save it as a favorite type
            localDataSource.saveMovie(movie.toEntity(type = "favorite").copy(isFavorite = isFavorite))
        }
    }
}
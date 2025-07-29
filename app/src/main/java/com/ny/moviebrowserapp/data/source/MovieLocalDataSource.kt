package com.ny.moviebrowserapp.data.source

import com.ny.moviebrowserapp.data.local.MovieDao
import com.ny.moviebrowserapp.data.local.MovieEntity
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(private val movieDao: MovieDao) {

    suspend fun saveMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(movie)
    }

    fun getMoviesByType(type: String): Flow<List<MovieEntity>> {
        return movieDao.getMoviesByType(type)
    }

    fun getMovieById(movieId: Int): Flow<MovieEntity?> {
        return movieDao.getMovieById(movieId)
    }

    suspend fun getMovieByIdOnce(movieId: Int): MovieEntity? {
        return movieDao.getMovieByIdOnce(movieId)
    }


    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getFavoriteMovies()
    }

    suspend fun deleteMoviesByType(type: String) {
        movieDao.deleteMoviesByType(type)
    }

    suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }
}
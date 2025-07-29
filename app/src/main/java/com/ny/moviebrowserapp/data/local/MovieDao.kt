package com.ny.moviebrowserapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE type = :type ORDER BY voteAverage DESC")
    fun getMoviesByType(type: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): Flow<MovieEntity?>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieByIdOnce(movieId: Int): MovieEntity?

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies WHERE type = :type")
    suspend fun deleteMoviesByType(type: String)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}
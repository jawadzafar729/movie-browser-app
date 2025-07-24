package com.ny.moviebrowserapp.data.source

import com.ny.moviebrowserapp.BuildConfig
import com.ny.moviebrowserapp.data.remote.api.MovieApiService
import com.ny.moviebrowserapp.data.remote.model.MovieDetailsDto
import com.ny.moviebrowserapp.data.remote.model.MovieDto

class MovieRemoteDataSource(private val apiService: MovieApiService) {

    suspend fun getPopularMovies(page: Int): List<MovieDto> {
        return apiService.getPopularMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            page = page
        ).results
    }

    suspend fun getTopRatedMovies(page: Int): List<MovieDto> {
        return apiService.getTopRatedMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            page = page
        ).results
    }

    suspend fun searchMovies(query: String, page: Int): List<MovieDto> {
        return apiService.searchMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            query = query,
            page = page
        ).results
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return apiService.getMovieDetails(
            movieId = movieId,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }
}
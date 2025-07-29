package com.ny.moviebrowserapp.presentation.movie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.model.MovieDetails
import com.ny.moviebrowserapp.domain.usecase.GetMovieDetailsUseCase
import com.ny.moviebrowserapp.domain.usecase.ToggleFavoriteMovieUseCase
import com.ny.moviebrowserapp.presentation.common.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    savedStateHandle: SavedStateHandle // Used to get navigation arguments like movie ID
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<ScreenState<MovieDetails>>(ScreenState.Loading)
    val movieDetails: StateFlow<ScreenState<MovieDetails>> = _movieDetails.asStateFlow()

    private val movieId: Int = savedStateHandle["movieId"] ?: -1 // Get movie ID from navigation args

    init {
        if (movieId != -1) {
            fetchMovieDetails(movieId)
        } else {
            _movieDetails.value = ScreenState.Error("Movie ID not provided.")
        }
    }

    private fun fetchMovieDetails(id: Int) {
        viewModelScope.launch {
            _movieDetails.value = ScreenState.Loading
            try {
                getMovieDetailsUseCase(id).collectLatest{ details ->
                    if (details != null)
                        _movieDetails.value = ScreenState.Success(details)
                    else {
                        _movieDetails.value = ScreenState.Error("Movie details not found or failed to load.")
                    }
                }

            } catch (e: Exception) {
                _movieDetails.value = ScreenState.Error("Failed to load details: ${e.message}")
            }
        }
    }

    fun onToggleFavorite(movie: MovieDetails) {
        viewModelScope.launch {
            val movieForToggle = Movie(
                id = movie.id,
                title = movie.title,
                posterUrl = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                backdropUrl = movie.backdropPath,
                isFavorite = movie.isFavorite // Use the current favorite status from MovieDetails
            )
            toggleFavoriteMovieUseCase(movieForToggle, !movie.isFavorite)
        }
    }
}
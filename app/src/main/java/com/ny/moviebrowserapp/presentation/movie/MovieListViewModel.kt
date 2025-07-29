package com.ny.moviebrowserapp.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ny.moviebrowserapp.domain.model.Movie
import com.ny.moviebrowserapp.domain.usecase.GetPopularMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.GetTopRatedMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.SearchMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.ToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {

    // --- State for Popular Movies ---
    private val _popularMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val popularMovies: StateFlow<PagingData<Movie>> = _popularMovies.asStateFlow()

    // --- State for Top Rated Movies ---
    private val _topRatedMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val topRatedMovies: StateFlow<PagingData<Movie>> = _topRatedMovies.asStateFlow()

    // --- State for Search Query and Results ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: Flow<PagingData<Movie>> = _searchQuery
        .debounce(300L) // Debounce search input to avoid too frequent API calls
        .distinctUntilChanged() // Only emit if the query has actually changed
        .flatMapLatest { query ->
            if (query.isBlank()) {
                // If query is blank, return empty PagingData
                _popularMovies.asStateFlow() // Or PagingData.empty() if you don't want to show anything when search is empty
            } else {
                searchMoviesUseCase(query).cachedIn(viewModelScope)
            }
        }
        .cachedIn(viewModelScope) // Cache results to prevent re-fetching on configuration changes

    init {
        // Fetch popular movies when ViewModel is initialized
        viewModelScope.launch {
            getPopularMoviesUseCase().cachedIn(viewModelScope).collect {
                _popularMovies.value = it
            }
        }

        // Fetch top rated movies when ViewModel is initialized
        viewModelScope.launch {
            getTopRatedMoviesUseCase().cachedIn(viewModelScope).collect {
                _topRatedMovies.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onToggleFavorite(movie: Movie, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movie, isFavorite)
        }
    }
}
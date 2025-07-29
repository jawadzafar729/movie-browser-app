package com.ny.moviebrowserapp.presentation.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ny.moviebrowserapp.R
import com.ny.moviebrowserapp.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Popular", "Top Rated")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Browser") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Search Movies") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            if (searchQuery.isBlank()) {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }

            val moviesToDisplay = if (searchQuery.isBlank()) {
                when (selectedTab) {
                    0 -> popularMovies
                    1 -> topRatedMovies
                    else -> popularMovies
                }
            } else {
                searchResults
            }

            // --- START OF CHANGES IN MovieListScreen ---
            // Use a Box to layer the MovieGrid and the full-screen loading/error/empty states
            Box(modifier = Modifier.fillMaxSize()) {
                MovieGrid(movies = moviesToDisplay, onMovieClick = onMovieClick)

                when {
                    // Full-screen loading when refreshing and no items are loaded yet
                    moviesToDisplay.loadState.refresh is LoadState.Loading && moviesToDisplay.itemCount == 0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        }
                    }
                    // Full-screen error when refreshing and no items are loaded yet
                    moviesToDisplay.loadState.refresh is LoadState.Error && moviesToDisplay.itemCount == 0 -> {
                        val e = moviesToDisplay.loadState.refresh as LoadState.Error
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ErrorMessage(message = e.error.localizedMessage ?: "Failed to load movies.", modifier = Modifier.fillMaxSize())
                        }
                    }
                    // Full-screen "No movies found" message
                    moviesToDisplay.loadState.refresh is LoadState.NotLoading && moviesToDisplay.itemCount == 0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            NoResultsMessage("No movies found.", modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
            // --- END OF CHANGES IN MovieListScreen ---
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieGrid(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies.itemCount) { index ->
            val movie = movies[index]
            if (movie != null) {
                MoviePosterCard(movie = movie, onClick = onMovieClick)
            }
        }

        // Handle only append loading states here
        movies.apply {
            when {

                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { // Keep span for horizontal centering
                        LoadingIndicator(Modifier.fillMaxWidth()) // Appears at the bottom
                    }
                }
                loadState.append is LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { // Keep span for horizontal centering
                        val e = loadState.append as LoadState.Error
                        ErrorMessage(
                            message = e.error.localizedMessage ?: "Unknown error",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviePosterCard(movie: Movie, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(movie.id) }
            .height(300.dp) // Fixed height for consistency
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            movie.posterUrl?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .height(240.dp) // Adjust height for image
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                ) {
                    // Glide options for better loading/error UX
                    it.placeholder(R.drawable.ic_launcher_foreground) // Use a placeholder drawable
                        .error(R.drawable.ic_launcher_foreground) // Use an error drawable
                }
            } ?: run {
                // Placeholder if no poster URL
                Box(
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Poster", textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    // This component is fine as is; the centering problem was in how it was used.
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}

@Composable
fun NoResultsMessage(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, textAlign = TextAlign.Center)
    }
}
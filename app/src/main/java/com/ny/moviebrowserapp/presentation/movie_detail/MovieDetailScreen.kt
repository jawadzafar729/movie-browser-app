package com.ny.moviebrowserapp.presentation.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ny.moviebrowserapp.R
import com.ny.moviebrowserapp.domain.model.MovieDetails
import com.ny.moviebrowserapp.presentation.common.ScreenState
import com.ny.moviebrowserapp.presentation.movie.ErrorMessage
import com.ny.moviebrowserapp.presentation.movie.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit, // Callback to navigate back
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieDetailsState by viewModel.movieDetails.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (movieDetailsState) {
                is ScreenState.Loading -> {
                    LoadingIndicator(Modifier.align(Alignment.Center))
                }
                is ScreenState.Error -> {
                    val message = (movieDetailsState as ScreenState.Error).message
                    ErrorMessage(message = message, modifier = Modifier.align(Alignment.Center))
                }
                is ScreenState.Success -> {
                    val movieDetails = (movieDetailsState as ScreenState.Success).data
                    MovieDetailContent(movieDetails = movieDetails, onToggleFavorite = { viewModel.onToggleFavorite(movieDetails)})
                }
                ScreenState.Empty -> {
                    ErrorMessage(message = "No movie details found.", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailContent(movieDetails: MovieDetails, onToggleFavorite: (MovieDetails) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Backdrop Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            movieDetails.backdropPath?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = movieDetails.title + " backdrop",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                ) {
                    it.placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("No Backdrop", color = Color.White)
            }

            // Poster Image overlayed (example position)
            // You might want to position this more strategically
            movieDetails.posterPath?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = movieDetails.title + " poster",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                        .size(120.dp, 180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                ) {
                    it.placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                }
            }
        }

        // Title and Favorite Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movieDetails.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                movieDetails.tagline?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onToggleFavorite(movieDetails) }) {
                Icon(
                    imageVector = if (movieDetails.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (movieDetails.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Details
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            movieDetails.releaseDate?.let {
                Text(
                    text = "Release Date: $it",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            movieDetails.voteAverage?.let {
                Text(
                    text = "Rating: %.1f/10".format(it),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            movieDetails.runtime?.let {
                Text(
                    text = "Runtime: ${it} minutes",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            movieDetails.genres?.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = "Genres: ${it.joinToString()}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Overview:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movieDetails.overview,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
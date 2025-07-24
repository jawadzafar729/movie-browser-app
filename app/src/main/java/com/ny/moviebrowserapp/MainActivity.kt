package com.ny.moviebrowserapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ny.moviebrowserapp.data.remote.api.MovieApiService
import com.ny.moviebrowserapp.ui.theme.MovieBrowserAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var movieApiService: MovieApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieBrowserAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call your API test function inside a LaunchedEffect for Compose
                    ApiTestComposable(tmdbApiService = movieApiService)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ApiTestComposable(tmdbApiService: MovieApiService) {
    // LaunchedEffect is good for side effects that should run once or when keys change
    LaunchedEffect(Unit) {
        try {
            // Make the API call
            val response = tmdbApiService.getPopularMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                page = 1
            )
            // Log the successful response
            Log.d("API_TEST", "Successfully fetched movies: ${response.results.size} movies")
            response.results.forEach { movie ->
                Log.d("API_TEST", "Movie: ${movie.title} (ID: ${movie.id})")
            }
        } catch (e: Exception) {
            // Log any errors
            Log.e("API_TEST", "Error fetching movies: ${e.message}", e)
        }
    }
}
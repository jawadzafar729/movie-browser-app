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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ny.moviebrowserapp.data.remote.api.MovieApiService
import com.ny.moviebrowserapp.presentation.Screen
import com.ny.moviebrowserapp.presentation.movie.MovieListScreen
import com.ny.moviebrowserapp.presentation.movie_detail.MovieDetailScreen
import com.ny.moviebrowserapp.ui.theme.MovieBrowserAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieBrowserAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.MovieList.route) {
                        composable(Screen.MovieList.route) {
                            MovieListScreen(
                                onMovieClick = { movieId ->
                                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                }
                            )
                        }
                        composable(
                            route = Screen.MovieDetails.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getInt("movieId")
                            if (movieId != null) {
                                MovieDetailScreen(
                                    onBackClick = { navController.popBackStack() }
                                    // ViewModel will get movieId from SavedStateHandle automatically
                                )
                            } else {
                                // Handle error: No movie ID provided, perhaps navigate back
                                LaunchedEffect(Unit) {
                                    Log.e("Navigation", "Movie ID not found for details screen.")
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
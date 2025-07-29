package com.ny.moviebrowserapp.data.source

import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ny.moviebrowserapp.data.local.MovieDao
import com.ny.moviebrowserapp.data.mapper.toDomain
import com.ny.moviebrowserapp.data.mapper.toEntity
import com.ny.moviebrowserapp.data.remote.model.MovieDto
import java.io.IOException

class MoviePagingSource(
    private val contentType: String, // "popular", "top_rated", "search"
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,
    private val searchQuery: String? = null // Only for search type
) : PagingSource<Int, MovieDto>() { // PagingSource<Key, Value>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1 // Start page
        return try {
            val response = when (contentType) {
                "popular" -> remoteDataSource.getPopularMovies(page)
                "top_rated" -> remoteDataSource.getTopRatedMovies(page)
                "search" -> {
                    requireNotNull(searchQuery) { "Search query must not be null for search type." }
                    remoteDataSource.searchMovies(searchQuery, page)
                }
                else -> emptyList() // Should not happen
            }

            // Cache the fetched movies locally
            val movieEntities = response.map { movieDto ->
                val existingMovieEntity = localDataSource.getMovieByIdOnce(movieDto.id) // <--- Get existing DB entity
                // If it exists, use its favorite status; otherwise, it's not a favorite from remote
                val isFavorite = existingMovieEntity?.isFavorite ?: false

                // Convert DTO to Entity, applying the correct favorite status
                movieDto.toDomain().toEntity(
                    type = contentType,
                ).copy(isFavorite = isFavorite) // <--- Crucial: Override isFavorite with the preserved value
            }

            localDataSource.saveMovies(movieEntities)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1 // Assuming API returns empty list when no more pages
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        // Try to find the page number of the closest page to the center of the current view.
        // This is used when the PagingSource needs to be invalidated and reloaded (e.g., due to data refresh)
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
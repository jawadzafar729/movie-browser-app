package com.ny.moviebrowserapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val overview: String,
    val releaseDate: String?,
    val voteAverage: Double,
    val backdropUrl: String?,
    val isFavorite: Boolean, // Store favorite status
    val type: String // E.g., "popular", "top_rated" - to categorize cached data
)

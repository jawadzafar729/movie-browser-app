package com.ny.moviebrowserapp.di

import com.ny.moviebrowserapp.domain.repository.MovieRepository
import com.ny.moviebrowserapp.domain.usecase.GetFavoriteMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.GetMovieDetailsUseCase
import com.ny.moviebrowserapp.domain.usecase.GetPopularMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.GetTopRatedMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.SearchMoviesUseCase
import com.ny.moviebrowserapp.domain.usecase.ToggleFavoriteMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(repository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTopRatedMoviesUseCase(repository: MovieRepository): GetTopRatedMoviesUseCase {
        return GetTopRatedMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(repository: MovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repository: MovieRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteMoviesUseCase(repository: MovieRepository): GetFavoriteMoviesUseCase {
        return GetFavoriteMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteMovieUseCase(repository: MovieRepository): ToggleFavoriteMovieUseCase {
        return ToggleFavoriteMovieUseCase(repository)
    }
}
package com.ny.moviebrowserapp.di

import com.ny.moviebrowserapp.data.local.MovieDao
import com.ny.moviebrowserapp.data.remote.api.MovieApiService
import com.ny.moviebrowserapp.data.repository.MovieRepositoryImpl
import com.ny.moviebrowserapp.data.source.MovieLocalDataSource
import com.ny.moviebrowserapp.data.source.MovieRemoteDataSource
import com.ny.moviebrowserapp.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(apiService: MovieApiService): MovieRemoteDataSource {
        return MovieRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieDao: MovieDao): MovieLocalDataSource {
        return MovieLocalDataSource(movieDao)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(remoteDataSource, localDataSource)
    }
}
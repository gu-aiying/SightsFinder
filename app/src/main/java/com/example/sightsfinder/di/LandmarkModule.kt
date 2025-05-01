package com.example.sightsfinder.di

import com.example.sightsfinder.data.repository.LandmarkRepositoryImpl
import com.example.sightsfinder.domain.repository.LandmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LandmarkModule {

    @Provides
    @Singleton
    fun provideLandmarkRepository(): LandmarkRepository {
        return LandmarkRepositoryImpl()
    }

}

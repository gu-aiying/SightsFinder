package com.example.sightsfinder.di

import android.content.Context
import com.example.sightsfinder.data.repository.GetUserLocationImpl
import com.example.sightsfinder.data.repository.LandmarkClassifierImpl
import com.example.sightsfinder.data.repository.LandmarkGeocodingImpl
import com.example.sightsfinder.domain.repository.GetUserLocation
import com.example.sightsfinder.domain.repository.LandmarkClassifier
import com.example.sightsfinder.domain.repository.LandmarkGeocoding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLandmarkClassifier(@ApplicationContext context: Context) : LandmarkClassifier {
            return LandmarkClassifierImpl(context)
    }


    @Provides
    @Singleton
    fun provideLandmarkGeocoding(): LandmarkGeocoding {
        return LandmarkGeocodingImpl()
    }

    @Provides
    @Singleton
    fun provideGetUserLocation() : GetUserLocation {
        return GetUserLocationImpl()
    }
}
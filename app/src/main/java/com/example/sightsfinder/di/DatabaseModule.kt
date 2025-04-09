package com.example.sightsfinder.di

import android.content.Context
import androidx.room.Room
import com.example.sightsfinder.data.local.AppDatabase
import com.example.sightsfinder.data.local.dao.LandmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDatabase.db"
        ).fallbackToDestructiveMigration().build() //fallbackToDestructiveMigration() - When the database version is upgraded but no correct migration solution is provided, it will directly delete the old database and recreate the new table instead of crashing the application
    }

    @Provides
    @Singleton
    fun provideLandmarkDao(appDatabase: AppDatabase): LandmarkDao {
        return appDatabase.getLandmarkDao()
    }

}
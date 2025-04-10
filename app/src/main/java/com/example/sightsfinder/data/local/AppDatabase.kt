package com.example.sightsfinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sightsfinder.data.local.dao.LandmarkDao
import com.example.sightsfinder.data.local.entity.Landmark

@Database(
    entities = [Landmark::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getLandmarkDao(): LandmarkDao
}
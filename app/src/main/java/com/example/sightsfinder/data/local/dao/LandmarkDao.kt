package com.example.sightsfinder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sightsfinder.data.local.entity.Landmark
import kotlinx.coroutines.flow.Flow

@Dao
interface LandmarkDao {

    @Upsert
    suspend fun upsertLandmark(landmark: Landmark)

    @Delete
    suspend fun deleteLandmark(landmark: Landmark)

    @Query("SELECT * FROM landmark ORDER BY id ASC")
    fun getLandmarksOrderedById(): Flow<List<Landmark>> // Flow itself is an asynchronous flow, so there is no need to mark it with suspend.

    @Query("SELECT * FROM landmark ORDER BY distance ASC")
    fun getLandmarksOrderedByDistance(): Flow<List<Landmark>> // When a @Query method returns a Flow, Room will automatically re-trigger the query and emit new data when the database data changes, without the need for manual monitoring or polling.

}
package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskdego.data.entity.mLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface mLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: mLocationEntity)

    @Query("SELECT * FROM m_locations ORDER BY location_id")
    fun getAllLocations(): Flow<List<mLocationEntity>>
}
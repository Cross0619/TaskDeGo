package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskdego.data.entity.mLocationSpawnEntity

@Dao
interface mLocationSpawnDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpawn(spawn: mLocationSpawnEntity)

    @Query("SELECT * FROM m_location_spawns WHERE location_id = :locationId")
    suspend fun getSpawnsByLocation(locationId: Int): List<mLocationSpawnEntity>
}
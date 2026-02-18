package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "m_location_spawns")
data class mLocationSpawnEntity(
    @PrimaryKey(autoGenerate = true) val spawn_id: Int = 0,
    val location_id: Int,
    val pokemon_id: Int,
    val pokemon_name: String,
    val spawn_rate: Int
)
package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "m_locations")
data class mLocationEntity(
    @PrimaryKey val location_id: Int,
    val location_name: String,
    val unlock_level: Int
)
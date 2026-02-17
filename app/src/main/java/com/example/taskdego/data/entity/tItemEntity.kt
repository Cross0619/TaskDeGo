package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_items")
data class tItemEntity(
    @PrimaryKey(autoGenerate = true) val t_item_id: Int = 0,
    val m_item_id: Int,
    val quantity: Int = 0
)
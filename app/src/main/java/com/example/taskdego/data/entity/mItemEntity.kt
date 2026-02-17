package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "m_items")
data class mItemEntity(
    @PrimaryKey val m_item_id: Int,
    val item_name: String,
    val price: Int? = null,
    val is_pokemon_candy: Boolean = false
)
package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "t_tasks") // [cite: 39]
data class tTaskEntity(
    @PrimaryKey(autoGenerate = true) val t_task_id: Int = 0, // [cite: 41]
    val task_name: String, // [cite: 42]
    val type: Int, // 1: 通常, 2: 日課 [cite: 43]
    val is_completed: Boolean = false, // [cite: 44]
    val created_at: String // [cite: 45]
)
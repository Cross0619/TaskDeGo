package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_trainer_profile")
data class tTrainerProfileEntity (
    @PrimaryKey val t_trainer_id: Int = 1,
    val trainer_name: String = "くろちゃん",
    val level: Int = 1,
    val exp: Int = 0,
    val coins: Int = 0,
    val daily_reword_count_task: Int = 0,
    val daily_reword_count_routine: Int = 0
)
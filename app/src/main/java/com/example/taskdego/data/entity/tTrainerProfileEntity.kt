package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_trainer_profile")
data class tTrainerProfileEntity (
    @PrimaryKey val t_trainer_id: Int = 1,
    var trainer_name: String = "くろちゃん",
    var level: Int = 1,
    var exp: Int = 0,
    var coins: Int = 0,
    var daily_reword_count_task: Int = 0,
    var daily_reword_count_routine: Int = 0
)
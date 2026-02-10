package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.taskdego.data.entity.tTrainerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface tTrainerProfileDao {
    @Query("SELECT * FROM t_trainer_profile WHERE t_trainer_id = 1")
    fun getTrainer(): Flow<tTrainerProfileEntity?>

    @Upsert
    suspend fun updateTrainer(trainer: tTrainerProfileEntity)
}
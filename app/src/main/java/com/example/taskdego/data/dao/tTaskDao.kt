package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskdego.data.entity.tTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface tTaskDao {
    //タスク作成
    @Insert
    suspend fun insertTask(task: tTaskEntity)

    @Query("SELECT * FROM t_tasks ORDER BY t_task_id DESC")
    fun getAllTasks(): Flow<List<tTaskEntity>> // WHERE句を外す

    @Query("Select * FROM t_tasks WHERE is_completed = 0")
    fun getActiveTasks(): Flow<List<tTaskEntity>>

    @Update
    suspend fun updateTask(task: tTaskEntity)
}
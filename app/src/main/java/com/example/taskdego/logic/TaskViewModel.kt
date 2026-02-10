package com.example.taskdego.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskdego.data.dao.tTaskDao
import com.example.taskdego.data.dao.tTrainerProfileDao
import com.example.taskdego.data.entity.tTaskEntity
import com.example.taskdego.data.entity.tTrainerProfileEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val tTaskDao: tTaskDao, private val tTrainerProfileDao: tTrainerProfileDao): ViewModel() {
    val tasks = tTaskDao.getAllTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//    val trainer = tTrainerProfileDao.getTrainer().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
//        tTrainerProfileEntity())

    // ★ 初期値のIDを1に設定します
    val trainer = tTrainerProfileDao.getTrainer().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        tTrainerProfileEntity(t_trainer_id = 1, trainer_name = "たろう", coins = 0, exp = 0)
    )

    fun addTask(name: String, type: Int){
        viewModelScope.launch{
            tTaskDao.insertTask(tTaskEntity(task_name = name, type = type, created_at = "2026-02-09"))
        }
    }

    fun completeTask(task: tTaskEntity){
        viewModelScope.launch{
//          task.is_completed = true
            val updatedTask = task.copy(is_completed = true)
            tTaskDao.updateTask(updatedTask)


            val currentTrainer = trainer.value ?: return@launch

            val trainerWithCorrectId = currentTrainer.copy(t_trainer_id = 1)

            //報酬判定
            // type0はtask type1はroutine
            val count = if (task. type == 0) currentTrainer.daily_reword_count_task else currentTrainer.daily_reword_count_routine

//            if (count < 10){
// ★ .copyを使って「新しい状態」のトレーナーを作ります
                val rewardedTrainer = trainerWithCorrectId.copy(
                    t_trainer_id = 1, // IDを確実に1にする
                    coins = trainerWithCorrectId.coins + 100,
                    exp = trainerWithCorrectId.exp + 100,
                    daily_reword_count_task = if (task.type == 0) trainerWithCorrectId.daily_reword_count_task + 1 else trainerWithCorrectId.daily_reword_count_task,
                    daily_reword_count_routine = if (task.type == 0) trainerWithCorrectId.daily_reword_count_routine else trainerWithCorrectId.daily_reword_count_routine + 1
                )

                // 報酬付与
                // ボールの付与(アイテム管理は別途)
//                currentTrainer.coins += 100
//                currentTrainer.exp += 100
//                if (task.type == 0) currentTrainer.daily_reword_count_task++ else currentTrainer.daily_reword_count_routine++

                tTrainerProfileDao.updateTrainer(currentTrainer)
//            }

        }
    }
}
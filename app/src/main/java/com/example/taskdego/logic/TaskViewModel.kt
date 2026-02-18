package com.example.taskdego.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskdego.data.dao.*
import com.example.taskdego.data.entity.tTaskEntity
import com.example.taskdego.data.entity.tTrainerProfileEntity
import com.example.taskdego.data.entity.mItemEntity
import com.example.taskdego.data.entity.mLocationSpawnEntity
import com.example.taskdego.data.entity.tItemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val tTaskDao: tTaskDao,
                    private val tTrainerProfileDao: tTrainerProfileDao,
                    private val mItemDao: mItemDao,
                    private val tItemDao: tItemDao,
                    private val mLocationDao: mLocationDao,             // ★ 追加
                    private val mLocationSpawnDao: mLocationSpawnDao,    // ★ 追加
                    private val mPokemonDao: mPokemonDao
    ): ViewModel() {
    val tasks = tTaskDao.getAllTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//    val trainer = tTrainerProfileDao.getTrainer().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
//        tTrainerProfileEntity())

    // ★ 完了前のタスクのみを取得
    val activeTasks = tTaskDao.getActiveTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trainer = tTrainerProfileDao.getTrainer().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        tTrainerProfileEntity(t_trainer_id = 1, trainer_name = "たろう", coins = 0, exp = 0)
    )


    // ★ 全ポケモン一覧
    val allPokemons = mPokemonDao.getAllPokemons().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // ★ アイテム一覧を取得
    val items = tItemDao.getAllItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // ★ 修正：ItemWithQuantityのリストを返す
    val itemsWithQuantity = tItemDao.getAllItemsWithQuantity().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // ★ 報酬モーダル表示用の状態
    private val _showRewardDialog = MutableStateFlow(false)
    val showRewardDialog: StateFlow<Boolean> = _showRewardDialog.asStateFlow()

    private val _rewardData = MutableStateFlow<RewardData?>(null)
    val rewardData: StateFlow<RewardData?> = _rewardData.asStateFlow()

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

//            val trainerWithCorrectId = currentTrainer.copy(t_trainer_id = 1)

            //報酬判定
            // type0はtask type1はroutine
            val count = if (task. type == 0) currentTrainer.daily_reword_count_task else currentTrainer.daily_reword_count_routine

            if (count < 10){
                // 報酬の定義
                val expReward = 100
                val coinReward = 100
                val itemId = 1 // モンスターボール
                val itemQuantity = 1

                // ★ .copyを使って「新しい状態」のトレーナーを作ります
                val rewardedTrainer = currentTrainer.copy(
//                    t_trainer_id = 1, // IDを確実に1にする
                    coins = currentTrainer.coins + 100,
                    exp = currentTrainer.exp + 100,
                    daily_reword_count_task = if (task.type == 0) currentTrainer.daily_reword_count_task + 1 else currentTrainer.daily_reword_count_task,
                    daily_reword_count_routine = if (task.type == 0) currentTrainer.daily_reword_count_routine else currentTrainer.daily_reword_count_routine + 1
                )

                tTrainerProfileDao.updateTrainer(rewardedTrainer)

                // ★ アイテム報酬（モンスターボール1個）
                addItem(m_item_id = 1, quantity = 1)

                // ★ アイテム名を取得
                val itemName = mItemDao.getItemById(itemId)?.item_name ?: "アイテム"

                // ★ 報酬データを設定してモーダルを表示
                _rewardData.value = RewardData(
                    exp = expReward,
                    coins = coinReward,
                    itemName = itemName,
                    itemQuantity = itemQuantity
                )
                _showRewardDialog.value = true
            }
        }
    }

    // ★ モーダルを閉じる
    fun dismissRewardDialog() {
        _showRewardDialog.value = false
        _rewardData.value = null
    }

    // ★ アイテム追加メソッド
    suspend fun addItem(m_item_id: Int, quantity: Int) {
        val existingItem = tItemDao.getItemByMasterId(m_item_id)
        if (existingItem != null) {
            // 既に持っている場合は個数を増やす
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            tItemDao.updateItem(updatedItem)
        } else {
            // 新規追加
            tItemDao.insertItem(tItemEntity(m_item_id = m_item_id, quantity = quantity))
        }
    }

    // ★ アイテム名と個数を取得するヘルパー
    suspend fun getItemWithName(tItem: tItemEntity): Pair<String, Int> {
        val masterItem = mItemDao.getItemById(tItem.m_item_id)
        return Pair(masterItem?.item_name ?: "不明なアイテム", tItem.quantity)
    }

    // ★ エリア一覧
    val locations = mLocationDao.getAllLocations().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // ★ エンカウントしたポケモン
    private val _encounteredPokemon = MutableStateFlow<mLocationSpawnEntity?>(null)
    val encounteredPokemon: StateFlow<mLocationSpawnEntity?> = _encounteredPokemon.asStateFlow()

    // ★ エリアに入ってポケモンを抽選する
    fun encounterPokemon(locationId: Int) {
        viewModelScope.launch {
            val spawns = mLocationSpawnDao.getSpawnsByLocation(locationId)
            if (spawns.isEmpty()) return@launch

            // 確率抽選
            val totalRate = spawns.sumOf { it.spawn_rate }
            val random = (1..totalRate).random()
            var cumulative = 0
            var result = spawns.last()

            for (spawn in spawns) {
                cumulative += spawn.spawn_rate
                if (random <= cumulative) {
                    result = spawn
                    break
                }
            }
            _encounteredPokemon.value = result
        }
    }

    // ★ エンカウント状態をリセット
    fun resetEncounter() {
        _encounteredPokemon.value = null
    }
}
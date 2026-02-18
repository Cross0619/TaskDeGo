package com.example.taskdego // ★ご自身のプロジェクトのパッケージ名に書き換えてください

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskdego.data.AppDatabase
import com.example.taskdego.ui.*
import com.example.taskdego.logic.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. データベースの初期化
        // contextとして「this」を渡すことで、アプリの身分証明書をRoomに伝えます
        val database = AppDatabase.getDatabase(this)
        val tTaskDao = database.tTaskDao()
        val tTrainerProfileDao = database.tTrainerProfileDao()
        val mItemDao = database.mItemDao()  // ★ 追加
        val tItemDao = database.tItemDao()  // ★ 追加
        val mLocationDao  = database.mLocationDao()      // ★ 追加
        val mLocationSpawnDao = database.mLocationSpawnDao()   // ★ 追加
        val mPokemonDao = database.mPokemonDao()  // ★ 追加

        // 2. ViewModelの初期化
        // Factoryを使って、ViewModelにDao（データベースの窓口）を渡します
        val viewModel: TaskViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(
                        tTaskDao,
                        tTrainerProfileDao,
                        mItemDao,
                        tItemDao,
                        mLocationDao,
                        mLocationSpawnDao,
                        mPokemonDao) as T
                }
            }
        }

        // 3. 画面の表示
        setContent {
            // あなたのアプリのテーマを適用します
            MaterialTheme {
                // 画面全体を背景色で塗りつぶします
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ついに完成したTaskScreenを呼び出します！
//                    TaskScreen(viewModel = viewModel)
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    fun AppNavigation(viewModel: TaskViewModel) {
        // 現在の画面を管理する状態
        var currentScreen by remember { mutableStateOf("home") }

        when (currentScreen) {
            "home" -> HomeScreen(
                viewModel = viewModel,
                onNavigateToTask = { currentScreen = "task" },
                onNavigateToAdventure = { currentScreen = "adventure" },
                onNavigateToItem = { currentScreen = "item" },  // ★ 追加

                onNavigateToPokedex = { currentScreen = "pokedex" }  // ★ 追加
            )

            "task" -> TaskScreen(
                viewModel = viewModel,
                onNavigateBack = { currentScreen = "home" }
            )
            "adventure" -> AdventureScreen(  // ★ 追加
                viewModel = viewModel,
                onNavigateBack = { currentScreen = "home" }
            )
            "pokedex" -> PokedexScreen(  // ★ 追加
                viewModel = viewModel,
                onNavigateBack = { currentScreen = "home" }
            )
            "item" -> ItemScreen(  // ★ 追加
                viewModel = viewModel,
                onNavigateBack = { currentScreen = "home" }
            )
        }
    }
}
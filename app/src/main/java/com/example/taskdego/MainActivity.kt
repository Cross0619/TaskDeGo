package com.example.taskdego // ★ご自身のプロジェクトのパッケージ名に書き換えてください

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskdego.data.AppDatabase
import com.example.taskdego.ui.TaskScreen
import com.example.taskdego.logic.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. データベースの初期化
        // contextとして「this」を渡すことで、アプリの身分証明書をRoomに伝えます
        val database = AppDatabase.getDatabase(this)
        val tTaskDao = database.tTaskDao()
        val tTrainerProfileDao = database.tTrainerProfileDao()

        // 2. ViewModelの初期化
        // Factoryを使って、ViewModelにDao（データベースの窓口）を渡します
        val viewModel: TaskViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(tTaskDao, tTrainerProfileDao) as T
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
                    TaskScreen(viewModel = viewModel)
                }
            }
        }
    }
}
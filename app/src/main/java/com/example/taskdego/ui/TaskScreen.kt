package com.example.taskdego.ui // ★ご自身のプロジェクトのパッケージ名に変更してください

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskdego.data.entity.tTaskEntity     // ★Taskクラスの正しいフォルダ名に合わせてください
import com.example.taskdego.data.entity.tTrainerProfileEntity  // ★Trainerクラスの正しいフォルダ名に合わせてください
import com.example.taskdego.logic.TaskViewModel // ★ViewModelの正しいフォルダ名に合わせてください

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    // ViewModelからデータを購読します [cite: 58-60]
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val trainer by viewModel.trainer.collectAsStateWithLifecycle()

    // 画面内だけで使う入力状態
    var newTaskName by remember { mutableStateOf("") }
    var isRoutine by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("タスクでGO！", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // --- トレーナーステータス表示 [cite: 10, 59-60] ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Lv.${trainer?.level ?: 1} ${trainer?.trainer_name ?: "トレーナー"}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "所持コイン: ${trainer?.coins ?: 0} / XP: ${trainer?.exp ?: 0}")
                    Text(
                        text = "今日の報酬枠: 通常 ${trainer?.daily_reword_count_task ?: 0}/10  日課 ${trainer?.daily_reword_count_routine ?: 0}/10",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 新規タスク入力エリア [cite: 14] ---
            Text(text = "新しいタスクを追加", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("タスク名を入力（例：散歩、読書）") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Checkbox(checked = isRoutine, onCheckedChange = { isRoutine = it })
                Text("日課として登録")
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (newTaskName.isNotBlank()) {
                            viewModel.addTask(newTaskName, if (isRoutine) 2 else 1)
                            newTaskName = "" // 入力欄をリセット
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("追加")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // --- タスク一覧リスト [cite: 15, 44] ---
            Text(text = "冒険のタスク一覧", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

//            LazyColumn(modifier = Modifier.weight(1f)) {
//                items(tasks) { task ->
//                    TaskRow(
//                        task = task,
//                        onComplete = { viewModel.completeTask(task) }
//                    )
//                }
//            }
            LazyColumn {
                items(
                    items = tasks,
                    // ★ 各タスクの「ID」をキーとして教えることで、再描画を確実にトリガーします
                    key = { it.t_task_id }
                ) { task ->
                    TaskRow(
                        task = task,
                        onComplete = { viewModel.completeTask(task) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: tTaskEntity, onComplete: () -> Unit) {
    // 一時的なテスト用：チェック状態をメモリに持たせる
    var checkedState by remember { mutableStateOf(task.is_completed) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 完了済みならチェックを入れ、押せなくする
        Checkbox(
            checked = task.is_completed,
            onCheckedChange = { isChecked ->
                // チェックが入った時だけ完了処理を呼ぶ
                if (isChecked && !task.is_completed) {
                    onComplete()
                }
            },
//            checked = checkedState, // task.is_completed ではなくこちらを使う
//            onCheckedChange = { isChecked ->
//                checkedState = isChecked // これでタップした瞬間にチェックが付くはず
//                if (isChecked) onComplete()
//            },
            enabled = !task.is_completed // 報酬の二重取り防止
        )

        val textStyle = if (task.is_completed) {
            TextStyle(
                textDecoration = TextDecoration.LineThrough,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            TextStyle(color = MaterialTheme.colorScheme.onSurface)
        }

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = task.task_name, style = textStyle, fontSize = 16.sp)
            Text(
                text = if (task.type == 2) "日課" else "通常",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
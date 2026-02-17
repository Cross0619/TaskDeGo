package com.example.taskdego.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskdego.data.entity.tItemEntity
import com.example.taskdego.logic.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // アイテム名を取得するための状態
    var itemsWithNames by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }


    // ★ 修正：itemsWithQuantityを直接使用
    val itemsWithQuantity by viewModel.itemsWithQuantity.collectAsStateWithLifecycle()

    // アイテムが変更されたら名前を取得
    LaunchedEffect(items) {
        itemsWithNames = items.map { tItem ->
            viewModel.getItemWithName(tItem)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("アイテム確認", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ホームに戻る"
                        )
                    }
                },
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
            // ヘッダーカード
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Icon(
//                        imageVector = Icons.Default.LocalHospital,
//                        contentDescription = null,
//                        modifier = Modifier.size(48.dp),
//                        tint = MaterialTheme.colorScheme.primary
//                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "所持アイテム一覧",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "冒険に必要なアイテムを確認できます",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // アイテム一覧
            if (itemsWithQuantity.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "アイテムを持っていません",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "タスクをクリアしてアイテムを集めましょう！",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(itemsWithQuantity) { item ->
                        ItemRow(
                            itemName = item.item_name,
                            quantity = item.quantity ?: 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemRow(itemName: String, quantity: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = itemName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = getItemDescription(itemName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "×$quantity",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

// アイテムの説明を返すヘルパー関数
fun getItemDescription(itemName: String): String {
    return when (itemName) {
        "モンスターボール" -> "野生のポケモンを捕まえるためのボール"
        "スーパーボール" -> "モンスターボールより捕まえやすいボール"
        "ハイパーボール" -> "とても捕まえやすい高性能なボール"
        "きずぐすり" -> "ポケモンのHPを少し回復する"
        "げんきのかけら" -> "ひんしのポケモンを復活させる"
        else -> "冒険に役立つアイテム"
    }
}
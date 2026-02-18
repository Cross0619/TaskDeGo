package com.example.taskdego.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Forest
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskdego.logic.TaskViewModel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdventureScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    val encounteredPokemon by viewModel.encounteredPokemon.collectAsStateWithLifecycle()

    // ★ スクロール状態をAdventureScreenレベルで保持
    val listState = rememberLazyListState()

    // エンカウント中かどうかで表示を切り替える
    if (encounteredPokemon != null) {
        EncounterScreen(
            pokemonName = encounteredPokemon!!.pokemon_name,
            pokemonId = encounteredPokemon!!.pokemon_id,
            onBack = { viewModel.resetEncounter() }
        )
    } else {
        LocationSelectScreen(
            locations = locations,
            listState = listState,  // ★ 渡す
            onLocationSelected = { locationId -> viewModel.encounterPokemon(locationId) },
            onNavigateBack = onNavigateBack
        )
    }
}

// エリア選択画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectScreen(
    locations: List<com.example.taskdego.data.entity.mLocationEntity>,
    listState: LazyListState,           // ★ 追加
    onLocationSelected: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("どこへ冒険する？", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            state = listState,          // ★ 追加
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "クロワ地方",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(locations) { location ->
                Card(
                    onClick = { onLocationSelected(location.location_id) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Forest,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier.size(28.dp)
//                            )
                            Text(
                                text = location.location_name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
// エンカウント画面
@Composable
fun EncounterScreen(
    pokemonName: String,
    pokemonId: Int,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("野生のポケモンが現れた！", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // メッセージ
            Text(
                text = "あ、野生の",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = pokemonName,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "が現れた！",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ポケモン画像
            // ★ p(id).png という名前でdrawableに入れてください
            val context = androidx.compose.ui.platform.LocalContext.current
            val idPicture = "%04d".format(pokemonId)
            val resId = remember(pokemonId) {
                context.resources.getIdentifier(
                    "p$idPicture",
                    "drawable",
                    context.packageName
                )
            }

            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = pokemonName,
                    modifier = Modifier.size(200.dp)
                )
            } else {
                // 画像がない場合のプレースホルダー
                Box(
                    modifier = Modifier
                        .size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No.$pokemonId",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 戻るボタン（後で捕獲ボタンに変える）
            Button(
                onClick = onBack,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("別のエリアへ", fontSize = 18.sp)
            }
        }
    }
}
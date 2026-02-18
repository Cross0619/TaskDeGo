package com.example.taskdego.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskdego.data.entity.mPokemonEntity
import com.example.taskdego.logic.TaskViewModel
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState


// 並び替えの種類
enum class SortType {
    POKEDEX_ASC,        // 図鑑No昇順
    NAME_ASC,           // 名前昇順
    NAME_DESC,          // 名前降順
    HEIGHT_DESC,        // 高さ高い順
    HEIGHT_ASC,         // 高さ低い順
    WEIGHT_DESC,        // 重さ重い順
    WEIGHT_ASC          // 重さ軽い順
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    val allPokemons by viewModel.allPokemons.collectAsStateWithLifecycle()
    var selectedPokemonIndex by remember { mutableIntStateOf(-1) }
    var currentSortType by remember { mutableStateOf(SortType.POKEDEX_ASC) }


    // ★ スクロール状態をPokedexScreenレベルで保持
    val listState = rememberLazyListState()

    // 並び替え処理
    val sortedPokemons = remember(allPokemons, currentSortType) {
        when (currentSortType) {
            SortType.POKEDEX_ASC -> allPokemons.sortedBy { it.croix_no }
            SortType.NAME_ASC -> allPokemons.sortedBy { it.pokemon_name }
            SortType.NAME_DESC -> allPokemons.sortedByDescending { it.pokemon_name }
            SortType.HEIGHT_DESC -> allPokemons.sortedByDescending { it.tall }
            SortType.HEIGHT_ASC -> allPokemons.sortedBy { it.tall }
            SortType.WEIGHT_DESC -> allPokemons.sortedByDescending { it.weight }
            SortType.WEIGHT_ASC -> allPokemons.sortedBy { it.weight }
        }
    }

    if (selectedPokemonIndex >= 0) {
        // 詳細画面
        PokemonDetailScreen(
            pokemon = sortedPokemons[selectedPokemonIndex],
            currentIndex = selectedPokemonIndex,
            totalCount = sortedPokemons.size,
            onBack = { selectedPokemonIndex = -1 },
            onNavigateNext = {
                if (selectedPokemonIndex < sortedPokemons.size - 1) {
                    selectedPokemonIndex++
                }
            },
            onNavigatePrevious = {
                if (selectedPokemonIndex > 0) {
                    selectedPokemonIndex--
                }
            }
        )
    } else {
        // 一覧画面
        PokemonListScreen(
            pokemons = sortedPokemons,
            currentSortType = currentSortType,
            listState = listState,  // ★ 渡す
            onSortTypeChange = { currentSortType = it },
            onPokemonClick = { pokemon ->
                selectedPokemonIndex = sortedPokemons.indexOf(pokemon)
            },
            onNavigateBack = onNavigateBack
        )
    }
}

// 一覧画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    pokemons: List<mPokemonEntity>,
    currentSortType: SortType,
    listState: LazyListState,  // ★ 追加
    onSortTypeChange: (SortType) -> Unit,
    onPokemonClick: (mPokemonEntity) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("クロワ図鑑", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ホームに戻る"
                        )
                    }
                },
                actions = {
                    // 並び替えボタン
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "並び替え"
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("図鑑No順") },
                                onClick = {
                                    onSortTypeChange(SortType.POKEDEX_ASC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.POKEDEX_ASC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("名前順（あ→ん）") },
                                onClick = {
                                    onSortTypeChange(SortType.NAME_ASC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.NAME_ASC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("名前順（ん→あ）") },
                                onClick = {
                                    onSortTypeChange(SortType.NAME_DESC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.NAME_DESC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("高さ（高い順）") },
                                onClick = {
                                    onSortTypeChange(SortType.HEIGHT_DESC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.HEIGHT_DESC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("高さ（低い順）") },
                                onClick = {
                                    onSortTypeChange(SortType.HEIGHT_ASC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.HEIGHT_ASC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("重さ（重い順）") },
                                onClick = {
                                    onSortTypeChange(SortType.WEIGHT_DESC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.WEIGHT_DESC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                            DropdownMenuItem(
                                text = { Text("重さ（軽い順）") },
                                onClick = {
                                    onSortTypeChange(SortType.WEIGHT_ASC)
                                    showSortMenu = false
                                },
                                leadingIcon = if (currentSortType == SortType.WEIGHT_ASC) {
                                    { Text("✓", fontWeight = FontWeight.Bold) }
                                } else null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // ヘッダー情報
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "クロワ地方のポケモン図鑑",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "登録数: ${pokemons.size}種類",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getSortTypeLabel(currentSortType),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            // ポケモン一覧
            LazyColumn(
                state = listState,  // ★ 追加
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = pokemons,
                    key = { it.m_pokemon_id }
                ) { pokemon ->
                    PokemonListItem(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon) }
                    )
                }
            }
        }
    }
}

// 並び替えタイプのラベル取得
fun getSortTypeLabel(sortType: SortType): String {
    return when (sortType) {
        SortType.POKEDEX_ASC -> "並び順: 図鑑No順"
        SortType.NAME_ASC -> "並び順: 名前順（あ→ん）"
        SortType.NAME_DESC -> "並び順: 名前順（ん→あ）"
        SortType.HEIGHT_DESC -> "並び順: 高さ（高い順）"
        SortType.HEIGHT_ASC -> "並び順: 高さ（低い順）"
        SortType.WEIGHT_DESC -> "並び順: 重さ（重い順）"
        SortType.WEIGHT_ASC -> "並び順: 重さ（軽い順）"
    }
}

// ポケモンリストアイテム
@Composable
fun PokemonListItem(
    pokemon: mPokemonEntity,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val idPicture = "%04d".format(pokemon.m_pokemon_id)
    val resId = remember(pokemon.m_pokemon_id) {
        context.resources.getIdentifier(
            "p$idPicture",
            "drawable",
            context.packageName
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ポケモン画像（正方形）
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = pokemon.pokemon_name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = "No.${pokemon.croix_no}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ポケモン名とフォルム名
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No.${pokemon.croix_no}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = pokemon.pokemon_name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (pokemon.form_name != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pokemon.form_name,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

// 詳細画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemon: mPokemonEntity,
    currentIndex: Int,
    totalCount: Int,
    onBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit
) {
    val context = LocalContext.current
    val idPicture = "%04d".format(pokemon.m_pokemon_id)
    val resId = remember(pokemon.m_pokemon_id) {
        context.resources.getIdentifier(
            "p$idPicture",
            "drawable",
            context.packageName
        )
    }

    // スワイプ検知用
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "図鑑詳細 (${currentIndex + 1}/${totalCount})",
                        fontWeight = FontWeight.Bold
                    )
                },
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // スワイプ判定
                            when {
                                dragOffset > 100 -> onNavigatePrevious() // 右スワイプ = 前へ
                                dragOffset < -100 -> onNavigateNext()     // 左スワイプ = 次へ
                            }
                            dragOffset = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            dragOffset += dragAmount
                        }
                    )
                }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    // No. とポケモン名
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No.${pokemon.croix_no}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = pokemon.pokemon_name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // フォルム名
                    if (pokemon.form_name != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pokemon.form_name,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ポケモン画像
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (resId != 0) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = pokemon.pokemon_name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(
                                text = "No Image",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 分類
                    InfoRow(label = "分類", value = "${pokemon.species}ポケモン")

                    // タイプ
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "タイプ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(80.dp)
                        )
                        TypeBadge(type = pokemon.type1)
                        if (pokemon.type2 != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            TypeBadge(type = pokemon.type2)
                        }
                    }

                    // 高さ
                    InfoRow(label = "高さ", value = "${pokemon.tall}m")

                    // 重さ
                    InfoRow(label = "重さ", value = "${pokemon.weight}kg")

                    Spacer(modifier = Modifier.height(16.dp))

                    // 横線
//                    HorizontalDivider(
//                        thickness = 2.dp,
//                        color = MaterialTheme.colorScheme.outline
//                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 説明文
                    Text(
                        text = pokemon.description,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ナビゲーションヒント
                    Text(
                        text = "← スワイプして他のポケモンを見る →",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// 情報行コンポーネント
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
}

// タイプバッジコンポーネント
@Composable
fun TypeBadge(type: String) {
    val typeColor = getTypeColor(type)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = typeColor,
        modifier = Modifier
    ) {
        Text(
            text = type,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

// タイプごとの色を返す関数
@Composable
fun getTypeColor(type: String): Color {
    return when (type) {
        "ノーマル" -> Color(0xFFA8A878)
        "ほのお" -> Color(0xFFF08030)
        "みず" -> Color(0xFF6890F0)
        "でんき" -> Color(0xFFF8D030)
        "くさ" -> Color(0xFF78C850)
        "こおり" -> Color(0xFF98D8D8)
        "かくとう" -> Color(0xFFC03028)
        "どく" -> Color(0xFFA040A0)
        "じめん" -> Color(0xFFE0C068)
        "ひこう" -> Color(0xFFA890F0)
        "エスパー" -> Color(0xFFF85888)
        "むし" -> Color(0xFFA8B820)
        "いわ" -> Color(0xFFB8A038)
        "ゴースト" -> Color(0xFF705898)
        "ドラゴン" -> Color(0xFF7038F8)
        "あく" -> Color(0xFF705848)
        "はがね" -> Color(0xFFB8B8D0)
        "フェアリー" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }
}
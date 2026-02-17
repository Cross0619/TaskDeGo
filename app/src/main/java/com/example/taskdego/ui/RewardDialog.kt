package com.example.taskdego.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.EmojiEvents
//import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskdego.logic.RewardData

@Composable
fun RewardDialog(
    rewardData: RewardData,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // トロフィーアイコン
//                Icon(
//                    imageVector = Icons.Default.EmojiEvents,
//                    contentDescription = null,
//                    modifier = Modifier.size(64.dp),
//                    tint = MaterialTheme.colorScheme.primary
//                )

                Spacer(modifier = Modifier.height(16.dp))

                // タイトル
                Text(
                    text = "タスクをクリアした！",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 報酬一覧
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 経験値
                    RewardItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700) // ゴールド
                            )
                        },
                        label = "経験値",
                        value = "+${rewardData.exp} XP"
                    )

                    // コイン
                    RewardItem(
                        icon = {
//                            Icon(
//                                imageVector = Icons.Default.MonetizationOn,
//                                contentDescription = null,
//                                tint = Color(0xFFFFD700) // ゴールド
//                            )
                        },
                        label = "コイン",
                        value = "+${rewardData.coins}"
                    )

                    // アイテム
                    RewardItem(
                        icon = {
                            Text(
                                text = "🎁",
                                fontSize = 24.sp
                            )
                        },
                        label = "アイテム",
                        value = "${rewardData.itemName} ×${rewardData.itemQuantity}"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 閉じるボタン
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "OK",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RewardItem(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
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
                icon()
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
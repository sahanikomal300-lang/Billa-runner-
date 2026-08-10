package com.example.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.data.LevelData
import com.example.game.engine.GameUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(
    uiState: GameUiState,
    onSelectLevel: (Int) -> Unit,
    onBackToGame: () -> Unit,
    onOpenHtmlCode: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Level Selection",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackToGame,
                        modifier = Modifier.testTag("btn_back_to_game")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onOpenHtmlCode,
                        modifier = Modifier.testTag("btn_html_code_topbar")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "HTML Code",
                            tint = Color(0xFFFFA502)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F0F1B)
                )
            )
        },
        containerColor = Color(0xFF0F0F1B)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header stats bar
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B2E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL FAILS", fontSize = 11.sp, color = Color(0xFFA4B0BE))
                        Text(
                            text = "${uiState.totalDeaths}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFF4757)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF2F3542))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CLEARED", fontSize = 11.sp, color = Color(0xFFA4B0BE))
                        val cleared = uiState.levelProgressMap.values.count { it.isCompleted }
                        Text(
                            text = "$cleared / ${LevelData.levels.size}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2ED573)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Level Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(LevelData.levels) { level ->
                    val progress = uiState.levelProgressMap[level.id]
                    val isUnlocked = progress?.isUnlocked ?: (level.id == 1)
                    val isCompleted = progress?.isCompleted ?: false

                    LevelCard(
                        levelId = level.id,
                        title = level.name,
                        subtitle = level.subtitle,
                        isUnlocked = isUnlocked,
                        isCompleted = isCompleted,
                        onCardClick = {
                            if (isUnlocked) {
                                onSelectLevel(level.id - 1)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelCard(
    levelId: Int,
    title: String,
    subtitle: String,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    onCardClick: () -> Unit
) {
    val cardBg = if (isUnlocked) Color(0xFF1E1B2E) else Color(0xFF141221)
    val borderColor = when {
        isCompleted -> Color(0xFF2ED573)
        isUnlocked -> Color(0xFFFF9800)
        else -> Color(0xFF2F3542)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        modifier = Modifier
            .testTag("level_card_$levelId")
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked, onClick = onCardClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = borderColor,
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$levelId",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Cleared",
                        tint = Color(0xFF2ED573),
                        modifier = Modifier.size(22.dp)
                    )
                } else if (!isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF747D8C),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = if (isUnlocked) Color.White else Color(0xFF747D8C),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = subtitle,
                color = if (isUnlocked) Color(0xFFA4B0BE) else Color(0xFF485460),
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

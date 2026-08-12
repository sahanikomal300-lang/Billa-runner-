package com.example.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.engine.GameUiState

@Composable
fun GameControlsOverlay(
    uiState: GameUiState,
    onLeftPressedChange: (Boolean) -> Unit,
    onRightPressedChange: (Boolean) -> Unit,
    onJumpPressedChange: (Boolean) -> Unit,
    onRestartLevel: () -> Unit,
    onNextLevel: () -> Unit,
    onOpenLevelSelect: () -> Unit,
    onOpenHtmlCode: () -> Unit,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        if (maxWidth < maxHeight) {
            // Portrait Warning
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F0F1B)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(text = "📱", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ROTATE YOUR PHONE",
                        color = Color(0xFFFF4757),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Level Devil is designed for landscape mode. Turn device sideways for precision platforming!",
                        color = Color(0xFFA4B0BE),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Landscape Game UI Overlay
            Box(modifier = Modifier.fillMaxSize()) {

                // --- TOP HUD BAR ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level Name & Subtitle
                    Column {
                        Text(
                            text = uiState.currentLevel.name,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = uiState.currentLevel.subtitle,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }

                    // Stats: Timer, Stars & Death Counter
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF1E1E30).copy(alpha = 0.85f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⏱️ ${formatTime(uiState.elapsedTimeMs)}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "⭐ ${uiState.collectedStarsInLevel}/${uiState.currentLevel.stars.size}",
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "💀 ${uiState.totalDeaths}",
                                    color = Color(0xFFFF4757),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = onTogglePause,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2F3542))
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color.White)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onRestartLevel,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2F3542))
                                .testTag("restart_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Restart Level", tint = Color.White)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onOpenLevelSelect,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2F3542))
                        ) {
                            Icon(Icons.Default.GridView, contentDescription = "Level Select", tint = Color.White)
                        }
                    }
                }

                // --- BANNER NOTIFICATION ---
                if (uiState.bannerAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 50.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(uiState.bannerColor.copy(alpha = 0.95f))
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = uiState.bannerText,
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                }

                // --- TOUCH CONTROLS OVERLAY ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Left / Right DPAD
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TouchButton(
                            label = "LEFT",
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            isPressed = false,
                            onPressedChange = onLeftPressedChange,
                            testTag = "btn_left"
                        )
                        TouchButton(
                            label = "RIGHT",
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            isPressed = false,
                            onPressedChange = onRightPressedChange,
                            testTag = "btn_right"
                        )
                    }

                    // Jump Button
                    TouchButton(
                        label = "JUMP",
                        icon = Icons.Default.ArrowUpward,
                        isPressed = false,
                        onPressedChange = onJumpPressedChange,
                        sizeDp = 76,
                        buttonColor = Color(0xFF2ED573),
                        testTag = "btn_jump"
                    )
                }

                // --- VICTORY MODAL ---
                if (uiState.isLevelWon) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.82f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E30)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .width(380.dp)
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "LEVEL BEATEN! 🎉",
                                    color = Color(0xFF2ED573),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Stars Earned Display
                                Row {
                                    for (i in 1..3) {
                                        Text(
                                            text = if (i <= uiState.earnedStars) "⭐" else "🔒",
                                            fontSize = 32.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Time: ${formatTime(uiState.elapsedTimeMs)} (Par: ${formatTime(uiState.currentLevel.parTimeMs)})",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = onOpenLevelSelect,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F3542)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Levels")
                                    }

                                    Button(
                                        onClick = onNextLevel,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED573)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Next Level ➔", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // --- GAME OVER MODAL ---
                if (uiState.isDead) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.78f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E30)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .width(360.dp)
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "YOU DIED! 💀",
                                    color = Color(0xFFFF4757),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 26.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = uiState.tauntMessage,
                                    color = Color(0xFFFFA502),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = onRestartLevel,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("respawn_button")
                                ) {
                                    Text("TRY AGAIN 🔄", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TouchButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPressed: Boolean,
    onPressedChange: (Boolean) -> Unit,
    sizeDp: Int = 64,
    buttonColor: Color = Color(0xFF3742FA),
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(buttonColor.copy(alpha = 0.85f))
            .testTag(testTag)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPressedChange(true)
                        tryAwaitRelease()
                        onPressedChange(false)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size((sizeDp * 0.5f).dp)
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val millis = (ms % 1000) / 100
    return String.format("%02d:%02d.%d", minutes, seconds, millis)
}

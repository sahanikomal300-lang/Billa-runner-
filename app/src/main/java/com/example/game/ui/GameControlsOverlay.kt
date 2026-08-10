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
                    Text(
                        text = "📱",
                        fontSize = 64.sp
                    )
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
                        text = "Level Devil is designed for landscape mode. Please turn your device sideways!",
                        color = Color(0xFFA4B0BE),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Landscape Game Controls UI
            Box(modifier = Modifier.fillMaxSize()) {

                // --- TOP HUD BAR ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level Title Pill
                    Surface(
                        color = Color(0xFF1E1B2E).copy(alpha = 0.8f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = uiState.currentLevel.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    // Inverted Controls Indicator
                    if (uiState.isControlsInverted) {
                        Surface(
                            color = Color(0xFFFF4757),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "⚠️ CONTROLS FLIPPED!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Death Count & Actions
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFFF4757).copy(alpha = 0.85f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "FAILS: ${uiState.totalDeaths}",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onRestartLevel,
                            modifier = Modifier
                                .testTag("btn_restart_hud")
                                .size(32.dp)
                                .background(Color(0xFF2F3542).copy(alpha = 0.75f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Restart Level",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onOpenLevelSelect,
                            modifier = Modifier
                                .testTag("btn_level_select_hud")
                                .size(32.dp)
                                .background(Color(0xFF2F3542).copy(alpha = 0.75f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Level Select",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onOpenHtmlCode,
                            modifier = Modifier
                                .testTag("btn_html_code_hud")
                                .size(32.dp)
                                .background(Color(0xFFFFA502).copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = "View HTML Code",
                                tint = Color(0xFF1E1B2E),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // --- BOTTOM TOUCH CONTROLS ---
                if (!uiState.isDead && !uiState.isLevelWon) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left & Right D-Pad
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // LEFT BUTTON
                            ControlButton(
                                testTag = "btn_touch_left",
                                size = 56,
                                onPressChange = onLeftPressedChange
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Left",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            // RIGHT BUTTON
                            ControlButton(
                                testTag = "btn_touch_right",
                                size = 56,
                                onPressChange = onRightPressedChange
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Right",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        // JUMP BUTTON
                        ControlButton(
                            testTag = "btn_touch_jump",
                            size = 64,
                            backgroundColor = Color(0xFFFF4757).copy(alpha = 0.75f),
                            onPressChange = onJumpPressedChange
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = "Jump",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "JUMP",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // --- DEATH OVERLAY SCREEN ---
                if (uiState.isDead) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xCC0F0F1B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B2E)),
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(0.7f)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "You Failed! 😂",
                                    color = Color(0xFFFF4757),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = uiState.tauntMessage,
                                    color = Color(0xFFA4B0BE),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onRestartLevel,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .testTag("btn_try_again")
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text(
                                        text = "TRY AGAIN",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Button(
                                    onClick = onOpenLevelSelect,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F3542)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .testTag("btn_select_level_death")
                                        .fillMaxWidth()
                                        .height(40.dp)
                                ) {
                                    Text(
                                        text = "SELECT LEVEL",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // --- LEVEL WIN OVERLAY SCREEN ---
                if (uiState.isLevelWon) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xEE0F0F1B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B2E)),
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(0.7f)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "LEVEL CLEARED! 🎉",
                                    color = Color(0xFF2ED573),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "You beat the Devil's trap!",
                                    color = Color.White,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onNextLevel,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED573)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .testTag("btn_next_level")
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text(
                                        text = "NEXT LEVEL →",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1E1B2E)
                                    )
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
private fun ControlButton(
    testTag: String,
    size: Int = 56,
    backgroundColor: Color = Color(0xFF2F3542).copy(alpha = 0.65f),
    onPressChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag(testTag)
            .size(size.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPressChange(true)
                        tryAwaitRelease()
                        onPressChange(false)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

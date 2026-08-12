package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.engine.GameScreenState
import com.example.game.engine.GameViewModel
import com.example.game.ui.AchievementsScreen
import com.example.game.ui.GameCanvas
import com.example.game.ui.GameControlsOverlay
import com.example.game.ui.HtmlCodeScreen
import com.example.game.ui.LevelSelectScreen
import com.example.game.ui.MainMenuScreen
import com.example.game.ui.SettingsScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null && viewModel.handleKeyEvent(androidx.compose.ui.input.key.KeyEvent(event))) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null && viewModel.handleKeyEvent(androidx.compose.ui.input.key.KeyEvent(event))) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}

@Composable
fun MainAppScreen(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.initPreferences(context)
    }

    // Trigger haptic vibration on death or level clear
    LaunchedEffect(uiState.isDead, uiState.isLevelWon) {
        if (uiState.isDead || uiState.isLevelWon) {
            viewModel.triggerVibration(context)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { viewModel.handleKeyEvent(it) }
        ) {
            when (uiState.screenState) {
                GameScreenState.MAIN_MENU -> {
                    MainMenuScreen(
                        uiState = uiState,
                        onStartGame = { viewModel.setScreen(GameScreenState.PLAYING) },
                        onOpenLevelSelect = { viewModel.setScreen(GameScreenState.LEVEL_SELECT) },
                        onOpenAchievements = { viewModel.setScreen(GameScreenState.ACHIEVEMENTS) },
                        onOpenSettings = { viewModel.setScreen(GameScreenState.SETTINGS) },
                        onOpenHtmlCode = { viewModel.setScreen(GameScreenState.HTML_CODE_VIEW) }
                    )
                }

                GameScreenState.PLAYING, GameScreenState.PAUSED -> {
                    // Render 2D Compose Canvas Game Engine
                    GameCanvas(
                        uiState = uiState,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Render Controls Overlay (HUD + Touch D-Pad + Death / Victory Popups)
                    GameControlsOverlay(
                        uiState = uiState,
                        onLeftPressedChange = { viewModel.setLeftPressed(it) },
                        onRightPressedChange = { viewModel.setRightPressed(it) },
                        onJumpPressedChange = { viewModel.setJumpPressed(it) },
                        onRestartLevel = { viewModel.restartCurrentLevel() },
                        onNextLevel = { viewModel.nextLevel() },
                        onOpenLevelSelect = { viewModel.setScreen(GameScreenState.LEVEL_SELECT) },
                        onOpenHtmlCode = { viewModel.setScreen(GameScreenState.HTML_CODE_VIEW) },
                        onTogglePause = { viewModel.setScreen(if (uiState.screenState == GameScreenState.PAUSED) GameScreenState.PLAYING else GameScreenState.PAUSED) }
                    )

                    if (uiState.screenState == GameScreenState.PAUSED) {
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
                                    .width(320.dp)
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "GAME PAUSED ⏸️",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = { viewModel.setScreen(GameScreenState.PLAYING) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED573)),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("RESUME", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedButton(
                                        onClick = { viewModel.restartCurrentLevel(); viewModel.setScreen(GameScreenState.PLAYING) },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("RESTART LEVEL", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedButton(
                                        onClick = { viewModel.setScreen(GameScreenState.MAIN_MENU) },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("MAIN MENU", color = Color(0xFFA4B0BE), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Auto-request focus for physical keyboard listeners
                    LaunchedEffect(Unit) {
                        try {
                            focusRequester.requestFocus()
                        } catch (_: Exception) {}
                    }
                }

                GameScreenState.LEVEL_SELECT -> {
                    LevelSelectScreen(
                        uiState = uiState,
                        onSelectLevel = { levelIdx ->
                            viewModel.loadLevel(levelIdx)
                            viewModel.setScreen(GameScreenState.PLAYING)
                        },
                        onBackToMenu = {
                            viewModel.setScreen(GameScreenState.MAIN_MENU)
                        }
                    )
                }

                GameScreenState.SETTINGS -> {
                    SettingsScreen(
                        uiState = uiState,
                        onToggleSound = { viewModel.toggleSound() },
                        onToggleMusic = { viewModel.toggleMusic() },
                        onClearData = { viewModel.clearAllProgress(context) },
                        onBackToMenu = { viewModel.setScreen(GameScreenState.MAIN_MENU) }
                    )
                }

                GameScreenState.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        uiState = uiState,
                        onBackToMenu = { viewModel.setScreen(GameScreenState.MAIN_MENU) }
                    )
                }

                GameScreenState.HTML_CODE_VIEW -> {
                    HtmlCodeScreen(
                        onBack = {
                            viewModel.setScreen(GameScreenState.MAIN_MENU)
                        }
                    )
                }
            }
        }
    }
}

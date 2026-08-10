package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import com.example.game.engine.GameScreenState
import com.example.game.engine.GameViewModel
import com.example.game.ui.GameCanvas
import com.example.game.ui.GameControlsOverlay
import com.example.game.ui.HtmlCodeScreen
import com.example.game.ui.LevelSelectScreen
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
                GameScreenState.PLAYING, GameScreenState.MENU -> {
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
                        onOpenHtmlCode = { viewModel.setScreen(GameScreenState.HTML_CODE_VIEW) }
                    )

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
                        onBackToGame = {
                            viewModel.setScreen(GameScreenState.PLAYING)
                        },
                        onOpenHtmlCode = {
                            viewModel.setScreen(GameScreenState.HTML_CODE_VIEW)
                        }
                    )
                }

                GameScreenState.HTML_CODE_VIEW -> {
                    HtmlCodeScreen(
                        onBack = {
                            viewModel.setScreen(GameScreenState.PLAYING)
                        }
                    )
                }

                GameScreenState.SETTINGS -> {
                    // Fallback to playing screen
                    viewModel.setScreen(GameScreenState.PLAYING)
                }
            }
        }
    }
}

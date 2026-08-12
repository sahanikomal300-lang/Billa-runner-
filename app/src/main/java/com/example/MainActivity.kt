package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.LoadAdError

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    private var rewardedAd: RewardedAd? = null

    // Google test Rewarded Ad Unit ID
    // Testing complete hone ke baad ise apne AdMob ID se replace karna.
    private val rewardedAdUnitId =
        "ca-app-pub-3940256099942544/5224354917"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize AdMob
        MobileAds.initialize(this)

        // Pre-load Rewarded Ad
        loadRewardedAd()

        setContent {
            MyApplicationTheme {
                MainAppScreen(
                    viewModel = viewModel,
                    activity = this
                )
            }
        }
    }

    private fun loadRewardedAd() {

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this,
            rewardedAdUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    fun showRewardedAd(onFinished: () -> Unit) {

        val ad = rewardedAd

        if (ad == null) {
            // Ad available nahi hai to game continue
            onFinished()

            // Next ad load karo
            loadRewardedAd()
            return
        }

        ad.show(this) {

            // Reward milne ke baad next level
            onFinished()

            // Agla ad preload
            rewardedAd = null
            loadRewardedAd()
        }
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        if (
            event != null &&
            viewModel.handleKeyEvent(
                androidx.compose.ui.input.key.KeyEvent(event)
            )
        ) {
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        if (
            event != null &&
            viewModel.handleKeyEvent(
                androidx.compose.ui.input.key.KeyEvent(event)
            )
        ) {
            return true
        }

        return super.onKeyUp(keyCode, event)
    }
}


@Composable
fun MainAppScreen(
    viewModel: GameViewModel,
    activity: MainActivity
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        viewModel.initPreferences(context)
    }

    // Death / Level complete vibration
    LaunchedEffect(
        uiState.isDead,
        uiState.isLevelWon
    ) {

        if (
            uiState.isDead ||
            uiState.isLevelWon
        ) {
            viewModel.triggerVibration(context)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent {
                    viewModel.handleKeyEvent(it)
                }
        ) {

            when (uiState.screenState) {

                // =========================
                // MAIN MENU
                // =========================

                GameScreenState.MAIN_MENU -> {

                    MainMenuScreen(
                        uiState = uiState,

                        onStartGame = {
                            viewModel.setScreen(
                                GameScreenState.PLAYING
                            )
                        },

                        onOpenLevelSelect = {
                            viewModel.setScreen(
                                GameScreenState.LEVEL_SELECT
                            )
                        },

                        onOpenAchievements = {
                            viewModel.setScreen(
                                GameScreenState.ACHIEVEMENTS
                            )
                        },

                        onOpenSettings = {
                            viewModel.setScreen(
                                GameScreenState.SETTINGS
                            )
                        },

                        onOpenHtmlCode = {
                            viewModel.setScreen(
                                GameScreenState.HTML_CODE_VIEW
                            )
                        }
                    )
                }


                // =========================
                // PLAYING / PAUSED
                // =========================

                GameScreenState.PLAYING,
                GameScreenState.PAUSED -> {

                    GameCanvas(
                        uiState = uiState,
                        modifier = Modifier.fillMaxSize()
                    )


                    GameControlsOverlay(

                        uiState = uiState,

                        onLeftPressedChange = {
                            viewModel.setLeftPressed(it)
                        },

                        onRightPressedChange = {
                            viewModel.setRightPressed(it)
                        },

                        onJumpPressedChange = {
                            viewModel.setJumpPressed(it)
                        },

                        onRestartLevel = {
                            viewModel.restartCurrentLevel()
                        },

                        // =========================
                        // NEXT LEVEL + REWARDED AD
                        // =========================

                        onNextLevel = {

                            activity.showRewardedAd {

                                viewModel.nextLevel()

                            }
                        },

                        onOpenLevelSelect = {
                            viewModel.setScreen(
                                GameScreenState.LEVEL_SELECT
                            )
                        },

                        onOpenHtmlCode = {
                            viewModel.setScreen(
                                GameScreenState.HTML_CODE_VIEW
                            )
                        },

                        onTogglePause = {

                            viewModel.setScreen(

                                if (
                                    uiState.screenState ==
                                    GameScreenState.PAUSED
                                ) {
                                    GameScreenState.PLAYING
                                } else {
                                    GameScreenState.PAUSED
                                }

                            )
                        }
                    )


                    // =========================
                    // PAUSE SCREEN
                    // =========================

                    if (
                        uiState.screenState ==
                        GameScreenState.PAUSED
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black.copy(
                                        alpha = 0.82f
                                    )
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Card(
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            Color(0xFF1E1E30)
                                    ),

                                shape =
                                    RoundedCornerShape(24.dp),

                                modifier =
                                    Modifier
                                        .width(320.dp)
                                        .padding(16.dp)
                            ) {

                                Column(
                                    modifier =
                                        Modifier.padding(24.dp),

                                    horizontalAlignment =
                                        Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text =
                                            "GAME PAUSED ⏸️",

                                        color =
                                            Color.White,

                                        fontWeight =
                                            FontWeight.Black,

                                        fontSize =
                                            22.sp
                                    )


                                    Spacer(
                                        modifier =
                                            Modifier.height(20.dp)
                                    )


                                    Button(
                                        onClick = {

                                            viewModel.setScreen(
                                                GameScreenState.PLAYING
                                            )
                                        },

                                        colors =
                                            ButtonDefaults.buttonColors(
                                                containerColor =
                                                    Color(0xFF2ED573)
                                            ),

                                        shape =
                                            RoundedCornerShape(12.dp),

                                        modifier =
                                            Modifier.fillMaxWidth()
                                    ) {

                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.width(8.dp)
                                        )

                                        Text(
                                            "RESUME",
                                            color = Color.Black,
                                            fontWeight =
                                                FontWeight.Bold
                                        )
                                    }


                                    Spacer(
                                        modifier =
                                            Modifier.height(10.dp)
                                    )


                                    OutlinedButton(
                                        onClick = {

                                            viewModel.restartCurrentLevel()

                                            viewModel.setScreen(
                                                GameScreenState.PLAYING
                                            )
                                        },

                                        shape =
                                            RoundedCornerShape(12.dp),

                                        modifier =
                                            Modifier.fillMaxWidth()
                                    ) {

                                        Icon(
                                            Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = Color.White
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.width(8.dp)
                                        )

                                        Text(
                                            "RESTART LEVEL",
                                            color = Color.White,
                                            fontWeight =
                                                FontWeight.Bold
                                        )
                                    }


                                    Spacer(
                                       

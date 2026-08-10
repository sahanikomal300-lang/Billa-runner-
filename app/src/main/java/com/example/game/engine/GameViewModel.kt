package com.example.game.engine

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.data.LevelData
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.Particle
import com.example.game.model.Platform
import com.example.game.model.Player
import com.example.game.model.Spike
import com.example.game.model.TrapType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.random.Random

enum class GameScreenState {
    MENU,
    PLAYING,
    LEVEL_SELECT,
    HTML_CODE_VIEW,
    SETTINGS
}

data class LevelProgress(
    val levelId: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val deathCount: Int = 0
)

data class GameUiState(
    val screenState: GameScreenState = GameScreenState.PLAYING,
    val currentLevelIndex: Int = 0,
    val currentLevel: Level = LevelData.levels[0],
    val player: Player = Player(x = 50f, y = 220f),
    val platforms: List<Platform> = emptyList(),
    val spikes: List<Spike> = emptyList(),
    val door: Door = Door(x = 730f, y = 212f),
    val particles: List<Particle> = emptyList(),
    val cameraX: Float = 0f,
    val isDead: Boolean = false,
    val isLevelWon: Boolean = false,
    val totalDeaths: Int = 0,
    val isControlsInverted: Boolean = false,
    val tauntMessage: String = "",
    val levelProgressMap: Map<Int, LevelProgress> = emptyMap(),
    val isSoundEnabled: Boolean = true
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Inputs
    private var leftInput = false
    private var rightInput = false
    private var jumpInput = false

    // Tuning Constants for Smooth Pixel-Art Platforming
    private val moveSpeed = 5.2f
    private val accel = 0.22f
    private val jumpPower = -11.5f
    private val gravity = 0.52f

    // Coyote & Jump Buffer
    private var coyoteTimer = 0
    private var jumpBufferTimer = 0

    init {
        val initialProgress = LevelData.levels.associate {
            it.id to LevelProgress(it.id, isUnlocked = (it.id == 1))
        }
        _uiState.update { it.copy(levelProgressMap = initialProgress) }
        loadLevel(0)
        startGameLoop()
    }

    fun setScreen(screen: GameScreenState) {
        _uiState.update { it.copy(screenState = screen) }
    }

    fun loadLevel(index: Int) {
        val safeIndex = index.coerceAtLeast(0)
        val level = LevelData.getLevel(safeIndex)

        val platformsCopy = level.platforms.map { it.copy() }
        val spikesCopy = level.spikes.map { it.copy() }
        val doorCopy = level.door.copy()
        val playerStart = Player(x = level.playerStart.first, y = level.playerStart.second)

        leftInput = false
        rightInput = false
        jumpInput = false
        coyoteTimer = 0
        jumpBufferTimer = 0

        _uiState.update {
            it.copy(
                currentLevelIndex = safeIndex,
                currentLevel = level,
                player = playerStart,
                platforms = platformsCopy,
                spikes = spikesCopy,
                door = doorCopy,
                particles = emptyList(),
                cameraX = 0f,
                isDead = false,
                isLevelWon = false,
                isControlsInverted = false,
                tauntMessage = ""
            )
        }
    }

    fun restartCurrentLevel() {
        loadLevel(_uiState.value.currentLevelIndex)
    }

    fun nextLevel() {
        val nextIdx = _uiState.value.currentLevelIndex + 1
        loadLevel(nextIdx)
    }

    // Touch Controls
    fun setLeftPressed(pressed: Boolean) {
        leftInput = pressed
    }

    fun setRightPressed(pressed: Boolean) {
        rightInput = pressed
    }

    fun setJumpPressed(pressed: Boolean) {
        jumpInput = pressed
        if (pressed) {
            jumpBufferTimer = 6
            if (_uiState.value.isDead || _uiState.value.isLevelWon) {
                if (_uiState.value.isLevelWon) nextLevel()
                else restartCurrentLevel()
            }
        }
    }

    // Keyboard Input
    fun handleKeyEvent(event: KeyEvent): Boolean {
        val isDown = event.type == KeyEventType.KeyDown
        val isUp = event.type == KeyEventType.KeyUp

        if (!isDown && !isUp) return false

        when (event.key) {
            Key.DirectionLeft, Key.A -> {
                leftInput = isDown
                return true
            }
            Key.DirectionRight, Key.D -> {
                rightInput = isDown
                return true
            }
            Key.DirectionUp, Key.Spacebar, Key.W -> {
                jumpInput = isDown
                if (isDown) {
                    jumpBufferTimer = 6
                    if (_uiState.value.isDead || _uiState.value.isLevelWon) {
                        if (_uiState.value.isLevelWon) nextLevel()
                        else restartCurrentLevel()
                    }
                }
                return true
            }
            Key.R -> {
                if (isDown) restartCurrentLevel()
                return true
            }
        }
        return false
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(16) // ~60 FPS
                if (_uiState.value.screenState == GameScreenState.PLAYING) {
                    tickPhysics()
                }
            }
        }
    }

    private fun tickPhysics() {
        val state = _uiState.value
        if (state.isDead) {
            updateParticles()
            return
        }
        if (state.isLevelWon) return

        val player = state.player.copy()
        var inverted = state.isControlsInverted
        val platforms = state.platforms.map { it.copy() }
        val spikes = state.spikes.map { it.copy() }
        val door = state.door.copy()

        // Horizontal input
        val moveLeft = if (inverted) rightInput else leftInput
        val moveRight = if (inverted) leftInput else rightInput

        val targetVx = when {
            moveLeft -> -moveSpeed
            moveRight -> moveSpeed
            else -> 0f
        }

        // Smooth acceleration & friction
        player.vx += (targetVx - player.vx) * accel
        if (abs(player.vx) > 0.1f) {
            player.facingRight = player.vx > 0
        }

        // Timers update
        if (player.isGrounded) {
            coyoteTimer = 6
        } else if (coyoteTimer > 0) {
            coyoteTimer--
        }

        if (jumpBufferTimer > 0) {
            jumpBufferTimer--
        }

        // Jump Execution
        if (jumpBufferTimer > 0 && coyoteTimer > 0) {
            player.vy = jumpPower
            player.isGrounded = false
            coyoteTimer = 0
            jumpBufferTimer = 0
        }

        // Apply Gravity
        player.vy += gravity

        // Horizontal Movement & Collisions
        player.x += player.vx
        if (player.x < 0f) player.x = 0f

        platforms.forEach { platform ->
            if (!platform.isVisible) return@forEach

            // Trap: Disappear on Approach
            if (platform.trapType == TrapType.DISAPPEAR_ON_APPROACH) {
                val pCenterX = platform.x + platform.width / 2f
                val playerCenterX = player.x + player.width / 2f
                if (abs(pCenterX - playerCenterX) < platform.triggerDistance) {
                    platform.isVisible = false
                }
            }

            if (platform.isVisible && player.bounds.overlaps(platform.bounds)) {
                if (player.vx > 0) player.x = platform.x - player.width
                else if (player.vx < 0) player.x = platform.x + platform.width
            }
        }

        // Vertical Movement & Collisions
        player.y += player.vy
        player.isGrounded = false

        platforms.forEach { platform ->
            if (!platform.isVisible) return@forEach

            // Falling Block Trap
            if (platform.trapType == TrapType.FALLING_BLOCK) {
                val pCenterX = platform.x + platform.width / 2f
                val playerCenterX = player.x + player.width / 2f
                if (abs(pCenterX - playerCenterX) < platform.triggerDistance && !platform.isTriggered) {
                    platform.isTriggered = true
                    platform.vy = 10f
                }
                if (platform.isTriggered) {
                    platform.y += platform.vy
                }
            }

            if (platform.isVisible && player.bounds.overlaps(platform.bounds)) {
                if (player.vy > 0) { // Landing
                    player.y = platform.y - player.height
                    player.vy = 0f
                    player.isGrounded = true

                    if (platform.trapType == TrapType.INVERT_CONTROLS) {
                        inverted = true
                    }

                    if (platform.trapType == TrapType.DISAPPEAR_ON_TOUCH && !platform.isTriggered) {
                        platform.isTriggered = true
                        viewModelScope.launch {
                            delay(160)
                            platform.isVisible = false
                            _uiState.update { curr ->
                                curr.copy(platforms = curr.platforms.map { if (it.id == platform.id) it.copy(isVisible = false) else it })
                            }
                        }
                    }
                } else if (player.vy < 0) { // Ceiling hit
                    player.y = platform.y + platform.height
                    player.vy = 0f
                }
            }
        }

        // Spikes
        spikes.forEach { spike ->
            if (spike.isHiddenSpike) {
                val playerCenterX = player.x + player.width / 2f
                val spikeCenterX = spike.x + spike.width / 2f
                if (abs(playerCenterX - spikeCenterX) < spike.triggerDistance) {
                    spike.currentY += (spike.targetY - spike.currentY) * 0.25f
                }
            }

            if (spike.isVisible && player.bounds.overlaps(spike.bounds)) {
                triggerPlayerDeath()
                return
            }
        }

        // Door Trap
        if (door.isMovingDoor && !door.hasMoved) {
            val distToDoor = hypot((player.x - door.x).toDouble(), (player.y - door.y).toDouble())
            if (distToDoor < door.triggerDistance) {
                door.x = door.targetX
                door.hasMoved = true
            }
        }

        // Goal Check
        if (player.bounds.overlaps(door.bounds)) {
            triggerLevelVictory()
            return
        }

        // Fall Off Screen
        if (player.y > 340f) {
            triggerPlayerDeath()
            return
        }

        // Smooth Camera Lerp
        val targetCamX = (player.x - 200f).coerceAtLeast(0f)
        val newCamX = state.cameraX + (targetCamX - state.cameraX) * 0.12f

        _uiState.update {
            it.copy(
                player = player,
                platforms = platforms,
                spikes = spikes,
                door = door,
                cameraX = newCamX,
                isControlsInverted = inverted
            )
        }
    }

    private fun triggerPlayerDeath() {
        val state = _uiState.value
        if (state.isDead) return

        val newDeaths = state.totalDeaths + 1
        val randomTaunt = LevelData.funnyTaunts.random()

        val newParticles = List(30) {
            Particle(
                x = state.player.x + state.player.width / 2f,
                y = state.player.y + state.player.height / 2f,
                vx = (Random.nextFloat() - 0.5f) * 12f,
                vy = (Random.nextFloat() - 0.5f) * 12f,
                size = Random.nextFloat() * 6f + 4f,
                color = Color(0xFFFF4757)
            )
        }

        _uiState.update {
            it.copy(
                isDead = true,
                totalDeaths = newDeaths,
                tauntMessage = randomTaunt,
                particles = newParticles
            )
        }
    }

    private fun updateParticles() {
        val updated = _uiState.value.particles.mapNotNull { p ->
            val nextAlpha = p.alpha - 0.05f
            if (nextAlpha <= 0f) null
            else p.copy(x = p.x + p.vx, y = p.y + p.vy, alpha = nextAlpha)
        }
        _uiState.update { it.copy(particles = updated) }
    }

    private fun triggerLevelVictory() {
        val state = _uiState.value
        val currId = state.currentLevel.id
        val nextId = currId + 1

        val updatedMap = state.levelProgressMap.toMutableMap()
        updatedMap[currId] = updatedMap[currId]?.copy(isCompleted = true) ?: LevelProgress(currId, isUnlocked = true, isCompleted = true)
        updatedMap[nextId] = LevelProgress(nextId, isUnlocked = true)

        _uiState.update {
            it.copy(
                isLevelWon = true,
                levelProgressMap = updatedMap
            )
        }
    }

    fun triggerVibration(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(80)
            }
        } catch (_: Exception) {
            // Safe fallback
        }
    }
}

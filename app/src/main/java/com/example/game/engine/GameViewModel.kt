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
import com.example.game.model.Checkpoint
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.LevelKey
import com.example.game.model.Particle
import com.example.game.model.Platform
import com.example.game.model.Player
import com.example.game.model.Spike
import com.example.game.model.TrapType
import com.example.game.model.TriggerZone
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sin
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
    val keys: List<LevelKey> = emptyList(),
    val checkpoints: List<Checkpoint> = emptyList(),
    val door: Door = Door(x = 730f, y = 212f),
    val particles: List<Particle> = emptyList(),
    val cameraX: Float = 0f,
    val isDead: Boolean = false,
    val isLevelWon: Boolean = false,
    val totalDeaths: Int = 0,
    val isControlsInverted: Boolean = false,
    val isReverseGravity: Boolean = false,
    val tauntMessage: String = "",
    val bannerText: String = "",
    val bannerColor: Color = Color.Yellow,
    val bannerAlpha: Float = 0f,
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
    private var moveSpeed = 5.0f
    private val accel = 0.22f
    private var jumpPower = -15.5f
    private var gravity = 0.45f

    // Coyote & Jump Buffer
    private var coyoteTimer = 0
    private var jumpBufferTimer = 0

    // Active Checkpoint position
    private var activeCheckpointPos: Pair<Float, Float>? = null

    // Banner Timer
    private var bannerTicks = 0

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
        val keysCopy = level.keys.map { it.copy() }
        val checkpointsCopy = level.checkpoints.map { it.copy() }
        val doorCopy = level.door.copy(isLocked = level.keys.isNotEmpty())
        val playerStart = Player(
            x = level.playerStart.first,
            y = level.playerStart.second,
            width = 24f,
            height = 28f,
            targetWidth = 24f,
            targetHeight = 28f
        )

        leftInput = false
        rightInput = false
        jumpInput = false
        coyoteTimer = 0
        jumpBufferTimer = 0
        activeCheckpointPos = null

        // Reset gravity & physics
        gravity = 0.45f
        jumpPower = -15.5f

        _uiState.update {
            it.copy(
                currentLevelIndex = safeIndex,
                currentLevel = level,
                player = playerStart,
                platforms = platformsCopy,
                spikes = spikesCopy,
                keys = keysCopy,
                checkpoints = checkpointsCopy,
                door = doorCopy,
                particles = emptyList(),
                cameraX = 0f,
                isDead = false,
                isLevelWon = false,
                isControlsInverted = false,
                isReverseGravity = false,
                tauntMessage = "",
                bannerText = "",
                bannerAlpha = 0f
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

    fun showBanner(text: String, color: Color = Color(0xFFFFA502)) {
        bannerTicks = 90 // ~1.5 sec
        _uiState.update {
            it.copy(bannerText = text, bannerColor = color, bannerAlpha = 1f)
        }
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
                else respawnOrRestart()
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
                        else respawnOrRestart()
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

    private fun respawnOrRestart() {
        val state = _uiState.value
        val checkpoint = activeCheckpointPos
        if (checkpoint != null) {
            // Respawn at checkpoint!
            val respawnPlayer = state.player.copy(
                x = checkpoint.first,
                y = checkpoint.second - state.player.height,
                vx = 0f,
                vy = 0f,
                isGrounded = false
            )

            // Reset platforms & door state safely
            val platformsCopy = state.currentLevel.platforms.map { it.copy() }
            val spikesCopy = state.currentLevel.spikes.map { it.copy() }
            val doorCopy = state.currentLevel.door.copy()

            _uiState.update {
                it.copy(
                    player = respawnPlayer,
                    platforms = platformsCopy,
                    spikes = spikesCopy,
                    door = doorCopy,
                    particles = emptyList(),
                    isDead = false,
                    isLevelWon = false,
                    tauntMessage = ""
                )
            }
        } else {
            restartCurrentLevel()
        }
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

        // Update Banner Text alpha decay
        if (bannerTicks > 0) {
            bannerTicks--
            val newAlpha = (bannerTicks / 90f).coerceIn(0f, 1f)
            _uiState.update { it.copy(bannerAlpha = newAlpha) }
        }

        if (state.isDead) {
            updateParticles()
            return
        }
        if (state.isLevelWon) return

        val player = state.player.copy()
        var inverted = state.isControlsInverted
        var isReverseGravity = state.isReverseGravity
        val platforms = state.platforms.map { it.copy() }
        val spikes = state.spikes.map { it.copy() }
        val keys = state.keys.map { it.copy() }
        val checkpoints = state.checkpoints.map { it.copy() }
        val triggerZones = state.currentLevel.triggerZones
        val door = state.door.copy()

        // 1. Smoothly interpolate player size
        player.width += (player.targetWidth - player.width) * 0.15f
        player.height += (player.targetHeight - player.height) * 0.15f

        // 2. Adjust gravity direction & magnitude
        val curGravity = if (isReverseGravity) -gravity else gravity
        val curJumpPower = if (isReverseGravity) -jumpPower else jumpPower

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
            player.vy = curJumpPower
            player.isGrounded = false
            coyoteTimer = 0
            jumpBufferTimer = 0
            if (state.isSoundEnabled) GameAudio.playJumpSound()
        }

        // Apply Gravity
        player.vy += curGravity

        // --- UPDATE MOVING PLATFORMS ---
        platforms.forEach { platform ->
            if (platform.trapType == TrapType.MOVING_PLATFORM && platform.isVisible) {
                val dx = platform.endX - platform.startX
                val dy = platform.endY - platform.startY
                val dist = hypot(dx.toDouble(), dy.toDouble()).toFloat()

                if (dist > 0f) {
                    platform.moveProgress += (platform.moveSpeed / dist) * platform.moveDirection
                    if (platform.moveProgress >= 1f) {
                        platform.moveProgress = 1f
                        platform.moveDirection = -1
                    } else if (platform.moveProgress <= 0f) {
                        platform.moveProgress = 0f
                        platform.moveDirection = 1
                    }

                    val oldX = platform.x
                    val oldY = platform.y
                    platform.x = platform.startX + dx * platform.moveProgress
                    platform.y = platform.startY + dy * platform.moveProgress

                    // Move standing player with platform
                    if (player.isGrounded && player.bounds.overlaps(platform.bounds)) {
                        player.x += (platform.x - oldX)
                        player.y += (platform.y - oldY)
                    }
                }
            }
        }

        // --- TRIGGER ZONES CHECK ---
        triggerZones.forEach { zone ->
            if (player.bounds.overlaps(zone.bounds)) {
                when (zone.trapType) {
                    TrapType.SIZE_GIANT -> {
                        player.targetWidth = 44f
                        player.targetHeight = 52f
                        showBanner("🔥 GIANT MODE!", Color(0xFFFF4757))
                    }
                    TrapType.SIZE_TINY -> {
                        player.targetWidth = 12f
                        player.targetHeight = 14f
                        showBanner("🔬 TINY MODE!", Color(0xFF00D2D3))
                    }
                    TrapType.SIZE_NORMAL -> {
                        player.targetWidth = 24f
                        player.targetHeight = 28f
                        showBanner("⚡ NORMAL SIZE", Color(0xFF2ED573))
                    }
                    TrapType.GRAVITY_REVERSE -> {
                        isReverseGravity = true
                        showBanner("🌌 GRAVITY REVERSED!", Color(0xFF9C27B0))
                    }
                    TrapType.GRAVITY_NORMAL -> {
                        isReverseGravity = false
                        showBanner("⬇️ GRAVITY NORMAL", Color(0xFF2ED573))
                    }
                    TrapType.INVERT_CONTROLS -> {
                        inverted = true
                        showBanner("⚠️ CONTROLS FLIPPED!", Color(0xFFFF4757))
                    }
                    else -> {}
                }
            }
        }

        // --- HORIZONTAL MOVEMENT & COLLISIONS ---
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
                // Breakable wall check when GIANT
                if (platform.isBreakable && player.isGiant) {
                    platform.isVisible = false
                    showBanner("💥 WALL SHATTERED!", Color(0xFFFF4757))
                } else {
                    if (player.vx > 0) player.x = platform.x - player.width
                    else if (player.vx < 0) player.x = platform.x + platform.width
                }
            }
        }

        // --- VERTICAL MOVEMENT & COLLISIONS ---
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

            // Sinking Floor Trap
            if (platform.trapType == TrapType.SINKING_FLOOR && platform.isTriggered) {
                platform.y += 2.5f
            }

            if (platform.isVisible && player.bounds.overlaps(platform.bounds)) {
                if (!isReverseGravity) { // Normal Gravity
                    if (player.vy > 0) { // Landing on top
                        player.y = platform.y - player.height
                        player.vy = 0f
                        player.isGrounded = true

                        if (platform.trapType == TrapType.INVERT_CONTROLS) {
                            inverted = true
                        }

                        if (platform.trapType == TrapType.SINKING_FLOOR) {
                            platform.isTriggered = true
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
                    } else if (player.vy < 0) { // Hitting ceiling from below
                        player.y = platform.y + platform.height
                        player.vy = 0f
                    }
                } else { // Reverse Gravity (walking on undersides of platforms / ceiling)
                    if (player.vy < 0) { // Falling upward onto platform bottom
                        player.y = platform.y + platform.height
                        player.vy = 0f
                        player.isGrounded = true
                    } else if (player.vy > 0) { // Hitting floor from above
                        player.y = platform.y - player.height
                        player.vy = 0f
                    }
                }
            }
        }

        // --- KEYS OVERLAP & FLEEING LOGIC ---
        keys.forEach { key ->
            if (!key.isCollected) {
                if (key.isMovingKey && !key.hasMoved) {
                    val distToKey = hypot((player.x - key.x).toDouble(), (player.y - key.y).toDouble()).toFloat()
                    if (distToKey < key.triggerDistance) {
                        key.x = key.targetX
                        key.hasMoved = true
                        showBanner("💨 KEY FLEEING!", Color(0xFFFF4757))
                    }
                }

                if (player.bounds.overlaps(key.bounds)) {
                    key.isCollected = true
                    val remainingKeys = keys.count { !it.isCollected && it.id != key.id }
                    if (remainingKeys == 0) {
                        door.isLocked = false
                        showBanner("🔑 KEY COLLECTED! DOOR UNLOCKED!", Color(0xFFFFD700))
                    } else {
                        showBanner("🔑 KEY COLLECTED! ($remainingKeys REMAINING)", Color(0xFFFFD700))
                    }
                    if (state.isSoundEnabled) GameAudio.playKeyCollectSound()
                }
            }
        }

        // --- CHECKPOINT OVERLAP ---
        checkpoints.forEach { chk ->
            if (!chk.isActivated && player.bounds.overlaps(chk.bounds)) {
                chk.isActivated = true
                activeCheckpointPos = Pair(chk.x, chk.y)
                showBanner("🚩 CHECKPOINT!", Color(0xFF2ED573))
                if (state.isSoundEnabled) GameAudio.playCheckpointSound()
            }
        }

        // --- SPIKES OVERLAP ---
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

        // --- DOOR TRAP & GOAL ---
        if (door.isMovingDoor && !door.hasMoved) {
            val distToDoor = hypot((player.x - door.x).toDouble(), (player.y - door.y).toDouble())
            if (distToDoor < door.triggerDistance) {
                door.x = door.targetX
                door.hasMoved = true
            }
        }

        if (player.bounds.overlaps(door.bounds)) {
            if (door.isLocked) {
                showBanner("🔒 DOOR IS LOCKED! FIND THE KEY!", Color(0xFFFF4757))
            } else {
                triggerLevelVictory()
                return
            }
        }

        // --- FALL OFF SCREEN (TOP OR BOTTOM) ---
        if (player.y > 360f || player.y < -100f) {
            triggerPlayerDeath()
            return
        }

        // --- SMOOTH CAMERA LERP ---
        val targetCamX = (player.x - 200f).coerceAtLeast(0f)
        val newCamX = state.cameraX + (targetCamX - state.cameraX) * 0.12f

        _uiState.update {
            it.copy(
                player = player,
                platforms = platforms,
                spikes = spikes,
                keys = keys,
                checkpoints = checkpoints,
                door = door,
                cameraX = newCamX,
                isControlsInverted = inverted,
                isReverseGravity = isReverseGravity
            )
        }
    }

    private fun triggerPlayerDeath() {
        val state = _uiState.value
        if (state.isDead) return

        if (state.isSoundEnabled) GameAudio.playDeathSound()

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
        if (state.isSoundEnabled) GameAudio.playWinSound()

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

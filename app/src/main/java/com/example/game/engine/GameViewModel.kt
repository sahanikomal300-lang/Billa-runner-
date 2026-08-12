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
import com.example.game.data.GamePreferences
import com.example.game.data.LevelData
import com.example.game.model.Checkpoint
import com.example.game.model.CollectibleStar
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.LevelKey
import com.example.game.model.Particle
import com.example.game.model.Platform
import com.example.game.model.Player
import com.example.game.model.Spike
import com.example.game.model.SpringPad
import com.example.game.model.Teleporter
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
import kotlin.random.Random

enum class GameScreenState {
    MAIN_MENU,
    PLAYING,
    LEVEL_SELECT,
    SETTINGS,
    ACHIEVEMENTS,
    HTML_CODE_VIEW,
    PAUSED
}

data class LevelProgress(
    val levelId: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val deathCount: Int = 0,
    val stars: Int = 0,
    val bestTimeMs: Long = 0L
)

data class GameUiState(
    val screenState: GameScreenState = GameScreenState.MAIN_MENU,
    val currentLevelIndex: Int = 0,
    val currentLevel: Level = LevelData.levels[0],
    val player: Player = Player(x = 50f, y = 220f),
    val platforms: List<Platform> = emptyList(),
    val spikes: List<Spike> = emptyList(),
    val keys: List<LevelKey> = emptyList(),
    val teleporters: List<Teleporter> = emptyList(),
    val springs: List<SpringPad> = emptyList(),
    val stars: List<CollectibleStar> = emptyList(),
    val checkpoints: List<Checkpoint> = emptyList(),
    val door: Door = Door(x = 730f, y = 212f),
    val particles: List<Particle> = emptyList(),
    val cameraX: Float = 0f,
    val cameraY: Float = 0f,
    val cameraShake: Float = 0f,
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
    val isSoundEnabled: Boolean = true,
    val isMusicEnabled: Boolean = true,
    val elapsedTimeMs: Long = 0L,
    val collectedStarsInLevel: Int = 0,
    val earnedStars: Int = 0,
    val bestTimeMs: Long = 0L,
    val unlockedAchievements: Set<String> = emptySet()
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var prefs: GamePreferences? = null

    // Inputs
    private var leftInput = false
    private var rightInput = false
    private var jumpInput = false

    // Tuning Constants for Smooth Pixel-Art Platforming
    private var moveSpeed = 5.2f
    private val accel = 0.25f
    private val friction = 0.82f
    private val iceFriction = 0.96f
    private var jumpPower = -15.5f
    private var gravity = 0.45f

    // Coyote & Jump Buffer
    private var coyoteTimer = 0
    private var jumpBufferTimer = 0

    // Checkpoint
    private var activeCheckpointPos: Pair<Float, Float>? = null

    // Timer & Stopwatch
    private var levelStartTimeMs = 0L
    private var bannerTicks = 0

    init {
        val initialProgress = LevelData.levels.associate {
            it.id to LevelProgress(it.id, isUnlocked = (it.id == 1))
        }
        _uiState.update { it.copy(levelProgressMap = initialProgress) }
        loadLevel(0)
        startGameLoop()
    }

    fun initPreferences(context: Context) {
        if (prefs == null) {
            prefs = GamePreferences(context)
            loadSavedPreferences()
        }
    }

    private fun loadSavedPreferences() {
        val p = prefs ?: return
        val totalDeaths = p.getTotalDeaths()
        val soundEnabled = p.isSoundEnabled()
        val musicEnabled = p.isMusicEnabled()

        val progressMap = LevelData.levels.associate { level ->
            level.id to LevelProgress(
                levelId = level.id,
                isUnlocked = p.isLevelUnlocked(level.id),
                isCompleted = p.getLevelStars(level.id) > 0,
                stars = p.getLevelStars(level.id),
                bestTimeMs = p.getBestTimeMs(level.id)
            )
        }

        val achievementsUnlocked = LevelData.achievements.mapNotNull {
            if (p.isAchievementUnlocked(it.id)) it.id else null
        }.toSet()

        _uiState.update {
            it.copy(
                totalDeaths = totalDeaths,
                isSoundEnabled = soundEnabled,
                isMusicEnabled = musicEnabled,
                levelProgressMap = progressMap,
                unlockedAchievements = achievementsUnlocked
            )
        }
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
        val teleportersCopy = level.teleporters.map { it.copy() }
        val springsCopy = level.springs.map { it.copy() }
        val starsCopy = level.stars.map { it.copy() }
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
        levelStartTimeMs = System.currentTimeMillis()

        gravity = 0.45f
        jumpPower = -15.5f

        val bestTime = prefs?.getBestTimeMs(level.id) ?: 0L

        _uiState.update {
            it.copy(
                currentLevelIndex = safeIndex,
                currentLevel = level,
                player = playerStart,
                platforms = platformsCopy,
                spikes = spikesCopy,
                keys = keysCopy,
                teleporters = teleportersCopy,
                springs = springsCopy,
                stars = starsCopy,
                checkpoints = checkpointsCopy,
                door = doorCopy,
                particles = emptyList(),
                cameraX = 0f,
                cameraY = 0f,
                cameraShake = 0f,
                isDead = false,
                isLevelWon = false,
                isControlsInverted = false,
                isReverseGravity = false,
                tauntMessage = "",
                bannerText = "",
                bannerAlpha = 0f,
                elapsedTimeMs = 0L,
                collectedStarsInLevel = 0,
                earnedStars = 0,
                bestTimeMs = bestTime
            )
        }
    }

    fun restartCurrentLevel() {
        loadLevel(_uiState.value.currentLevelIndex)
    }

    fun nextLevel() {
        val nextIdx = _uiState.value.currentLevelIndex + 1
        loadLevel(nextIdx)
        setScreen(GameScreenState.PLAYING)
    }

    fun toggleSound() {
        val current = _uiState.value.isSoundEnabled
        val updated = !current
        prefs?.setSoundEnabled(updated)
        _uiState.update { it.copy(isSoundEnabled = updated) }
    }

    fun toggleMusic() {
        val current = _uiState.value.isMusicEnabled
        val updated = !current
        prefs?.setMusicEnabled(updated)
        _uiState.update { it.copy(isMusicEnabled = updated) }
    }

    fun clearAllProgress(context: Context) {
        prefs?.clearAllData()
        loadSavedPreferences()
        loadLevel(0)
    }

    fun showBanner(text: String, color: Color = Color(0xFFFFA502)) {
        bannerTicks = 90
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
            Key.P, Key.Escape -> {
                if (isDown && _uiState.value.screenState == GameScreenState.PLAYING) {
                    setScreen(GameScreenState.PAUSED)
                } else if (isDown && _uiState.value.screenState == GameScreenState.PAUSED) {
                    setScreen(GameScreenState.PLAYING)
                }
                return true
            }
        }
        return false
    }

    private fun respawnOrRestart() {
        val state = _uiState.value
        val checkpoint = activeCheckpointPos
        if (checkpoint != null) {
            val respawnPlayer = state.player.copy(
                x = checkpoint.first,
                y = checkpoint.second - state.player.height,
                vx = 0f,
                vy = 0f,
                isGrounded = false
            )
            _uiState.update {
                it.copy(
                    player = respawnPlayer,
                    isDead = false,
                    particles = emptyList()
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
                if (_uiState.value.screenState == GameScreenState.PLAYING && !_uiState.value.isDead && !_uiState.value.isLevelWon) {
                    tickGame()
                }
            }
        }
    }

    private fun tickGame() {
        val state = _uiState.value
        var player = state.player
        val platforms = state.platforms.map { it.copy() }
        val spikes = state.spikes.map { it.copy() }
        val keys = state.keys.map { it.copy() }
        val teleporters = state.teleporters.map { it.copy() }
        val springs = state.springs.map { it.copy() }
        val stars = state.stars.map { it.copy() }
        val checkpoints = state.checkpoints.map { it.copy() }
        var door = state.door.copy()
        var cameraX = state.cameraX
        var cameraY = state.cameraY
        var cameraShake = state.cameraShake
        var isReverseGravity = state.isReverseGravity
        var isControlsInverted = state.isControlsInverted
        var collectedStarsCount = state.collectedStarsInLevel

        val now = System.currentTimeMillis()
        val elapsedTime = now - levelStartTimeMs

        // Banner decay
        if (bannerTicks > 0) {
            bannerTicks--
            if (bannerTicks == 0) {
                _uiState.update { it.copy(bannerAlpha = 0f) }
            }
        }

        // Camera Shake Decay
        if (cameraShake > 0f) cameraShake *= 0.85f

        // Smooth Size Scaling
        if (player.width != player.targetWidth || player.height != player.targetHeight) {
            player.width += (player.targetWidth - player.width) * 0.15f
            player.height += (player.targetHeight - player.height) * 0.15f
        }

        // 1. Particle life update
        val updatedParticles = state.particles.mapNotNull { p ->
            p.x += p.vx
            p.y += p.vy
            p.alpha -= 0.03f
            if (p.alpha > 0f) p else null
        }.toMutableList()

        // 2. Platform Movement & Sinking Physics
        platforms.forEach { plat ->
            if (plat.trapType == TrapType.MOVING_PLATFORM) {
                plat.moveProgress += plat.moveSpeed * plat.moveDirection * 0.015f
                if (plat.moveProgress >= 1f) {
                    plat.moveProgress = 1f
                    plat.moveDirection = -1
                } else if (plat.moveProgress <= 0f) {
                    plat.moveProgress = 0f
                    plat.moveDirection = 1
                }
                plat.x = plat.startX + (plat.endX - plat.startX) * plat.moveProgress
                plat.y = plat.startY + (plat.endY - plat.startY) * plat.moveProgress
            } else if (plat.trapType == TrapType.SINKING_FLOOR && plat.isTriggered) {
                plat.vy += 0.4f
                plat.y += plat.vy
            }
        }

        // Teleporter Cooldowns
        teleporters.forEach { tp ->
            if (tp.cooldown > 0) tp.cooldown--
        }

        // Spring Pad Compression Decay
        springs.forEach { sp ->
            if (sp.compressionAnim > 0f) sp.compressionAnim = (sp.compressionAnim - 0.1f).coerceAtLeast(0f)
        }

        // 3. Proximity Traps (Disappearing Blocks, Spikes, Fleeing Keys & Door)
        val playerCenterX = player.x + player.width / 2f
        val playerCenterY = player.y + player.height / 2f

        platforms.forEach { plat ->
            if (plat.trapType == TrapType.DISAPPEAR_ON_APPROACH && plat.isVisible) {
                val dist = hypot(playerCenterX - (plat.x + plat.width / 2f), playerCenterY - (plat.y + plat.height / 2f))
                if (dist < plat.triggerDistance) plat.isVisible = false
            }
        }

        spikes.forEach { spike ->
            if (spike.isHiddenSpike) {
                val dist = hypot(playerCenterX - (spike.x + spike.width / 2f), playerCenterY - (spike.currentY + spike.height / 2f))
                if (dist < spike.triggerDistance) spike.currentY = spike.targetY
            }
        }

        // Fleeing Keys
        keys.forEach { key ->
            if (!key.isCollected && key.isMovingKey && !key.hasMoved) {
                val dist = hypot(playerCenterX - (key.x + key.width / 2f), playerCenterY - (key.y + key.height / 2f))
                if (dist < key.triggerDistance) {
                    key.x = key.targetX
                    key.hasMoved = true
                    if (state.isSoundEnabled) GameAudio.playKeyCollectSound()
                }
            }
        }

        // Fleeing Door
        if (door.isMovingDoor && !door.hasMoved) {
            val dist = hypot(playerCenterX - (door.x + door.width / 2f), playerCenterY - (door.y + door.height / 2f))
            if (dist < door.triggerDistance) {
                door.x = door.targetX
                door.hasMoved = true
                if (state.isSoundEnabled) GameAudio.playKeyCollectSound()
            }
        }

        // Trigger Zones
        state.currentLevel.triggerZones.forEach { zone ->
            if (zone.bounds.overlaps(player.bounds)) {
                when (zone.trapType) {
                    TrapType.INVERT_CONTROLS -> if (!isControlsInverted) {
                        isControlsInverted = true
                        showBanner("⚠️ CONTROLS INVERTED!")
                    }
                    TrapType.SIZE_GIANT -> if (!player.isGiant) {
                        player.targetWidth = 48f
                        player.targetHeight = 56f
                        showBanner(zone.bannerText.ifEmpty { "🔥 GIANT MODE!" })
                    }
                    TrapType.SIZE_TINY -> if (!player.isTiny) {
                        player.targetWidth = 14f
                        player.targetHeight = 16f
                        showBanner(zone.bannerText.ifEmpty { "🔬 TINY MODE!" })
                    }
                    TrapType.SIZE_NORMAL -> {
                        player.targetWidth = 24f
                        player.targetHeight = 28f
                        showBanner("⚡ NORMAL SIZE")
                    }
                    TrapType.GRAVITY_REVERSE -> if (!isReverseGravity) {
                        isReverseGravity = true
                        gravity = -0.45f
                        jumpPower = 15.5f
                        showBanner("🌌 REVERSE GRAVITY!")
                    }
                    TrapType.GRAVITY_NORMAL -> if (isReverseGravity) {
                        isReverseGravity = false
                        gravity = 0.45f
                        jumpPower = -15.5f
                        showBanner("⬇️ NORMAL GRAVITY")
                    }
                    else -> {}
                }
            }
        }

        // 4. Horizontal Input & Friction
        var moveDir = 0f
        if (leftInput) moveDir -= 1f
        if (rightInput) moveDir += 1f
        if (isControlsInverted) moveDir = -moveDir

        val targetVx = moveDir * moveSpeed

        // Ice Friction Check
        val onIce = platforms.any { plat ->
            plat.isVisible && plat.trapType == TrapType.SLIPPERY_ICE &&
                    player.x + player.width > plat.x && player.x < plat.x + plat.width &&
                    abs(player.y + player.height - plat.y) < 4f
        }

        val currentFriction = if (onIce) iceFriction else friction
        player.vx = player.vx * currentFriction + targetVx * accel

        if (moveDir > 0) player.facingRight = true
        else if (moveDir < 0) player.facingRight = false

        // Horizontal Movement + Platform Collision
        player.x += player.vx

        // Breakable Wall Smash (Giant Player)
        platforms.forEach { plat ->
            if (plat.isVisible && plat.bounds.overlaps(player.bounds)) {
                if (plat.isBreakable && player.isGiant) {
                    plat.isVisible = false
                    cameraShake = 12f
                    if (state.isSoundEnabled) GameAudio.playWallBreakSound()
                    repeat(12) {
                        updatedParticles.add(
                            Particle(
                                x = plat.x + plat.width / 2f, y = plat.y + plat.height / 2f,
                                vx = Random.nextFloat() * 6f - 3f, vy = Random.nextFloat() * 6f - 3f,
                                size = Random.nextFloat() * 6f + 4f, color = plat.color
                            )
                        )
                    }
                } else {
                    if (player.vx > 0) player.x = plat.x - player.width
                    else if (player.vx < 0) player.x = plat.x + plat.width
                    player.vx = 0f
                }
            }
        }

        // 5. Vertical Movement & Gravity
        if (coyoteTimer > 0) coyoteTimer--
        if (jumpBufferTimer > 0) jumpBufferTimer--

        if (player.isGrounded) coyoteTimer = 6

        // Variable Jump Height (releasing jump button cuts upward jump speed)
        if (!jumpInput && ((!isReverseGravity && player.vy < -4f) || (isReverseGravity && player.vy > 4f))) {
            player.vy *= 0.5f
        }

        if (jumpBufferTimer > 0 && coyoteTimer > 0) {
            player.vy = jumpPower
            player.isGrounded = false
            coyoteTimer = 0
            jumpBufferTimer = 0
            if (state.isSoundEnabled) GameAudio.playJumpSound()
        }

        player.vy += gravity
        player.y += player.vy
        player.isGrounded = false

        // Vertical Platform Collision
        platforms.forEach { plat ->
            if (plat.isVisible && plat.bounds.overlaps(player.bounds)) {
                if (!isReverseGravity) {
                    if (player.vy > 0 && player.y + player.height - player.vy <= plat.y + 12f) {
                        player.y = plat.y - player.height
                        player.vy = 0f
                        player.isGrounded = true

                        // Trigger Sinking Floor or Disappearing Step
                        if (plat.trapType == TrapType.SINKING_FLOOR) plat.isTriggered = true
                        else if (plat.trapType == TrapType.DISAPPEAR_ON_TOUCH && !plat.isTriggered) {
                            plat.isTriggered = true
                            viewModelScope.launch {
                                delay(180)
                                plat.isVisible = false
                            }
                        }
                    } else if (player.vy < 0 && player.y - player.vy >= plat.y + plat.height - 12f) {
                        player.y = plat.y + plat.height
                        player.vy = 0f
                    }
                } else { // Reverse Gravity (Ceiling Walking)
                    if (player.vy < 0 && player.y - player.vy >= plat.y + plat.height - 12f) {
                        player.y = plat.y + plat.height
                        player.vy = 0f
                        player.isGrounded = true
                    } else if (player.vy > 0 && player.y + player.height - player.vy <= plat.y + 12f) {
                        player.y = plat.y - player.height
                        player.vy = 0f
                    }
                }
            }
        }

        // 6. Spring Pads
        springs.forEach { sp ->
            if (sp.bounds.overlaps(player.bounds)) {
                sp.compressionAnim = 1f
                player.vy = sp.launchPower
                player.isGrounded = false
                if (state.isSoundEnabled) GameAudio.playSpringSound()
                repeat(8) {
                    updatedParticles.add(
                        Particle(
                            x = sp.x + sp.width / 2f, y = sp.y,
                            vx = Random.nextFloat() * 4f - 2f, vy = -Random.nextFloat() * 3f - 2f,
                            size = 4f, color = Color(0xFF2ED573)
                        )
                    )
                }
                unlockAchievement("spring_master")
            }
        }

        // 7. Teleporters
        teleporters.forEach { tp ->
            if (tp.cooldown == 0 && tp.bounds.overlaps(player.bounds)) {
                player.x = tp.targetX
                player.y = tp.targetY - player.height
                tp.cooldown = 45 // ~0.75s cooldown to avoid infinite loop
                if (state.isSoundEnabled) GameAudio.playTeleportSound()
                repeat(12) {
                    updatedParticles.add(
                        Particle(
                            x = tp.targetX, y = tp.targetY,
                            vx = Random.nextFloat() * 4f - 2f, vy = Random.nextFloat() * 4f - 2f,
                            size = 5f, color = Color(0xFF00D2D3)
                        )
                    )
                }
                unlockAchievement("portal_hopper")
            }
        }

        // 8. Collectible Stars
        stars.forEach { star ->
            if (!star.isCollected && star.bounds.overlaps(player.bounds)) {
                star.isCollected = true
                collectedStarsCount++
                if (state.isSoundEnabled) GameAudio.playStarSound()
                repeat(10) {
                    updatedParticles.add(
                        Particle(
                            x = star.x + star.width / 2f, y = star.y + star.height / 2f,
                            vx = Random.nextFloat() * 5f - 2.5f, vy = Random.nextFloat() * 5f - 2.5f,
                            size = 5f, color = Color(0xFFFFD700)
                        )
                    )
                }
            }
        }

        // 9. Key Collection & Unlocking Door
        keys.forEach { key ->
            if (!key.isCollected && key.bounds.overlaps(player.bounds)) {
                key.isCollected = true
                if (state.isSoundEnabled) GameAudio.playKeyCollectSound()
                showBanner("🔑 KEY COLLECTED!")
                unlockAchievement("key_master")
            }
        }

        // Unlock Door if all keys collected
        if (door.isLocked && keys.all { it.isCollected }) {
            door.isLocked = false
            showBanner("🔓 DOOR UNLOCKED!")
        }

        // 10. Checkpoints
        checkpoints.forEach { chk ->
            if (!chk.isActivated && chk.bounds.overlaps(player.bounds)) {
                chk.isActivated = true
                activeCheckpointPos = Pair(chk.x, chk.y)
                if (state.isSoundEnabled) GameAudio.playCheckpointSound()
                showBanner("🚩 CHECKPOINT REACHED!")
            }
        }

        // 11. Death Check (Spikes, Abyss Fall, Ceiling Crushing)
        var playerDied = false
        spikes.forEach { spike ->
            if (spike.isVisible && spike.bounds.overlaps(player.bounds)) playerDied = true
        }

        // Fall into Abyss
        if (player.y > 450f || player.y < -200f) playerDied = true

        if (playerDied) {
            triggerPlayerDeath()
            return
        }

        // 12. Door / Exit Goal Reach Check
        if (!door.isLocked && door.bounds.overlaps(player.bounds)) {
            triggerLevelVictory(elapsedTime, collectedStarsCount)
            return
        }

        // 13. Camera Follow
        val targetCamX = (player.x - 220f).coerceIn(0f, (state.currentLevel.worldWidth - 650f).coerceAtLeast(0f))
        val targetCamY = (player.y - 180f).coerceIn(-100f, 200f)
        cameraX += (targetCamX - cameraX) * 0.12f
        cameraY += (targetCamY - cameraY) * 0.12f

        _uiState.update {
            it.copy(
                player = player,
                platforms = platforms,
                spikes = spikes,
                keys = keys,
                teleporters = teleporters,
                springs = springs,
                stars = stars,
                door = door,
                particles = updatedParticles,
                cameraX = cameraX,
                cameraY = cameraY,
                cameraShake = cameraShake,
                isReverseGravity = isReverseGravity,
                isControlsInverted = isControlsInverted,
                elapsedTimeMs = elapsedTime,
                collectedStarsInLevel = collectedStarsCount
            )
        }
    }

    private fun triggerPlayerDeath() {
        val state = _uiState.value
        if (state.isDead) return

        if (state.isSoundEnabled) GameAudio.playDeathSound()

        val newDeaths = state.totalDeaths + 1
        prefs?.setTotalDeaths(newDeaths)
        val randomTaunt = LevelData.funnyTaunts.random()

        if (newDeaths >= 1) unlockAchievement("first_death")

        _uiState.update {
            it.copy(
                isDead = true,
                totalDeaths = newDeaths,
                tauntMessage = randomTaunt,
                cameraShake = 15f
            )
        }
    }

    private fun triggerLevelVictory(timeMs: Long, starsCollected: Int) {
        val state = _uiState.value
        if (state.isSoundEnabled) GameAudio.playWinSound()

        val currId = state.currentLevel.id
        val nextId = currId + 1

        // Calculate Stars Earned (1 to 3 ⭐)
        var stars = 1 // 1 Star for clearing
        if (timeMs <= state.currentLevel.parTimeMs || state.currentLevel.spikes.none { state.isDead }) stars++
        if (starsCollected >= state.currentLevel.stars.size && state.currentLevel.stars.isNotEmpty()) stars++
        stars = stars.coerceIn(1, 3)

        // Save progress to Preferences
        prefs?.unlockLevel(nextId)
        prefs?.setLevelStars(currId, stars)
        prefs?.setBestTimeMs(currId, timeMs)

        // Achievement Triggers
        if (timeMs <= state.currentLevel.parTimeMs) unlockAchievement("speed_demon")
        if (currId == 5) unlockAchievement("level_5")
        if (currId == 10) unlockAchievement("level_10")
        if (currId == 16) unlockAchievement("level_16")
        if (currId == 20) unlockAchievement("level_20")

        val updatedMap = state.levelProgressMap.toMutableMap()
        updatedMap[currId] = LevelProgress(currId, isUnlocked = true, isCompleted = true, stars = stars, bestTimeMs = timeMs)
        updatedMap[nextId] = LevelProgress(nextId, isUnlocked = true, isCompleted = false)

        _uiState.update {
            it.copy(
                isLevelWon = true,
                earnedStars = stars,
                bestTimeMs = timeMs,
                levelProgressMap = updatedMap
            )
        }
    }

    private fun unlockAchievement(id: String) {
        if (!_uiState.value.unlockedAchievements.contains(id)) {
            prefs?.unlockAchievement(id)
            val updated = _uiState.value.unlockedAchievements + id
            _uiState.update { it.copy(unlockedAchievements = updated) }
            val ach = LevelData.achievements.find { it.id == id }
            if (ach != null) {
                showBanner("🏆 UNLOCKED: ${ach.title}!", Color(0xFFFFD700))
            }
        }
    }

    fun triggerVibration(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        } catch (_: Exception) {}
    }
}

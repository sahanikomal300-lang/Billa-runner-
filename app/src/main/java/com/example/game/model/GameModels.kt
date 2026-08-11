package com.example.game.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color

enum class TrapType {
    NONE,
    DISAPPEAR_ON_APPROACH, // Block vanishes when player gets close
    DISAPPEAR_ON_TOUCH,    // Block vanishes shortly after player steps on it
    SPROUT_SPIKE,          // Spike shoots up when player gets close
    FALLING_BLOCK,         // Block drops down when player walks under it
    MOVING_DOOR,           // Goal door slides/teleports away when player approaches
    INVERT_CONTROLS,       // Stepping on this flips Left/Right controls
    FAKE_DOOR,             // Door disappears when touched, revealing real door elsewhere
    SIZE_GIANT,            // Character grows giant
    SIZE_TINY,             // Character shrinks tiny
    SIZE_NORMAL,           // Resets size to normal
    GRAVITY_LOW,           // Low gravity (jump higher)
    GRAVITY_HIGH,          // High gravity (fall faster)
    GRAVITY_REVERSE,       // Reverses gravity (ceiling walking)
    GRAVITY_NORMAL,        // Normal gravity
    MOVING_PLATFORM,       // Platform slides back and forth
    BREAKABLE_WALL,        // Giant player can break this wall
    SINKING_FLOOR          // Platform sinks into the abyss when stepped on
}

data class LevelKey(
    val id: Int,
    var x: Float,
    var y: Float,
    var width: Float = 20f,
    var height: Float = 20f,
    var isCollected: Boolean = false,
    var isMovingKey: Boolean = false,
    var targetX: Float = x,
    var triggerDistance: Float = 90f,
    var hasMoved: Boolean = false
) {
    val bounds: Rect
        get() = Rect(x, y, x + width, y + height)
}

data class Player(
    var x: Float,
    var y: Float,
    var width: Float = 24f,
    var height: Float = 28f,
    var targetWidth: Float = 24f,
    var targetHeight: Float = 28f,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var isGrounded: Boolean = false,
    var facingRight: Boolean = true
) {
    val bounds: Rect
        get() = Rect(x, y, x + width, y + height)

    val isGiant: Boolean get() = targetWidth > 30f
    val isTiny: Boolean get() = targetWidth < 18f
}

data class Platform(
    val id: Int,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float = 20f,
    val color: Color = Color(0xFF3742FA),
    val trapType: TrapType = TrapType.NONE,
    var isVisible: Boolean = true,
    var isTriggered: Boolean = false,
    var triggerDistance: Float = 110f,
    var vy: Float = 0f,
    // Moving Platform Properties
    var startX: Float = x,
    var startY: Float = y,
    var endX: Float = x,
    var endY: Float = y,
    var moveSpeed: Float = 1.5f,
    var moveProgress: Float = 0f,
    var moveDirection: Int = 1,
    var isBreakable: Boolean = false
) {
    val bounds: Rect
        get() = Rect(x, y, x + width, y + height)
}

data class Spike(
    val id: Int,
    var x: Float,
    var y: Float,
    var width: Float = 16f,
    var height: Float = 16f,
    var isVisible: Boolean = true,
    var isHiddenSpike: Boolean = false,
    var isCeilingSpike: Boolean = false,
    var triggerDistance: Float = 95f,
    var targetY: Float = y,
    var currentY: Float = if (isHiddenSpike) y + 24f else y
) {
    val bounds: Rect
        get() = Rect(x, currentY, x + width, currentY + height)
}

data class Door(
    var id: Int = 1,
    var x: Float,
    var y: Float,
    var width: Float = 28f,
    var height: Float = 38f,
    var isTarget: Boolean = true,
    var isMovingDoor: Boolean = false,
    var isFakeDoor: Boolean = false,
    var isLocked: Boolean = false,
    var targetX: Float = x,
    var hasMoved: Boolean = false,
    var isVisible: Boolean = true,
    var triggerDistance: Float = 100f
) {
    val bounds: Rect
        get() = Rect(x, y, x + width, y + height)
}

data class Checkpoint(
    val id: Int,
    val x: Float,
    val y: Float,
    val width: Float = 24f,
    val height: Float = 36f,
    var isActivated: Boolean = false
) {
    val bounds: Rect
        get() = Rect(x, y, x + width, y + height)
}

data class TriggerZone(
    val id: Int,
    val bounds: Rect,
    val trapType: TrapType,
    val bannerText: String = "",
    var isTriggered: Boolean = false,
    var isOneShot: Boolean = true
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var color: Color,
    var alpha: Float = 1f,
    var life: Float = 1f
)

data class Level(
    val id: Int,
    val name: String,
    val subtitle: String,
    val playerStart: Pair<Float, Float>,
    val platforms: List<Platform>,
    val spikes: List<Spike>,
    val door: Door,
    val keys: List<LevelKey> = emptyList(),
    val checkpoints: List<Checkpoint> = emptyList(),
    val triggerZones: List<TriggerZone> = emptyList(),
    val worldWidth: Float = 1200f,
    val hint: String = ""
)

data class GameStats(
    val levelIndex: Int = 0,
    val deathCount: Int = 0,
    val completedLevels: Set<Int> = emptySet(),
    val isControlsInverted: Boolean = false,
    val funnyTaunt: String = ""
)

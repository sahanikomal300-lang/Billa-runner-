package com.example.game.data

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import com.example.game.model.Achievement
import com.example.game.model.Checkpoint
import com.example.game.model.CollectibleStar
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.LevelKey
import com.example.game.model.Platform
import com.example.game.model.Spike
import com.example.game.model.SpringPad
import com.example.game.model.Teleporter
import com.example.game.model.TrapType
import com.example.game.model.TriggerZone

object LevelData {

    val funnyTaunts = listOf(
        "Gravity said NO! 💀",
        "Classic Level Devil trap! 😂",
        "Trust issues activated! 🤡",
        "Nice try, human! 🤖",
        "That platform was a lie! 🧱",
        "Did you really fall for that? 🙈",
        "The key ran away! 🔑💨",
        "Physics left the chat! 🌌",
        "Skill issue detected! 🎮",
        "Spikes: 'Surprise!' 🗡️",
        "One does not simply walk to the exit! 🚪",
        "Sinking floor strikes again! ⏳",
        "Gravity reversed your brain! 🙃",
        "The door is playing hide & seek! 🚪💨"
    )

    val achievements = listOf(
        Achievement("first_death", "First Blood", "Die for the first time in any level", "💀"),
        Achievement("level_5", "Getting Warm", "Clear Level 5", "🔥"),
        Achievement("level_10", "Troll Survivor", "Clear Level 10", "🛡️"),
        Achievement("level_16", "Devil Master", "Clear Level 16", "😈"),
        Achievement("level_20", "Grand Champion", "Clear Level 20", "👑"),
        Achievement("key_master", "Key Master", "Catch a fleeing key to unlock the door", "🔑"),
        Achievement("portal_hopper", "Portal Hopper", "Teleport through a portal loop", "🌀"),
        Achievement("spring_master", "Spring Hopper", "Launch off a spring pad", "🚀"),
        Achievement("speed_demon", "Speed Demon", "Clear any level under Par Time", "⚡"),
        Achievement("star_collector", "Star Hunter", "Collect 10 total stars across levels", "⭐")
    )

    val levels = listOf(
        // LEVEL 1: Looks Easy (Easy)
        Level(
            id = 1,
            name = "Level 1: Looks Easy",
            subtitle = "Just walk to the exit... or can you?",
            playerStart = Pair(50f, 220f),
            worldWidth = 1000f,
            parTimeMs = 8000L,
            difficultyRating = "Easy",
            platforms = listOf(
                Platform(1, 0f, 250f, 220f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 250f, 250f, 150f, 60f, Color(0xFF3742FA),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 110f
                ),
                Platform(3, 280f, 285f, 90f, 20f, Color(0xFFE91E63)),
                Platform(4, 520f, 250f, 280f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 220f, 296f, 300f, 16f)
            ),
            stars = listOf(
                CollectibleStar(1, 310f, 250f),
                CollectibleStar(2, 600f, 200f)
            ),
            door = Door(id = 1, x = 730f, y = 212f, width = 28f, height = 38f),
            hint = "Beware: What you see isn't always solid!"
        ),

        // LEVEL 2: Watch Your Step (Easy)
        Level(
            id = 2,
            name = "Level 2: Watch Your Step",
            subtitle = "Don't rush forward blindly!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            parTimeMs = 10000L,
            difficultyRating = "Easy",
            platforms = listOf(
                Platform(1, 0f, 250f, 1000f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 220f, 234f, 24f, 16f),
                Spike(2, 340f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 90f),
                Spike(3, 480f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 90f),
                Spike(4, 620f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 95f),
                Spike(5, 760f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 95f)
            ),
            stars = listOf(
                CollectibleStar(1, 400f, 200f),
                CollectibleStar(2, 700f, 200f)
            ),
            door = Door(id = 1, x = 920f, y = 212f, width = 28f, height = 38f),
            hint = "Spikes can jump out of nowhere!"
        ),

        // LEVEL 3: Shy Door (Normal)
        Level(
            id = 3,
            name = "Level 3: Shy Door",
            subtitle = "Reach the exit... if it doesn't move!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            parTimeMs = 10000L,
            difficultyRating = "Normal",
            platforms = listOf(
                Platform(1, 0f, 250f, 1000f, 60f, Color(0xFF3742FA)),
                Platform(2, 400f, 190f, 80f, 60f, Color(0xFF5F27CD))
            ),
            spikes = listOf(
                Spike(1, 400f, 174f, 80f, 16f)
            ),
            stars = listOf(
                CollectibleStar(1, 250f, 200f),
                CollectibleStar(2, 750f, 200f)
            ),
            door = Door(
                id = 1, x = 520f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 900f,
                triggerDistance = 100f
            ),
            hint = "The exit might run away from you!"
        ),

        // LEVEL 4: Mind Flip (Normal)
        Level(
            id = 4,
            name = "Level 4: Mind Flip",
            subtitle = "Cross the toxic gap with inverted brain!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            parTimeMs = 12000L,
            difficultyRating = "Normal",
            platforms = listOf(
                Platform(1, 0f, 250f, 220f, 60f, Color(0xFF009688)),
                Platform(
                    2, 280f, 220f, 140f, 90f, Color(0xFFFF5722),
                    trapType = TrapType.INVERT_CONTROLS, triggerDistance = 85f
                ),
                Platform(3, 490f, 250f, 300f, 60f, Color(0xFF009688))
            ),
            spikes = listOf(
                Spike(1, 220f, 296f, 270f, 16f)
            ),
            stars = listOf(
                CollectibleStar(1, 350f, 170f)
            ),
            door = Door(id = 1, x = 680f, y = 212f, width = 28f, height = 38f),
            hint = "Left becomes Right! Right becomes Left!"
        ),

        // LEVEL 5: Heavy Sky (Normal)
        Level(
            id = 5,
            name = "Level 5: Heavy Sky",
            subtitle = "Ceiling blocks are falling down!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            parTimeMs = 12000L,
            difficultyRating = "Normal",
            platforms = listOf(
                Platform(1, 0f, 250f, 1000f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 280f, 40f, 80f, 60f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 100f
                ),
                Platform(
                    3, 480f, 40f, 80f, 60f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 100f
                )
            ),
            spikes = listOf(
                Spike(1, 620f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 90f)
            ),
            stars = listOf(
                CollectibleStar(1, 320f, 180f),
                CollectibleStar(2, 520f, 180f)
            ),
            door = Door(id = 1, x = 850f, y = 212f, width = 28f, height = 38f),
            hint = "Trigger the falling block then back up!"
        ),

        // LEVEL 6: Crumbling Steps (Normal)
        Level(
            id = 6,
            name = "Level 6: Crumbling Steps",
            subtitle = "Don't pause on the disappearing steps!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1300f,
            parTimeMs = 14000L,
            difficultyRating = "Normal",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 220f, 220f, 70f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(
                    3, 330f, 190f, 70f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(4, 440f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint Island
                Platform(
                    5, 590f, 220f, 70f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(6, 700f, 250f, 300f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 520f, 16f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 470f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 255f, 180f),
                CollectibleStar(2, 625f, 180f)
            ),
            door = Door(id = 1, x = 880f, y = 212f, width = 28f, height = 38f),
            hint = "Step and jump quickly before the platform dissolves!"
        ),

        // LEVEL 7: Size Shifter (Hard)
        Level(
            id = 7,
            name = "Level 7: Size Shifter",
            subtitle = "Grow giant to smash walls, shrink tiny to crawl!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1400f,
            parTimeMs = 15000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF3742FA)),
                Platform(2, 280f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true), // Wall
                Platform(3, 200f, 250f, 280f, 60f, Color(0xFFD32F2F)),
                Platform(4, 480f, 250f, 120f, 60f, Color(0xFF2ED573)), // Checkpoint
                Platform(5, 600f, 210f, 180f, 20f, Color(0xFF9C27B0)), // Low ceiling tunnel
                Platform(6, 600f, 250f, 400f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 200f, 296f, 280f, 16f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(120f, 180f, 160f, 250f), TrapType.SIZE_GIANT, "🔥 GIANT MODE!"),
                TriggerZone(2, Rect(500f, 180f, 540f, 250f), TrapType.SIZE_TINY, "🔬 TINY MODE!"),
                TriggerZone(3, Rect(820f, 180f, 860f, 250f), TrapType.SIZE_NORMAL, "⚡ SIZE RESTORED")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 520f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 140f, 190f),
                CollectibleStar(2, 680f, 225f)
            ),
            door = Door(id = 1, x = 880f, y = 212f, width = 28f, height = 38f),
            hint = "Giant player breaks red walls! Tiny player fits under violet ceilings!"
        ),

        // LEVEL 8: Anti-Gravity (Hard)
        Level(
            id = 8,
            name = "Level 8: Anti-Gravity",
            subtitle = "Flip gravity and walk on the ceiling!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1400f,
            parTimeMs = 15000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF3742FA)),
                Platform(2, 240f, 30f, 400f, 25f, Color(0xFF9C27B0)), // Ceiling runway
                Platform(3, 680f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 200f, 296f, 480f, 16f), // Floor spikes
                Spike(2, 380f, 55f, 60f, 16f, isCeilingSpike = true) // Ceiling spikes
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(180f, 180f, 220f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(2, Rect(600f, 30f, 640f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED")
            ),
            stars = listOf(
                CollectibleStar(1, 300f, 70f),
                CollectibleStar(2, 520f, 70f)
            ),
            door = Door(id = 1, x = 820f, y = 212f, width = 28f, height = 38f),
            hint = "Jump up to flip onto the ceiling, jump down to flip back!"
        ),

        // LEVEL 9: Double Combo (Hard)
        Level(
            id = 9,
            name = "Level 9: Double Combo",
            subtitle = "Moving platforms + gravity flips!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1500f,
            parTimeMs = 16000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 220f, 220f, 90f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 220f, startY = 220f, endX = 420f, endY = 220f, moveSpeed = 2f
                ),
                Platform(3, 540f, 30f, 300f, 25f, Color(0xFF9C27B0)),
                Platform(4, 880f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 700f, 16f),
                Spike(2, 650f, 55f, 60f, 16f, isCeilingSpike = true)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(480f, 180f, 520f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(2, Rect(820f, 30f, 860f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED")
            ),
            stars = listOf(
                CollectibleStar(1, 320f, 180f),
                CollectibleStar(2, 700f, 70f)
            ),
            door = Door(id = 1, x = 1000f, y = 212f, width = 28f, height = 38f),
            hint = "Time your jump from the moving platform into the gravity zone!"
        ),

        // LEVEL 10: Devil's Gauntlet (Hard)
        Level(
            id = 10,
            name = "Level 10: Devil's Gauntlet",
            subtitle = "A multi-stage obstacle course testing all your skills!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1800f,
            parTimeMs = 18000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 210f, 220f, 70f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 100f
                ),
                Platform(
                    3, 310f, 200f, 70f, 18f, Color(0xFFFF5722),
                    trapType = TrapType.INVERT_CONTROLS
                ),
                Platform(4, 420f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 1
                Platform(
                    5, 560f, 220f, 80f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 560f, startY = 220f, endX = 760f, endY = 220f, moveSpeed = 2.2f
                ),
                Platform(6, 880f, 250f, 120f, 60f, Color(0xFF2ED573)), // Checkpoint 2
                Platform(7, 1040f, 250f, 250f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 700f, 16f),
                Spike(2, 1080f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 90f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 450f, y = 184f),
                Checkpoint(2, x = 910f, y = 214f)
            ),
            stars = listOf(
                CollectibleStar(1, 345f, 160f),
                CollectibleStar(2, 660f, 170f)
            ),
            door = Door(id = 1, x = 1200f, y = 212f, width = 28f, height = 38f),
            hint = "Memorize the trap order: Disappear -> Invert -> Moving -> Hidden Spike!"
        ),

        // LEVEL 11: Troll Mode (Insane)
        Level(
            id = 11,
            name = "Level 11: Troll Mode",
            subtitle = "Giant smash -> Reverse gravity -> Tiny crawl -> Moving platform!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2000f,
            parTimeMs = 20000L,
            difficultyRating = "Insane",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(2, 220f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true), // Wall
                Platform(3, 180f, 250f, 250f, 60f, Color(0xFFD32F2F)),
                Platform(4, 430f, 250f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 1
                Platform(5, 560f, 30f, 380f, 25f, Color(0xFF9C27B0)),  // Ceiling runway
                Platform(6, 960f, 250f, 110f, 60f, Color(0xFF2ED573)),  // Checkpoint 2
                Platform(
                    7, 1100f, 220f, 80f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 1100f, startY = 220f, endX = 1320f, endY = 220f, moveSpeed = 2.5f
                ),
                Platform(8, 1420f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 250f, 16f),
                Spike(2, 540f, 296f, 420f, 16f),
                Spike(3, 720f, 55f, 60f, 16f, isCeilingSpike = true),
                Spike(4, 1070f, 296f, 350f, 16f),
                Spike(5, 1480f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(100f, 180f, 140f, 250f), TrapType.SIZE_GIANT, "🔥 GIANT MODE!"),
                TriggerZone(2, Rect(480f, 180f, 520f, 250f), TrapType.SIZE_TINY, "🔬 TINY MODE!"),
                TriggerZone(3, Rect(520f, 180f, 560f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(4, Rect(900f, 30f, 940f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED"),
                TriggerZone(5, Rect(940f, 180f, 980f, 250f), TrapType.SIZE_NORMAL, "⚡ SIZE RESTORED")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 450f, y = 184f),
                Checkpoint(2, x = 980f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 120f, 180f),
                CollectibleStar(2, 700f, 70f)
            ),
            door = Door(
                id = 1, x = 1580f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1640f,
                triggerDistance = 70f
            ),
            hint = "Giant Smash -> Reverse Gravity -> Tiny Crawl -> Moving Platform!"
        ),

        // LEVEL 12: IMPOSSIBLE? 🔥💀 (Insane)
        Level(
            id = 12,
            name = "Level 12: IMPOSSIBLE? 🔥💀",
            subtitle = "Extreme precision gauntlet with 3 checkpoints!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2200f,
            parTimeMs = 22000L,
            difficultyRating = "Insane",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(
                    2, 210f, 220f, 60f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 90f
                ),
                Platform(
                    3, 300f, 200f, 70f, 18f, Color(0xFFFF5722),
                    trapType = TrapType.INVERT_CONTROLS
                ),
                Platform(
                    4, 330f, 40f, 70f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 90f
                ),
                Platform(5, 410f, 220f, 110f, 80f, Color(0xFF2ED573)),
                Platform(6, 520f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(7, 560f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true),
                Platform(
                    8, 730f, 220f, 65f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(9, 820f, 250f, 120f, 60f, Color(0xFF2ED573)),
                Platform(10, 930f, 30f, 450f, 25f, Color(0xFF9C27B0)),
                Platform(11, 1420f, 250f, 120f, 60f, Color(0xFF2ED573)),
                Platform(
                    12, 1570f, 220f, 90f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 1570f, startY = 220f, endX = 1790f, endY = 220f, moveSpeed = 2.8f
                ),
                Platform(13, 1850f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 230f, 16f),
                Spike(2, 520f, 296f, 300f, 16f),
                Spike(3, 920f, 296f, 500f, 16f),
                Spike(4, 1100f, 55f, 80f, 16f, isCeilingSpike = true),
                Spike(5, 1540f, 296f, 310f, 16f),
                Spike(6, 1890f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(420f, 150f, 480f, 220f), TrapType.SIZE_GIANT, "🔥 GIANT SMASH!"),
                TriggerZone(2, Rect(840f, 180f, 880f, 250f), TrapType.SIZE_TINY, "🔬 TINY INFILTRATION!"),
                TriggerZone(3, Rect(880f, 180f, 920f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY TUNNEL!"),
                TriggerZone(4, Rect(1360f, 30f, 1400f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED!"),
                TriggerZone(5, Rect(1400f, 180f, 1440f, 250f), TrapType.SIZE_NORMAL, "⚡ SIZE RESTORED!")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 440f, y = 184f),
                Checkpoint(2, x = 860f, y = 214f),
                Checkpoint(3, x = 1460f, y = 214f)
            ),
            stars = listOf(
                CollectibleStar(1, 320f, 150f),
                CollectibleStar(2, 1150f, 70f)
            ),
            door = Door(
                id = 1, x = 2000f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 2060f,
                triggerDistance = 75f
            ),
            hint = "Devil's Masterpiece: Master invert jump -> Giant smash -> Tiny gravity crawl -> Fast ride!"
        ),

        // LEVEL 13: The Golden Key 🔑 (Hard)
        Level(
            id = 13,
            name = "Level 13: The Golden Key 🔑",
            subtitle = "Collect the key to unlock the door... if it doesn't flee!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1400f,
            parTimeMs = 15000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 230f, 220f, 80f, 18f, Color(0xFFFF9800), trapType = TrapType.DISAPPEAR_ON_TOUCH),
                Platform(3, 360f, 220f, 100f, 60f, Color(0xFF2ED573)),
                Platform(4, 520f, 200f, 90f, 18f, Color(0xFF3742FA)),
                Platform(5, 660f, 220f, 90f, 18f, Color(0xFFFF5722), trapType = TrapType.INVERT_CONTROLS),
                Platform(6, 800f, 200f, 100f, 18f, Color(0xFF3742FA)),
                Platform(7, 950f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 770f, 16f),
                Spike(2, 680f, 204f, 24f, 16f, isHiddenSpike = true, triggerDistance = 80f)
            ),
            keys = listOf(
                LevelKey(1, x = 550f, y = 175f, isMovingKey = true, targetX = 830f, triggerDistance = 90f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 400f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 560f, 150f)
            ),
            door = Door(id = 1, x = 1120f, y = 212f, isLocked = true),
            hint = "Reach the key before it flees, then unlock the green exit!"
        ),

        // LEVEL 14: Sinking Sands ⏳ (Hard)
        Level(
            id = 14,
            name = "Level 14: Sinking Sands ⏳",
            subtitle = "Don't linger! Every platform sinks into the abyss!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1500f,
            parTimeMs = 15000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 160f, 60f, Color(0xFF3742FA)),
                Platform(2, 210f, 240f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(3, 320f, 220f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(4, 430f, 200f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(5, 540f, 220f, 110f, 60f, Color(0xFF2ED573)),
                Platform(6, 690f, 220f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(7, 800f, 200f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(8, 910f, 180f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(9, 1050f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 160f, 296f, 890f, 16f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 580f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 350f, 170f),
                CollectibleStar(2, 830f, 150f)
            ),
            door = Door(id = 1, x = 1200f, y = 212f),
            hint = "Keep hopping! Standing still means falling into spikes!"
        ),

        // LEVEL 15: Teleporting Portal 🚪 (Insane)
        Level(
            id = 15,
            name = "Level 15: Teleporting Portal 🚪",
            subtitle = "The door is playing hide & seek!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1600f,
            parTimeMs = 16000L,
            difficultyRating = "Insane",
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF3742FA)),
                Platform(2, 240f, 220f, 80f, 18f, Color(0xFFFF9800), trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 100f),
                Platform(3, 370f, 220f, 110f, 60f, Color(0xFF2ED573)),
                Platform(4, 520f, 220f, 90f, 18f, Color(0xFF009688), trapType = TrapType.MOVING_PLATFORM, startX = 520f, endX = 720f, moveSpeed = 2f),
                Platform(5, 850f, 220f, 100f, 18f, Color(0xFFFF5722), trapType = TrapType.INVERT_CONTROLS),
                Platform(6, 1000f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 200f, 296f, 800f, 16f),
                Spike(2, 1050f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 410f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 620f, 170f)
            ),
            door = Door(
                id = 1, x = 600f, y = 182f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1200f,
                triggerDistance = 80f
            ),
            hint = "Don't jump directly at the first door location!"
        ),

        // LEVEL 16: Devil's Ultimate Gauntlet 😈 (Extreme)
        Level(
            id = 16,
            name = "Level 16: Devil's Gauntlet 😈",
            subtitle = "The master troll level! Sinking floors, keys, reverse gravity & moving doors!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2400f,
            parTimeMs = 25000L,
            difficultyRating = "Extreme",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 220f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(3, 320f, 200f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(4, 430f, 220f, 110f, 60f, Color(0xFF2ED573)),
                Platform(5, 580f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(6, 620f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true),
                Platform(7, 780f, 250f, 110f, 60f, Color(0xFF2ED573)),
                Platform(8, 920f, 30f, 400f, 25f, Color(0xFF9C27B0)),
                Platform(9, 1350f, 250f, 110f, 60f, Color(0xFF2ED573)),
                Platform(10, 1500f, 220f, 80f, 18f, Color(0xFF009688), trapType = TrapType.MOVING_PLATFORM, startX = 1500f, endX = 1750f, moveSpeed = 3f),
                Platform(11, 1880f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 250f, 16f),
                Spike(2, 540f, 296f, 240f, 16f),
                Spike(3, 890f, 296f, 460f, 16f),
                Spike(4, 1050f, 55f, 60f, 16f, isCeilingSpike = true),
                Spike(5, 1460f, 296f, 420f, 16f),
                Spike(6, 1950f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f)
            ),
            keys = listOf(
                LevelKey(1, x = 335f, y = 160f, isMovingKey = true, targetX = 460f, triggerDistance = 85f),
                LevelKey(2, x = 1100f, y = 65f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(440f, 150f, 480f, 220f), TrapType.SIZE_GIANT, "🔥 GIANT MODE!"),
                TriggerZone(2, Rect(800f, 180f, 840f, 250f), TrapType.SIZE_TINY, "🔬 TINY MODE!"),
                TriggerZone(3, Rect(840f, 180f, 880f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(4, Rect(1300f, 30f, 1340f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMAL"),
                TriggerZone(5, Rect(1340f, 180f, 1380f, 250f), TrapType.SIZE_NORMAL, "⚡ SIZE RESTORED")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 460f, y = 184f),
                Checkpoint(2, x = 810f, y = 184f),
                Checkpoint(3, x = 1380f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 350f, 130f),
                CollectibleStar(2, 1120f, 70f)
            ),
            door = Door(
                id = 1, x = 2050f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 2120f,
                isLocked = true,
                triggerDistance = 75f
            ),
            hint = "Master all 16 levels: Sinking steps, fleeing key, giant smash, upside-down ceiling walk & moving exit!"
        ),

        // LEVEL 17: Teleporter Trouble 🌀 (Hard)
        Level(
            id = 17,
            name = "Level 17: Teleporter Loop 🌀",
            subtitle = "Jump into glowing portals to warp across toxic chasms!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1600f,
            parTimeMs = 15000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 200f, 90f, 18f, Color(0xFF3742FA)),
                Platform(3, 400f, 250f, 120f, 60f, Color(0xFF2ED573)), // Checkpoint 1
                Platform(4, 580f, 200f, 90f, 18f, Color(0xFF3742FA)),
                Platform(5, 800f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 620f, 16f)
            ),
            teleporters = listOf(
                Teleporter(1, x = 250f, y = 160f, targetX = 420f, targetY = 210f),
                Teleporter(2, x = 610f, y = 160f, targetX = 850f, targetY = 210f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 440f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 260f, 130f),
                CollectibleStar(2, 620f, 130f)
            ),
            door = Door(id = 1, x = 1000f, y = 212f),
            hint = "Step into cyan portal rings to teleport instantly across giant gaps!"
        ),

        // LEVEL 18: Spring Jump City 🚀 (Hard)
        Level(
            id = 18,
            name = "Level 18: Spring Jump City 🚀",
            subtitle = "Bounce off mechanical spring pads high into the sky!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1600f,
            parTimeMs = 16000L,
            difficultyRating = "Hard",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 250f, 80f, 60f, Color(0xFF3742FA)),
                Platform(3, 400f, 140f, 100f, 18f, Color(0xFF2ED573)),
                Platform(4, 580f, 250f, 80f, 60f, Color(0xFF3742FA)),
                Platform(5, 780f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            springs = listOf(
                SpringPad(1, x = 240f, y = 236f, launchPower = -22f),
                SpringPad(2, x = 600f, y = 236f, launchPower = -24f)
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 600f, 16f)
            ),
            stars = listOf(
                CollectibleStar(1, 440f, 100f),
                CollectibleStar(2, 850f, 200f)
            ),
            door = Door(id = 1, x = 980f, y = 212f),
            hint = "Land directly on top of green spring pads to launch high!"
        ),

        // LEVEL 19: Ice & Friction 🧊 (Insane)
        Level(
            id = 19,
            name = "Level 19: Slippery Ice 🧊",
            subtitle = "Low-friction icy platforms over deep spike chasms!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1600f,
            parTimeMs = 16000L,
            difficultyRating = "Insane",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 220f, 120f, 18f, Color(0xFF74B9FF), trapType = TrapType.SLIPPERY_ICE),
                Platform(3, 400f, 200f, 120f, 18f, Color(0xFF74B9FF), trapType = TrapType.SLIPPERY_ICE),
                Platform(4, 580f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint
                Platform(5, 740f, 200f, 120f, 18f, Color(0xFF74B9FF), trapType = TrapType.SLIPPERY_ICE),
                Platform(6, 920f, 250f, 300f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 740f, 16f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 620f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 270f, 180f),
                CollectibleStar(2, 790f, 160f)
            ),
            door = Door(id = 1, x = 1080f, y = 212f),
            hint = "Be careful! Light cyan ice platforms have zero friction and will make you slide!"
        ),

        // LEVEL 20: The Grand Master Trial 👑 (Extreme)
        Level(
            id = 20,
            name = "Level 20: Grand Master Trial 👑",
            subtitle = "Portals, springs, sinking steps, fleeing keys & gravity flips!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2600f,
            parTimeMs = 28000L,
            difficultyRating = "Extreme",
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 220f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(3, 320f, 200f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(4, 430f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 1
                Platform(5, 580f, 250f, 80f, 60f, Color(0xFF3742FA)),
                Platform(6, 750f, 160f, 100f, 18f, Color(0xFF2ED573)),
                Platform(7, 920f, 30f, 400f, 25f, Color(0xFF9C27B0)), // Ceiling
                Platform(8, 1380f, 250f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 2
                Platform(9, 1540f, 220f, 80f, 18f, Color(0xFF009688), trapType = TrapType.MOVING_PLATFORM, startX = 1540f, endX = 1800f, moveSpeed = 3.2f),
                Platform(10, 1950f, 250f, 350f, 60f, Color(0xFF2ED573))
            ),
            springs = listOf(
                SpringPad(1, x = 600f, y = 236f, launchPower = -23f)
            ),
            teleporters = listOf(
                Teleporter(1, x = 780f, y = 120f, targetX = 940f, targetY = 60f)
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 250f, 16f),
                Spike(2, 540f, 296f, 380f, 16f),
                Spike(3, 920f, 296f, 460f, 16f),
                Spike(4, 1080f, 55f, 60f, 16f, isCeilingSpike = true),
                Spike(5, 1490f, 296f, 460f, 16f),
                Spike(6, 2020f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f)
            ),
            keys = listOf(
                LevelKey(1, x = 335f, y = 160f, isMovingKey = true, targetX = 460f, triggerDistance = 85f),
                LevelKey(2, x = 1120f, y = 65f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(900f, 30f, 940f, 100f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(2, Rect(1320f, 30f, 1360f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMAL")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 460f, y = 184f),
                Checkpoint(2, x = 1410f, y = 184f)
            ),
            stars = listOf(
                CollectibleStar(1, 350f, 130f),
                CollectibleStar(2, 1140f, 70f),
                CollectibleStar(3, 1700f, 160f)
            ),
            door = Door(
                id = 1, x = 2180f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 2250f,
                isLocked = true,
                triggerDistance = 75f
            ),
            hint = "The ultimate trial! Master springs, portals, fleeing keys, ceiling walking, and fast platforms to win!"
        )
    )

    fun getLevel(index: Int): Level {
        return if (index < levels.size) {
            levels[index]
        } else {
            generateProceduralLevel(index)
        }
    }

    // Procedural Generator for Level 21+ (100% Beatable!)
    fun generateProceduralLevel(index: Int): Level {
        val levelNum = index + 1
        val seed = levelNum * 9301 + 49297
        var currentX = 0f
        val platformsList = mutableListOf<Platform>()
        val spikesList = mutableListOf<Spike>()
        val checkpointsList = mutableListOf<Checkpoint>()
        val triggerZonesList = mutableListOf<TriggerZone>()
        val teleportersList = mutableListOf<Teleporter>()
        val springsList = mutableListOf<SpringPad>()
        val keysList = mutableListOf<LevelKey>()
        val starsList = mutableListOf<CollectibleStar>()

        var pId = 1
        var sId = 1
        var chkId = 1

        // Start platform
        platformsList.add(Platform(pId++, 0f, 250f, 180f, 60f, Color(0xFF3742FA)))
        currentX += 180f

        val sectionCount = 4 + (levelNum % 4)
        for (i in 0 until sectionCount) {
            val sectionType = (seed + i * 37) % 6
            val gapWidth = 80f + ((seed + i * 13) % 40)

            spikesList.add(Spike(sId++, currentX, 296f, gapWidth, 16f))
            currentX += gapWidth

            when (sectionType) {
                0 -> { // Sinking floor steps
                    platformsList.add(Platform(pId++, currentX, 230f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR))
                    currentX += 70f
                }
                1 -> { // Disappearing steps
                    platformsList.add(Platform(pId++, currentX, 220f, 70f, 18f, Color(0xFFFF9800), trapType = TrapType.DISAPPEAR_ON_TOUCH))
                    currentX += 70f
                }
                2 -> { // Moving platform
                    platformsList.add(
                        Platform(
                            pId++, currentX, 220f, 80f, 18f, Color(0xFF009688),
                            trapType = TrapType.MOVING_PLATFORM,
                            startX = currentX, startY = 220f, endX = currentX + 100f, endY = 220f, moveSpeed = 2f
                        )
                    )
                    currentX += 180f
                }
                3 -> { // Spring pad
                    platformsList.add(Platform(pId++, currentX, 250f, 80f, 60f, Color(0xFF3742FA)))
                    springsList.add(SpringPad(1, x = currentX + 24f, y = 236f, launchPower = -22f))
                    currentX += 80f
                }
                4 -> { // Teleporter
                    platformsList.add(Platform(pId++, currentX, 220f, 80f, 18f, Color(0xFF3742FA)))
                    teleportersList.add(Teleporter(1, x = currentX + 20f, y = 170f, targetX = currentX + 160f, targetY = 210f))
                    currentX += 80f
                }
                else -> { // Solid checkpoint island
                    platformsList.add(Platform(pId++, currentX, 250f, 120f, 60f, Color(0xFF2ED573)))
                    checkpointsList.add(Checkpoint(chkId++, x = currentX + 45f, y = 184f))
                    currentX += 120f
                }
            }

            starsList.add(CollectibleStar(i + 1, currentX - 30f, 170f))
        }

        // Final exit platform
        spikesList.add(Spike(sId++, currentX, 296f, 100f, 16f))
        currentX += 100f
        platformsList.add(Platform(pId++, currentX, 250f, 250f, 60f, Color(0xFF2ED573)))

        val finalDoor = Door(id = 1, x = currentX + 120f, y = 212f)

        return Level(
            id = levelNum,
            name = "Level $levelNum: Endless Devil ♾️",
            subtitle = "Procedurally generated trial!",
            playerStart = Pair(50f, 220f),
            platforms = platformsList,
            spikes = spikesList,
            door = finalDoor,
            keys = keysList,
            teleporters = teleportersList,
            springs = springsList,
            stars = starsList,
            checkpoints = checkpointsList,
            triggerZones = triggerZonesList,
            worldWidth = currentX + 300f,
            hint = "Procedural level: Keep jumping and stay alert!",
            parTimeMs = 20000L,
            difficultyRating = if (levelNum > 30) "Extreme" else "Insane"
        )
    }
}

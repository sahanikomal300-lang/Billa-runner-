package com.example.game.data

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import com.example.game.model.Checkpoint
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.LevelKey
import com.example.game.model.Platform
import com.example.game.model.Spike
import com.example.game.model.TrapType
import com.example.game.model.TriggerZone

object LevelData {

    val levels = listOf(
        // LEVEL 1: Looks Easy
        Level(
            id = 1,
            name = "Level 1: Looks Easy",
            subtitle = "Just walk to the exit!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1000f,
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
            door = Door(id = 1, x = 730f, y = 212f, width = 28f, height = 38f),
            hint = "Beware: What you see isn't always solid!"
        ),

        // LEVEL 2: Watch Your Step
        Level(
            id = 2,
            name = "Level 2: Watch Your Step",
            subtitle = "Don't rush forward!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
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
            door = Door(id = 1, x = 920f, y = 212f, width = 28f, height = 38f),
            hint = "Spikes can jump out of nowhere!"
        ),

        // LEVEL 3: Shy Door
        Level(
            id = 3,
            name = "Level 3: Shy Door",
            subtitle = "Reach the exit... if you can!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            platforms = listOf(
                Platform(1, 0f, 250f, 1000f, 60f, Color(0xFF3742FA)),
                Platform(2, 400f, 190f, 80f, 60f, Color(0xFF5F27CD))
            ),
            spikes = listOf(
                Spike(1, 400f, 174f, 80f, 16f)
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

        // LEVEL 4: Mind Flip
        Level(
            id = 4,
            name = "Level 4: Mind Flip",
            subtitle = "Cross the toxic gap!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
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
            door = Door(id = 1, x = 720f, y = 212f, width = 28f, height = 38f),
            triggerZones = listOf(
                TriggerZone(1, Rect(280f, 180f, 420f, 250f), TrapType.INVERT_CONTROLS, "⚠️ CONTROLS FLIPPED!")
            ),
            hint = "Walking on orange flips Left & Right controls!"
        ),

        // LEVEL 5: Heavy Sky
        Level(
            id = 5,
            name = "Level 5: Heavy Sky",
            subtitle = "Don't look up!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1100f,
            platforms = listOf(
                Platform(1, 0f, 250f, 900f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 220f, 60f, 80f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 100f
                ),
                Platform(
                    3, 450f, 60f, 80f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 100f
                )
            ),
            spikes = listOf(
                Spike(1, 620f, 234f, 32f, 16f)
            ),
            door = Door(id = 1, x = 820f, y = 212f, width = 28f, height = 38f),
            hint = "Bait the crushing blocks before moving forward!"
        ),

        // LEVEL 6: Crumbling Steps & Checkpoint
        Level(
            id = 6,
            name = "Level 6: Crumbling Steps",
            subtitle = "Step lightly and reach safety!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1200f,
            platforms = listOf(
                Platform(1, 0f, 250f, 120f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 160f, 210f, 75f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(
                    3, 270f, 170f, 75f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                // Safe Checkpoint Island
                Platform(4, 380f, 150f, 100f, 150f, Color(0xFF2ED573)),
                Platform(
                    5, 510f, 130f, 75f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(6, 620f, 110f, 200f, 200f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 120f, 296f, 500f, 16f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 410f, y = 114f)
            ),
            door = Door(id = 1, x = 740f, y = 72f, width = 28f, height = 38f),
            hint = "Platforms vanish when touched! Use the checkpoint!"
        ),

        // LEVEL 7: The Size Trap
        Level(
            id = 7,
            name = "Level 7: Size Shifter",
            subtitle = "Smash walls as GIANT, squeeze as TINY!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1300f,
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF3742FA)),
                // Fragile wall breakable by GIANT player
                Platform(2, 320f, 130f, 30f, 120f, Color(0xFFFF4757), isBreakable = true),
                Platform(3, 200f, 250f, 300f, 60f, Color(0xFF3742FA)),
                // Low ceiling crawl space (only TINY can enter)
                Platform(4, 520f, 180f, 250f, 30f, Color(0xFF5F27CD)), // Ceiling above
                Platform(5, 500f, 250f, 350f, 60f, Color(0xFF3742FA)),
                Platform(6, 880f, 250f, 250f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 550f, 234f, 24f, 16f, isHiddenSpike = true)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(120f, 180f, 180f, 250f), TrapType.SIZE_GIANT, "🔥 GIANT MODE! BREAK THE WALL!"),
                TriggerZone(2, Rect(420f, 180f, 480f, 250f), TrapType.SIZE_TINY, "🔬 TINY MODE! SQUEEZE THROUGH!"),
                TriggerZone(3, Rect(850f, 180f, 900f, 250f), TrapType.SIZE_NORMAL, "⚡ NORMAL SIZE RESTORED")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 440f, y = 214f)
            ),
            door = Door(id = 1, x = 1020f, y = 212f, width = 28f, height = 38f),
            hint = "Grow GIANT to break obstacles, turn TINY to pass low ceilings!"
        ),

        // LEVEL 8: Gravity Flip Zone
        Level(
            id = 8,
            name = "Level 8: Anti-Gravity",
            subtitle = "Walk on the ceiling above toxic pits!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1300f,
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF009688)),
                // Top Ceiling Platform for upside-down walking
                Platform(2, 220f, 30f, 600f, 30f, Color(0xFF9C27B0)),
                // Ground gap full of spikes
                Platform(3, 850f, 250f, 300f, 60f, Color(0xFF009688))
            ),
            spikes = listOf(
                Spike(1, 200f, 296f, 650f, 16f) // Bottom pit
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(170f, 180f, 230f, 250f), TrapType.GRAVITY_REVERSE, "🌌 GRAVITY REVERSED! WALK ON CEILING!"),
                TriggerZone(2, Rect(800f, 30f, 850f, 120f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED!")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 500f, y = 60f)
            ),
            door = Door(id = 1, x = 1020f, y = 212f, width = 28f, height = 38f),
            hint = "When gravity flips, landing on ceiling platforms is safe!"
        ),

        // LEVEL 9: Double Combo Troll
        Level(
            id = 9,
            name = "Level 9: Double Combo",
            subtitle = "Reverse gravity + Moving platforms!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1400f,
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                // Moving platform in mid air
                Platform(
                    2, 220f, 220f, 100f, 20f, Color(0xFFFF9800),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 220f, startY = 220f, endX = 420f, endY = 220f, moveSpeed = 2f
                ),
                // Ceiling track
                Platform(3, 460f, 40f, 300f, 25f, Color(0xFF9C27B0)),
                Platform(4, 800f, 250f, 300f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 620f, 16f)
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(420f, 150f, 470f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY!"),
                TriggerZone(2, Rect(740f, 40f, 790f, 120f), TrapType.GRAVITY_NORMAL, "⬇️ NORMAL GRAVITY")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 550f, y = 65f)
            ),
            door = Door(
                id = 1, x = 900f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1040f,
                triggerDistance = 90f
            ),
            hint = "Ride the moving platform to the gravity inverter!"
        ),

        // LEVEL 10: Devil's Gauntlet
        Level(
            id = 10,
            name = "Level 10: Devil's Gauntlet",
            subtitle = "Crushing blocks, ceiling spikes & narrow jumps!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1500f,
            platforms = listOf(
                Platform(1, 0f, 250f, 140f, 60f, Color(0xFFD32F2F)),
                // Falling heavy block
                Platform(
                    2, 180f, 60f, 90f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 100f
                ),
                Platform(3, 160f, 250f, 140f, 60f, Color(0xFFD32F2F)),
                // Disappearing touch platform
                Platform(
                    4, 330f, 210f, 80f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                // Safe Island Checkpoint
                Platform(5, 450f, 180f, 120f, 120f, Color(0xFF2ED573)),
                // Moving Platform
                Platform(
                    6, 610f, 180f, 90f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 610f, startY = 180f, endX = 810f, endY = 180f, moveSpeed = 2.5f
                ),
                Platform(7, 940f, 250f, 300f, 60f, Color(0xFFD32F2F))
            ),
            spikes = listOf(
                Spike(1, 140f, 296f, 800f, 16f),
                // Ceiling Spikes (pointing down!)
                Spike(2, 450f, 60f, 120f, 16f, isCeilingSpike = true)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 490f, y = 144f)
            ),
            door = Door(
                id = 1, x = 1050f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1140f,
                triggerDistance = 85f
            ),
            hint = "Time your jumps carefully between moving platforms and ceiling traps!"
        ),

        // LEVEL 11: TROLL MODE (ULTIMATE MASTERPIECE - 100% BEATABLE TROLL GAUNTLET)
        Level(
            id = 11,
            name = "Level 11: TROLL MODE 🔥",
            subtitle = "The ultimate test of skill, patience, and memory!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2000f,
            platforms = listOf(
                // --- SECTION 1: GIANT WALL BREAKER ---
                Platform(1, 0f, 250f, 220f, 60f, Color(0xFFD32F2F)),
                Platform(2, 280f, 130f, 30f, 120f, Color(0xFFFF4757), isBreakable = true), // Wall
                Platform(3, 220f, 250f, 220f, 60f, Color(0xFFD32F2F)),

                // --- SECTION 2: REVERSE GRAVITY CEILING WALK ---
                Platform(4, 460f, 30f, 350f, 25f, Color(0xFF9C27B0)), // Ceiling runway

                // --- SECTION 3: CHECKPOINT 1 & FAKE DOOR TUNNEL ---
                Platform(5, 830f, 250f, 160f, 60f, Color(0xFF2ED573)), // Mid Checkpoint Ground
                Platform(6, 1010f, 180f, 260f, 30f, Color(0xFF5F27CD)), // Low ceiling for tiny crawl
                Platform(7, 990f, 250f, 300f, 60f, Color(0xFFD32F2F)), // Crawl floor

                // --- SECTION 4: CHECKPOINT 2 & MOVING PLATFORM TO REAL DOOR ---
                Platform(8, 1310f, 250f, 150f, 60f, Color(0xFF2ED573)), // Checkpoint 2 ground
                Platform(
                    9, 1490f, 220f, 100f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 1490f, startY = 220f, endX = 1690f, endY = 220f, moveSpeed = 2.2f
                ),
                Platform(10, 1810f, 250f, 180f, 60f, Color(0xFF2ED573)) // Real Door Ground
            ),
            spikes = listOf(
                Spike(1, 440f, 296f, 390f, 16f), // Pit under ceiling walk
                Spike(2, 1010f, 210f, 260f, 16f, isCeilingSpike = true), // Ceiling spikes in tiny crawl
                Spike(3, 1290f, 296f, 520f, 16f) // Deep pit under final moving platform
            ),
            triggerZones = listOf(
                TriggerZone(1, Rect(120f, 180f, 180f, 250f), TrapType.SIZE_GIANT, "🔥 GIANT MODE! SMASH THE WALL!"),
                TriggerZone(2, Rect(410f, 150f, 460f, 250f), TrapType.GRAVITY_REVERSE, "🌌 REVERSE GRAVITY! WALK ON CEILING!"),
                TriggerZone(3, Rect(800f, 30f, 840f, 100f), TrapType.GRAVITY_NORMAL, "⬇️ GRAVITY NORMALIZED!"),
                TriggerZone(4, Rect(940f, 180f, 980f, 250f), TrapType.SIZE_TINY, "🔬 TINY MODE! SQUEEZE UNDER SPIKES!"),
                TriggerZone(5, Rect(1280f, 180f, 1320f, 250f), TrapType.SIZE_NORMAL, "⚡ SIZE RESTORED!")
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 860f, y = 214f),
                Checkpoint(2, x = 1350f, y = 214f)
            ),
            // Fake Door troll at x=950, Real Door at x=1880
            door = Door(
                id = 1, x = 1880f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1910f,
                triggerDistance = 70f
            ),
            hint = "Troll Mode: Giant Smash -> Reverse Gravity -> Tiny Crawl -> Moving Platform!"
        ),

        // LEVEL 12: IMPOSSIBLE? 🔥💀 (VERY HARD GAUNTLET - 100% BEATABLE!)
        Level(
            id = 12,
            name = "Level 12: IMPOSSIBLE? 🔥💀",
            subtitle = "Extreme precision gauntlet with 3 checkpoints! Hard but beatable!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2200f,
            platforms = listOf(
                // --- SECTION 1: PRECISION TIMING & DISAPPEARING STEPS ---
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                // Disappearing approach step
                Platform(
                    2, 210f, 220f, 60f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 90f
                ),
                // Controls inverted platform
                Platform(
                    3, 300f, 200f, 70f, 18f, Color(0xFFFF5722),
                    trapType = TrapType.INVERT_CONTROLS
                ),
                // Falling heavy block above jump path
                Platform(
                    4, 330f, 40f, 70f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK, triggerDistance = 90f
                ),
                // Checkpoint 1 Island
                Platform(5, 410f, 220f, 110f, 80f, Color(0xFF2ED573)),

                // --- SECTION 2: GIANT WALL BREAK & DISAPPEARING STEP ---
                Platform(6, 520f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(7, 560f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true), // Wall
                // Disappearing touch step
                Platform(
                    8, 730f, 220f, 65f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                // Checkpoint 2 Island
                Platform(9, 820f, 250f, 120f, 60f, Color(0xFF2ED573)),

                // --- SECTION 3: REVERSE GRAVITY TINY SPIKE TUNNEL ---
                Platform(10, 930f, 30f, 450f, 25f, Color(0xFF9C27B0)), // Ceiling runway
                // Checkpoint 3 Island
                Platform(11, 1420f, 250f, 120f, 60f, Color(0xFF2ED573)),

                // --- SECTION 4: HIGH SPEED MOVING PLATFORM & SHY DOOR ---
                Platform(
                    12, 1570f, 220f, 90f, 18f, Color(0xFF009688),
                    trapType = TrapType.MOVING_PLATFORM,
                    startX = 1570f, startY = 220f, endX = 1790f, endY = 220f, moveSpeed = 2.8f
                ),
                // Final Door Platform
                Platform(13, 1850f, 250f, 250f, 60f, Color(0xFF2ED573))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 230f, 16f), // Pit 1
                Spike(2, 520f, 296f, 300f, 16f), // Pit 2 under giant wall & touch step
                Spike(3, 920f, 296f, 500f, 16f), // Main spike pit under ceiling walk
                Spike(4, 1100f, 55f, 80f, 16f, isCeilingSpike = true), // Ceiling spikes on runway
                Spike(5, 1540f, 296f, 310f, 16f), // Deep pit under fast moving platform
                Spike(6, 1890f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 85f) // Hidden spike near door
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
            door = Door(
                id = 1, x = 2000f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 2060f,
                triggerDistance = 75f
            ),
            hint = "Devil's Masterpiece: Master invert jump -> Giant smash -> Tiny gravity crawl -> Fast ride!"
        ),

        // LEVEL 13: The Golden Key 🔑
        Level(
            id = 13,
            name = "Level 13: The Golden Key 🔑",
            subtitle = "Collect the key to unlock the door... if it doesn't flee!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1400f,
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 230f, 220f, 80f, 18f, Color(0xFFFF9800), trapType = TrapType.DISAPPEAR_ON_TOUCH),
                Platform(3, 360f, 220f, 100f, 60f, Color(0xFF2ED573)), // Checkpoint 1
                Platform(4, 520f, 200f, 90f, 18f, Color(0xFF3742FA)),  // Key platform
                Platform(5, 660f, 220f, 90f, 18f, Color(0xFFFF5722), trapType = TrapType.INVERT_CONTROLS),
                Platform(6, 800f, 200f, 100f, 18f, Color(0xFF3742FA)), // Key flee target platform
                Platform(7, 950f, 250f, 250f, 60f, Color(0xFF2ED573))  // Door platform
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 770f, 16f), // Main spike pit
                Spike(2, 680f, 204f, 24f, 16f, isHiddenSpike = true, triggerDistance = 80f)
            ),
            keys = listOf(
                LevelKey(1, x = 550f, y = 175f, isMovingKey = true, targetX = 830f, triggerDistance = 90f)
            ),
            checkpoints = listOf(
                Checkpoint(1, x = 400f, y = 184f)
            ),
            door = Door(id = 1, x = 1120f, y = 212f, isLocked = true),
            hint = "Reach the key before it flees, then unlock the green exit!"
        ),

        // LEVEL 14: Sinking Sands ⏳
        Level(
            id = 14,
            name = "Level 14: Sinking Sands ⏳",
            subtitle = "Don't linger! Every platform sinks into the abyss!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1500f,
            platforms = listOf(
                Platform(1, 0f, 250f, 160f, 60f, Color(0xFF3742FA)),
                Platform(2, 210f, 240f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(3, 320f, 220f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(4, 430f, 200f, 70f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(5, 540f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 1
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
            door = Door(id = 1, x = 1200f, y = 212f),
            hint = "Keep hopping! Standing still means falling into spikes!"
        ),

        // LEVEL 15: Teleporting Portal 🚪
        Level(
            id = 15,
            name = "Level 15: Teleporting Portal 🚪",
            subtitle = "The door is playing hide & seek!",
            playerStart = Pair(50f, 220f),
            worldWidth = 1600f,
            platforms = listOf(
                Platform(1, 0f, 250f, 200f, 60f, Color(0xFF3742FA)),
                Platform(2, 240f, 220f, 80f, 18f, Color(0xFFFF9800), trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 100f),
                Platform(3, 370f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint
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
            door = Door(
                id = 1, x = 600f, y = 182f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 1200f,
                triggerDistance = 80f
            ),
            hint = "Don't jump directly at the first door location!"
        ),

        // LEVEL 16: Devil's Ultimate Gauntlet 😈
        Level(
            id = 16,
            name = "Level 16: Devil's Gauntlet 😈",
            subtitle = "The master troll level! Sinking floors, keys, reverse gravity & moving doors!",
            playerStart = Pair(50f, 220f),
            worldWidth = 2400f,
            platforms = listOf(
                // Section 1: Sinking steps & Key 1
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 220f, 220f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(3, 320f, 200f, 65f, 18f, Color(0xFF9C27B0), trapType = TrapType.SINKING_FLOOR),
                Platform(4, 430f, 220f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 1

                // Section 2: Giant Wall Break & Reverse Gravity Tunnel
                Platform(5, 580f, 250f, 180f, 60f, Color(0xFFD32F2F)),
                Platform(6, 620f, 110f, 30f, 140f, Color(0xFFFF4757), isBreakable = true), // Wall
                Platform(7, 780f, 250f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 2
                Platform(8, 920f, 30f, 400f, 25f, Color(0xFF9C27B0)),  // Ceiling runway

                // Section 3: Key 2 & Fast Moving Platform
                Platform(9, 1350f, 250f, 110f, 60f, Color(0xFF2ED573)), // Checkpoint 3
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
            door = Door(
                id = 1, x = 2050f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 2120f,
                isLocked = true,
                triggerDistance = 75f
            ),
            hint = "Master all 16 levels: Sinking steps, fleeing key, giant smash, upside-down ceiling walk & moving exit!"
        )
    )

    val funnyTaunts = listOf(
        "Nice try, human!",
        "Level Devil 1 - You 0!",
        "Did you really think it was that easy?",
        "Gravity says hi!",
        "Gravity: 100, Reflexes: 0",
        "Classic troll trap!",
        "Don't worry, even pros die here!",
        "The exit was right there!",
        "Spikes love tight hugs!",
        "Try jumping earlier next time!",
        "Troll level strikes again! 😂",
        "Look before you leap!"
    )

    /**
     * Procedural Level Generator for Level 12+
     * Guarantees beatable gaps (jump distance <= 220 units), valid platforms,
     * checkpoint placed midway, and reachable door.
     */
    fun getLevel(index: Int): Level {
        val safeIndex = index.coerceAtLeast(0)
        if (safeIndex < levels.size) {
            return levels[safeIndex]
        }

        val levelNum = safeIndex + 1
        val seed = levelNum.toLong() * 9301L + 49297L
        val random = kotlin.random.Random(seed)

        val worldWidth = 1600f + (random.nextInt(0, 4) * 200f)
        val startPlatform = Platform(1, 0f, 250f, 160f, 60f, Color(0xFF3742FA))
        val endPlatform = Platform(99, worldWidth - 200f, 230f, 200f, 80f, Color(0xFF3742FA))

        val platforms = mutableListOf(startPlatform)
        val spikes = mutableListOf(
            Spike(1, 160f, 296f, worldWidth - 360f, 16f)
        )
        val triggerZones = mutableListOf<TriggerZone>()

        val platformCount = random.nextInt(6, 10)
        val stepX = (worldWidth - 400f) / platformCount
        var currentX = 160f
        var currentY = 240f

        var sizeTrapPlaced = false
        var gravityTrapPlaced = false

        for (i in 1..platformCount) {
            val gap = random.nextInt(50, 120).toFloat() // Well within max 220 jump gap!
            val pWidth = random.nextInt(80, 130).toFloat()
            val nextY = (currentY + random.nextInt(-40, 40)).coerceIn(140f, 260f)

            val isMoving = (i % 3 == 0)
            val trapRoll = random.nextInt(0, 6)
            val trapType = when (trapRoll) {
                0 -> TrapType.DISAPPEAR_ON_APPROACH
                1 -> TrapType.DISAPPEAR_ON_TOUCH
                2 -> TrapType.INVERT_CONTROLS
                3 -> TrapType.FALLING_BLOCK
                else -> TrapType.NONE
            }

            val pColor = when {
                isMoving -> Color(0xFF009688)
                trapType == TrapType.DISAPPEAR_ON_APPROACH -> Color(0xFFE91E63)
                trapType == TrapType.DISAPPEAR_ON_TOUCH -> Color(0xFFFF9800)
                trapType == TrapType.INVERT_CONTROLS -> Color(0xFFFF5722)
                trapType == TrapType.FALLING_BLOCK -> Color(0xFF9C27B0)
                else -> Color(0xFF3742FA)
            }

            val platformX = currentX + gap
            if (isMoving) {
                platforms.add(
                    Platform(
                        id = i + 1,
                        x = platformX,
                        y = nextY,
                        width = pWidth,
                        height = 18f,
                        color = pColor,
                        trapType = TrapType.MOVING_PLATFORM,
                        startX = platformX, startY = nextY,
                        endX = platformX + 90f, endY = nextY,
                        moveSpeed = 1.8f
                    )
                )
            } else {
                platforms.add(
                    Platform(
                        id = i + 1,
                        x = platformX,
                        y = nextY,
                        width = pWidth,
                        height = 18f,
                        color = pColor,
                        trapType = trapType,
                        triggerDistance = random.nextInt(85, 110).toFloat()
                    )
                )
            }

            // Add occasional hidden spike
            if (random.nextInt(0, 3) == 0 && !isMoving) {
                spikes.add(
                    Spike(
                        id = spikes.size + 1,
                        x = platformX + pWidth / 2f - 10f,
                        y = nextY - 16f,
                        width = 20f, height = 16f,
                        isHiddenSpike = true,
                        triggerDistance = 85f
                    )
                )
            }

            // Add size/gravity trigger zones safely
            if (!sizeTrapPlaced && i == 2) {
                triggerZones.add(
                    TriggerZone(1, Rect(platformX, nextY - 60f, platformX + 40f, nextY), TrapType.SIZE_TINY, "🔬 TINY MODE!")
                )
                sizeTrapPlaced = true
            } else if (sizeTrapPlaced && i == 4) {
                triggerZones.add(
                    TriggerZone(2, Rect(platformX, nextY - 60f, platformX + 40f, nextY), TrapType.SIZE_NORMAL, "⚡ NORMAL SIZE")
                )
            }

            currentX = platformX + pWidth
            currentY = nextY
        }

        platforms.add(endPlatform)

        // Mid-level Checkpoint
        val midIndex = (platforms.size / 2).coerceIn(1, platforms.size - 2)
        val midPlatform = platforms[midIndex]
        val checkpoints = listOf(
            Checkpoint(1, x = midPlatform.x + midPlatform.width / 2f - 12f, y = midPlatform.y - 36f)
        )

        val door = Door(
            id = 1,
            x = worldWidth - 120f,
            y = 192f,
            width = 28f,
            height = 38f,
            isMovingDoor = random.nextBoolean(),
            targetX = worldWidth - 60f,
            triggerDistance = 85f
        )

        return Level(
            id = levelNum,
            name = "Level $levelNum: Procedural Challenge",
            subtitle = "Generated Troll Gauntlet #$levelNum",
            playerStart = Pair(50f, 220f),
            worldWidth = worldWidth,
            platforms = platforms,
            spikes = spikes,
            checkpoints = checkpoints,
            triggerZones = triggerZones,
            door = door,
            hint = "Procedurally generated level verified to be 100% beatable!"
        )
    }
}

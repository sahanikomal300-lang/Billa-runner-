package com.example.game.data

import androidx.compose.ui.graphics.Color
import com.example.game.model.Door
import com.example.game.model.Level
import com.example.game.model.Platform
import com.example.game.model.Spike
import com.example.game.model.TrapType

object LevelData {

    // Reference virtual coordinate space: Height = 320f
    val levels = listOf(
        // LEVEL 1: Disappearing Floor
        Level(
            id = 1,
            name = "Level 1: Looks Easy",
            subtitle = "Just walk to the door!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                // Starting ground
                Platform(1, 0f, 250f, 220f, 60f, Color(0xFF3742FA)),
                // Middle platform that DISAPPEARS on approach!
                Platform(
                    2, 250f, 250f, 150f, 60f, Color(0xFF3742FA),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 110f
                ),
                // Lower safety ledge
                Platform(
                    3, 280f, 285f, 90f, 20f, Color(0xFFE91E63),
                    trapType = TrapType.NONE
                ),
                // Goal ground
                Platform(4, 520f, 250f, 280f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 220f, 296f, 300f, 16f) // Bottom pit spikes
            ),
            door = Door(x = 730f, y = 212f, width = 28f, height = 38f),
            hint = "Beware: What you see isn't always solid!"
        ),

        // LEVEL 2: Sprouting Spikes
        Level(
            id = 2,
            name = "Level 2: Watch Your Step",
            subtitle = "Don't rush forward!",
            playerStart = Pair(50f, 220f),
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
            door = Door(x = 920f, y = 212f, width = 28f, height = 38f),
            hint = "Spikes can jump out of nowhere!"
        ),

        // LEVEL 3: Moving Door
        Level(
            id = 3,
            name = "Level 3: Shy Door",
            subtitle = "Reach the exit... if you can!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 1000f, 60f, Color(0xFF3742FA)),
                Platform(2, 400f, 190f, 80f, 60f, Color(0xFF5F27CD))
            ),
            spikes = listOf(
                Spike(1, 400f, 174f, 80f, 16f)
            ),
            door = Door(
                x = 520f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 900f,
                triggerDistance = 100f
            ),
            hint = "The exit might run away from you!"
        ),

        // LEVEL 4: Inverted Controls
        Level(
            id = 4,
            name = "Level 4: Mind Flip",
            subtitle = "Cross the toxic gap!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 220f, 60f, Color(0xFF009688)),
                Platform(
                    2, 280f, 220f, 140f, 90f, Color(0xFFFF5722),
                    trapType = TrapType.INVERT_CONTROLS,
                    triggerDistance = 85f
                ),
                Platform(3, 490f, 250f, 300f, 60f, Color(0xFF009688))
            ),
            spikes = listOf(
                Spike(1, 220f, 296f, 270f, 16f)
            ),
            door = Door(x = 720f, y = 212f, width = 28f, height = 38f),
            isControlInvertedTrigger = Pair(300f, 220f),
            hint = "Warning: Walking on orange flips your controls!"
        ),

        // LEVEL 5: Falling Ceilings
        Level(
            id = 5,
            name = "Level 5: Heavy Sky",
            subtitle = "Don't look up!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 900f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 220f, 60f, 80f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK,
                    triggerDistance = 100f
                ),
                Platform(
                    3, 450f, 60f, 80f, 50f, Color(0xFFE91E63),
                    trapType = TrapType.FALLING_BLOCK,
                    triggerDistance = 100f
                )
            ),
            spikes = listOf(
                Spike(1, 620f, 234f, 32f, 16f)
            ),
            door = Door(x = 820f, y = 212f, width = 28f, height = 38f),
            hint = "Bait the crushing blocks before moving!"
        ),

        // LEVEL 6: Disappearing Steps
        Level(
            id = 6,
            name = "Level 6: Crumbling Steps",
            subtitle = "Step lightly!",
            playerStart = Pair(50f, 220f),
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
                Platform(
                    4, 380f, 130f, 75f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(5, 500f, 110f, 200f, 200f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 120f, 296f, 380f, 16f)
            ),
            door = Door(x = 620f, y = 72f, width = 28f, height = 38f),
            hint = "Platforms disappear right after you touch them!"
        ),

        // LEVEL 7: Fake Door
        Level(
            id = 7,
            name = "Level 7: Illusionist",
            subtitle = "Which door is real?",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 900f, 60f, Color(0xFF3742FA)),
                Platform(2, 320f, 170f, 90f, 18f, Color(0xFF9C27B0))
            ),
            spikes = listOf(
                Spike(1, 240f, 234f, 32f, 16f, isHiddenSpike = true),
                Spike(2, 450f, 234f, 32f, 16f, isHiddenSpike = true)
            ),
            door = Door(
                x = 350f, y = 132f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 820f,
                triggerDistance = 85f
            ),
            hint = "Touch the fake door to reveal the real path!"
        ),

        // LEVEL 8: Invisible Glass Bridge
        Level(
            id = 8,
            name = "Level 8: Invisible Bridge",
            subtitle = "Trust the unseen!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(2, 195f, 250f, 280f, 18f, Color(0x66FFFFFF), isVisible = true),
                Platform(3, 490f, 250f, 250f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 260f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 75f),
                Spike(2, 370f, 234f, 24f, 16f, isHiddenSpike = true, triggerDistance = 75f)
            ),
            door = Door(x = 670f, y = 212f, width = 28f, height = 38f),
            hint = "The glass platform is real, but watch out for hidden spikes!"
        ),

        // LEVEL 9: Double Trap
        Level(
            id = 9,
            name = "Level 9: Double Trap",
            subtitle = "Expect the unexpected!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 180f, 60f, Color(0xFF3742FA)),
                Platform(
                    2, 200f, 250f, 110f, 60f, Color(0xFFE91E63),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH, triggerDistance = 90f
                ),
                Platform(
                    3, 330f, 250f, 110f, 60f, Color(0xFFFF9800),
                    trapType = TrapType.INVERT_CONTROLS, triggerDistance = 80f
                ),
                Platform(4, 460f, 250f, 280f, 60f, Color(0xFF3742FA))
            ),
            spikes = listOf(
                Spike(1, 180f, 296f, 280f, 16f),
                Spike(2, 530f, 234f, 24f, 16f, isHiddenSpike = true)
            ),
            door = Door(
                x = 510f, y = 212f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 680f,
                triggerDistance = 100f
            ),
            hint = "Vanishing floor + flipped controls + moving exit!"
        ),

        // LEVEL 10: Devil's Boss Gauntlet
        Level(
            id = 10,
            name = "Level 10: Devil's Gauntlet",
            subtitle = "The ultimate challenge!",
            playerStart = Pair(50f, 220f),
            platforms = listOf(
                Platform(1, 0f, 250f, 120f, 60f, Color(0xFFD32F2F)),
                Platform(
                    2, 145f, 210f, 75f, 18f, Color(0xFFFF9800),
                    trapType = TrapType.DISAPPEAR_ON_TOUCH
                ),
                Platform(
                    3, 250f, 170f, 75f, 18f, Color(0xFFE91E63),
                    trapType = TrapType.DISAPPEAR_ON_APPROACH
                ),
                Platform(
                    4, 355f, 130f, 85f, 18f, Color(0xFF9C27B0),
                    trapType = TrapType.INVERT_CONTROLS
                ),
                Platform(5, 480f, 90f, 220f, 230f, Color(0xFFD32F2F))
            ),
            spikes = listOf(
                Spike(1, 120f, 296f, 360f, 16f),
                Spike(2, 530f, 74f, 24f, 16f, isHiddenSpike = true, triggerDistance = 80f)
            ),
            door = Door(
                x = 530f, y = 52f,
                width = 28f, height = 38f,
                isMovingDoor = true,
                targetX = 640f,
                triggerDistance = 85f
            ),
            hint = "Everything wants to stop you. Good luck!"
        )
    )

    val funnyTaunts = listOf(
        "Nice try, human!",
        "Level Devil 1 - You 0!",
        "Did you really think it was that easy?",
        "Gravity says hi!",
        "Gravity: 100, Reflexes: 0",
        "Classic trap!",
        "Don't worry, even pros die here!",
        "The door was right there!",
        "Spikes love hugs!",
        "Try jumping earlier next time!"
    )

    fun getLevel(index: Int): Level {
        val safeIndex = index.coerceAtLeast(0)
        if (safeIndex < levels.size) {
            return levels[safeIndex]
        }

        // Procedural Generation for infinite levels
        val levelNum = safeIndex + 1
        val seed = levelNum.toLong() * 9301L + 49297L
        val random = kotlin.random.Random(seed)

        val worldWidth = 1400f
        val startPlatform = Platform(1, 0f, 250f, 160f, 60f, Color(0xFF3742FA))
        val endPlatform = Platform(99, worldWidth - 180f, 230f, 180f, 80f, Color(0xFF3742FA))

        val platforms = mutableListOf(startPlatform)

        val count = random.nextInt(5, 8)
        val stepX = (worldWidth - 340f) / count
        var currentX = 160f

        for (i in 1..count) {
            val pWidth = random.nextInt(65, 100).toFloat()
            val pY = random.nextInt(140, 240).toFloat()
            val trapRoll = random.nextInt(0, 5)
            val trapType = when (trapRoll) {
                0 -> TrapType.DISAPPEAR_ON_APPROACH
                1 -> TrapType.DISAPPEAR_ON_TOUCH
                2 -> TrapType.INVERT_CONTROLS
                3 -> TrapType.FALLING_BLOCK
                else -> TrapType.NONE
            }
            val pColor = when (trapType) {
                TrapType.DISAPPEAR_ON_APPROACH -> Color(0xFFE91E63)
                TrapType.DISAPPEAR_ON_TOUCH -> Color(0xFFFF9800)
                TrapType.INVERT_CONTROLS -> Color(0xFFFF5722)
                TrapType.FALLING_BLOCK -> Color(0xFF9C27B0)
                else -> Color(0xFF3742FA)
            }

            platforms.add(
                Platform(
                    id = i + 1,
                    x = currentX + random.nextInt(5, 15).toFloat(),
                    y = pY,
                    width = pWidth,
                    height = 18f,
                    color = pColor,
                    trapType = trapType,
                    triggerDistance = random.nextInt(80, 110).toFloat()
                )
            )
            currentX += stepX
        }
        platforms.add(endPlatform)

        val spikes = mutableListOf(
            Spike(1, 160f, 296f, worldWidth - 340f, 16f)
        )

        val numHiddenSpikes = random.nextInt(3, 5)
        for (s in 1..numHiddenSpikes) {
            val spikeX = random.nextInt(220, (worldWidth - 250).toInt()).toFloat()
            spikes.add(
                Spike(
                    s + 1, spikeX, 214f, 20f, 16f,
                    isHiddenSpike = true,
                    triggerDistance = random.nextInt(75, 95).toFloat()
                )
            )
        }

        val isMovingDoor = random.nextBoolean() || (levelNum % 2 == 1)
        val door = Door(
            x = worldWidth - 130f,
            y = 192f,
            width = 28f,
            height = 38f,
            isMovingDoor = isMovingDoor,
            targetX = worldWidth - 70f,
            triggerDistance = 90f
        )

        return Level(
            id = levelNum,
            name = "Level $levelNum: Wide Procedural Gauntlet",
            subtitle = "Infinite Troll Map #$levelNum",
            playerStart = Pair(50f, 220f),
            platforms = platforms,
            spikes = spikes,
            door = door,
            hint = "A giant wide level generated dynamically with troll traps!"
        )
    }
}

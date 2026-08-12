package com.example.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.game.engine.GameUiState
import com.example.game.model.TrapType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameCanvas(
    uiState: GameUiState,
    modifier: Modifier = Modifier
) {
    val virtualHeight = 320f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F1B))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scale = size.height / virtualHeight

            // Camera shake offsets
            val shakeX = if (uiState.cameraShake > 0) (Math.random().toFloat() * 2f - 1f) * uiState.cameraShake * scale else 0f
            val shakeY = if (uiState.cameraShake > 0) (Math.random().toFloat() * 2f - 1f) * uiState.cameraShake * scale else 0f

            val cameraX = uiState.cameraX + shakeX
            val cameraY = uiState.cameraY + shakeY

            // 1. Background Fill & Grid Lines
            drawRect(
                color = Color(0xFF0F0F1B),
                topLeft = Offset.Zero,
                size = size
            )

            val gridStep = 40f * scale
            val offsetX = -(cameraX * 0.3f * scale) % gridStep
            var gx = offsetX
            while (gx < size.width) {
                if (gx >= 0) {
                    drawLine(
                        color = Color(0xFF1C1C30),
                        start = Offset(gx, 0f),
                        end = Offset(gx, size.height),
                        strokeWidth = 1f
                    )
                }
                gx += gridStep
            }
            var gy = -(cameraY * 0.3f * scale) % gridStep
            while (gy < size.height) {
                if (gy >= 0) {
                    drawLine(
                        color = Color(0xFF1C1C30),
                        start = Offset(0f, gy),
                        end = Offset(size.width, gy),
                        strokeWidth = 1f
                    )
                }
                gy += gridStep
            }

            // 2. Teleporters
            uiState.teleporters.forEach { tp ->
                val tX = (tp.x - cameraX) * scale
                val tY = (tp.y - cameraY) * scale
                val tW = tp.width * scale
                val tH = tp.height * scale

                if (tX + tW >= 0f && tX <= size.width) {
                    // Portal Ring Outer Glow
                    drawCircle(
                        color = tp.color.copy(alpha = 0.3f),
                        center = Offset(tX + tW / 2f, tY + tH / 2f),
                        radius = tW * 0.8f
                    )
                    // Portal Ring Oval
                    drawOval(
                        color = tp.color,
                        topLeft = Offset(tX, tY),
                        size = Size(tW, tH),
                        style = Stroke(width = 4f * scale)
                    )
                    // Inner Portal Core
                    drawOval(
                        color = Color.White.copy(alpha = 0.8f),
                        topLeft = Offset(tX + tW * 0.25f, tY + tH * 0.25f),
                        size = Size(tW * 0.5f, tH * 0.5f)
                    )
                }
            }

            // 3. Spring Pads
            uiState.springs.forEach { sp ->
                val sX = (sp.x - cameraX) * scale
                val sY = (sp.y - cameraY) * scale
                val sW = sp.width * scale
                val sH = sp.height * scale

                if (sX + sW >= 0f && sX <= size.width) {
                    val animY = sY + (sp.compressionAnim * 6f * scale)
                    val animH = (sH - sp.compressionAnim * 6f * scale).coerceAtLeast(4f * scale)

                    // Base Plate
                    drawRect(
                        color = Color(0xFF57606F),
                        topLeft = Offset(sX, sY + sH - 4f * scale),
                        size = Size(sW, 4f * scale)
                    )
                    // Spring Pad Top
                    drawRoundRect(
                        color = Color(0xFF2ED573),
                        topLeft = Offset(sX, animY),
                        size = Size(sW, animH * 0.4f),
                        cornerRadius = CornerRadius(2f * scale, 2f * scale)
                    )
                    // Coil Lines
                    drawLine(
                        color = Color(0xFFA4B0BE),
                        start = Offset(sX + sW * 0.3f, animY + animH * 0.4f),
                        end = Offset(sX + sW * 0.7f, sY + sH - 4f * scale),
                        strokeWidth = 3f * scale
                    )
                }
            }

            // 4. Collectible Stars
            uiState.stars.forEach { star ->
                if (!star.isCollected) {
                    val stX = (star.x - cameraX) * scale
                    val stY = (star.y - cameraY) * scale
                    val stW = star.width * scale

                    if (stX + stW >= 0f && stX <= size.width) {
                        drawStar(
                            centerX = stX + stW / 2f,
                            centerY = stY + stW / 2f,
                            radius = stW / 2f,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }

            // 5. Checkpoints
            uiState.checkpoints.forEach { chk ->
                val cX = (chk.x - cameraX) * scale
                val cY = (chk.y - cameraY) * scale
                val cW = chk.width * scale
                val cH = chk.height * scale

                if (cX + cW >= 0f && cX <= size.width) {
                    // Pole
                    drawRect(
                        color = Color(0xFFA4B0BE),
                        topLeft = Offset(cX + cW * 0.45f, cY),
                        size = Size(3f * scale, cH)
                    )
                    // Flag
                    val flagColor = if (chk.isActivated) Color(0xFF2ED573) else Color(0xFFFF4757)
                    val flagPath = Path().apply {
                        moveTo(cX + cW * 0.48f, cY)
                        lineTo(cX + cW, cY + cH * 0.25f)
                        lineTo(cX + cW * 0.48f, cY + cH * 0.5f)
                        close()
                    }
                    drawPath(flagPath, color = flagColor)
                }
            }

            // 6. Platforms
            uiState.platforms.forEach { plat ->
                if (plat.isVisible) {
                    val pX = (plat.x - cameraX) * scale
                    val pY = (plat.y - cameraY) * scale
                    val pW = plat.width * scale
                    val pH = plat.height * scale

                    if (pX + pW >= 0f && pX <= size.width) {
                        val baseColor = when (plat.trapType) {
                            TrapType.SINKING_FLOOR -> Color(0xFF9C27B0) // Plum / Violet
                            TrapType.SLIPPERY_ICE -> Color(0xFF74B9FF) // Cyan Ice
                            TrapType.DISAPPEAR_ON_APPROACH, TrapType.DISAPPEAR_ON_TOUCH -> Color(0xFFFF9800)
                            TrapType.INVERT_CONTROLS -> Color(0xFFFF5722)
                            else -> plat.color
                        }

                        // Main Block
                        drawRoundRect(
                            color = baseColor,
                            topLeft = Offset(pX, pY),
                            size = Size(pW, pH),
                            cornerRadius = CornerRadius(3f * scale, 3f * scale)
                        )

                        // Top Highlight Edge
                        val highlightColor = if (plat.trapType == TrapType.SLIPPERY_ICE) Color.White else baseColor.copy(alpha = 0.3f)
                        drawRect(
                            color = highlightColor,
                            topLeft = Offset(pX, pY),
                            size = Size(pW, 3f * scale)
                        )

                        // Breakable Crack Visuals
                        if (plat.isBreakable) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.8f),
                                start = Offset(pX + pW * 0.2f, pY + pH * 0.1f),
                                end = Offset(pX + pW * 0.5f, pY + pH * 0.9f),
                                strokeWidth = 2f * scale
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.8f),
                                start = Offset(pX + pW * 0.5f, pY + pH * 0.9f),
                                end = Offset(pX + pW * 0.8f, pY + pH * 0.3f),
                                strokeWidth = 2f * scale
                            )
                        }
                    }
                }
            }

            // 7. Spikes
            uiState.spikes.forEach { spike ->
                if (spike.isVisible) {
                    val sX = (spike.x - cameraX) * scale
                    val sY = (spike.currentY - cameraY) * scale
                    val sW = spike.width * scale
                    val sH = spike.height * scale

                    if (sX + sW >= 0f && sX <= size.width) {
                        val numSpikes = (spike.width / 16f).toInt().coerceAtLeast(1)
                        val singleW = sW / numSpikes

                        for (i in 0 until numSpikes) {
                            val xStart = sX + i * singleW
                            val path = Path()
                            if (spike.isCeilingSpike) {
                                path.moveTo(xStart, sY)
                                path.lineTo(xStart + singleW / 2f, sY + sH)
                                path.lineTo(xStart + singleW, sY)
                            } else {
                                path.moveTo(xStart, sY + sH)
                                path.lineTo(xStart + singleW / 2f, sY)
                                path.lineTo(xStart + singleW, sY + sH)
                            }
                            path.close()

                            drawPath(path, color = Color(0xFFFF4757))
                            drawPath(path, color = Color(0xFF2F3542), style = Stroke(width = 1f * scale))
                        }
                    }
                }
            }

            // 8. Fleeing Keys
            uiState.keys.forEach { key ->
                if (!key.isCollected) {
                    val kX = (key.x - cameraX) * scale
                    val kY = (key.y - cameraY) * scale
                    val kW = key.width * scale
                    val kH = key.height * scale

                    if (kX + kW >= 0f && kX <= size.width) {
                        // Golden Key Ring
                        drawCircle(
                            color = Color(0xFFFFD700),
                            center = Offset(kX + kW * 0.3f, kY + kH * 0.3f),
                            radius = kW * 0.3f
                        )
                        // Shaft
                        drawRect(
                            color = Color(0xFFFFD700),
                            topLeft = Offset(kX + kW * 0.5f, kY + kH * 0.25f),
                            size = Size(kW * 0.45f, kH * 0.15f)
                        )
                        // Teeth
                        drawRect(
                            color = Color(0xFFFFD700),
                            topLeft = Offset(kX + kW * 0.75f, kY + kH * 0.4f),
                            size = Size(kW * 0.15f, kH * 0.25f)
                        )
                    }
                }
            }

            // 9. Door (Exit Goal)
            val door = uiState.door
            val dX = (door.x - cameraX) * scale
            val dY = (door.y - cameraY) * scale
            val dW = door.width * scale
            val dH = door.height * scale

            if (dX + dW >= 0f && dX <= size.width) {
                val archColor = if (door.isLocked) Color(0xFFFF4757) else Color(0xFF2ED573)

                // Frame
                drawRoundRect(
                    color = archColor,
                    topLeft = Offset(dX, dY),
                    size = Size(dW, dH),
                    cornerRadius = CornerRadius(10f * scale, 10f * scale)
                )
                // Door Opening
                drawRoundRect(
                    color = Color(0xFF1E272E),
                    topLeft = Offset(dX + dW * 0.15f, dY + dH * 0.15f),
                    size = Size(dW * 0.7f, dH * 0.85f),
                    cornerRadius = CornerRadius(6f * scale, 6f * scale)
                )
                // Lock Icon
                if (door.isLocked) {
                    drawCircle(
                        color = Color(0xFFFFD700),
                        center = Offset(dX + dW / 2f, dY + dH * 0.5f),
                        radius = 6f * scale
                    )
                }
            }

            // 10. Particles
            uiState.particles.forEach { p ->
                val pX = (p.x - cameraX) * scale
                val pY = (p.y - cameraY) * scale
                val pS = p.size * scale

                if (pX >= 0f && pX <= size.width) {
                    drawCircle(
                        color = p.color.copy(alpha = p.alpha.coerceIn(0f, 1f)),
                        center = Offset(pX, pY),
                        radius = pS
                    )
                }
            }

            // 11. Player Character
            if (!uiState.isDead) {
                val player = uiState.player
                val plX = (player.x - cameraX) * scale
                val plY = (player.y - cameraY) * scale
                val plW = player.width * scale
                val plH = player.height * scale

                if (plX + plW >= 0f && plX <= size.width) {
                    val bodyColor = if (player.isGiant) Color(0xFFFF4757) else if (player.isTiny) Color(0xFF00D2D3) else Color(0xFFFFA502)

                    // Character Body
                    drawRoundRect(
                        color = bodyColor,
                        topLeft = Offset(plX, plY),
                        size = Size(plW, plH),
                        cornerRadius = CornerRadius(4f * scale, 4f * scale)
                    )

                    // Expressive Eyes
                    val eyeOffsetX = if (player.facingRight) plW * 0.65f else plW * 0.25f
                    val eyeY = plY + plH * 0.3f
                    val eyeSize = 3f * scale

                    drawCircle(
                        color = Color.White,
                        center = Offset(plX + eyeOffsetX, eyeY),
                        radius = eyeSize
                    )
                    drawCircle(
                        color = Color.Black,
                        center = Offset(plX + eyeOffsetX + (if (player.facingRight) 1f else -1f) * scale, eyeY),
                        radius = eyeSize * 0.5f
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawStar(centerX: Float, centerY: Float, radius: Float, color: Color) {
    val path = Path()
    val innerRadius = radius * 0.4f
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = Math.PI / 5 * i - Math.PI / 2
        val x = (centerX + cos(angle) * r).toFloat()
        val y = (centerY + sin(angle) * r).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
}

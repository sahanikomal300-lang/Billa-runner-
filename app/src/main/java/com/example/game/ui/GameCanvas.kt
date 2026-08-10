package com.example.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.game.engine.GameUiState
import kotlin.math.abs
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
            val visibleWorldWidth = size.width / scale
            val cameraX = uiState.cameraX

            // 1. Background Fill & Subtle Grid Lines
            drawRect(
                color = Color(0xFF0F0F1B),
                topLeft = Offset.Zero,
                size = size
            )

            // Faint grid pattern for indie dark retro vibe
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
            var gy = 0f
            while (gy < size.height) {
                drawLine(
                    color = Color(0xFF1C1C30),
                    start = Offset(0f, gy),
                    end = Offset(size.width, gy),
                    strokeWidth = 1f
                )
                gy += gridStep
            }

            // 2. Draw Thin Platforms
            uiState.platforms.forEach { platform ->
                if (!platform.isVisible) return@forEach

                val pX = (platform.x - cameraX) * scale
                val pY = platform.y * scale
                val pW = platform.width * scale
                val pH = platform.height * scale

                if (pX + pW < 0f || pX > size.width) return@forEach

                // Platform Body
                drawRect(
                    color = platform.color,
                    topLeft = Offset(pX, pY),
                    size = Size(pW, pH)
                )

                // Top Accent Highlight (2dp thin)
                val highlightHeight = (2.5f * scale).coerceAtLeast(1.5f)
                drawRect(
                    color = Color.White.copy(alpha = 0.35f),
                    topLeft = Offset(pX, pY),
                    size = Size(pW, highlightHeight)
                )

                // Bottom Shadow Line
                drawRect(
                    color = Color.Black.copy(alpha = 0.3f),
                    topLeft = Offset(pX, pY + pH - highlightHeight),
                    size = Size(pW, highlightHeight)
                )
            }

            // 3. Draw Small Sharp Spikes
            uiState.spikes.forEach { spike ->
                val sy = if (spike.isHiddenSpike) spike.currentY else spike.y
                val sX = (spike.x - cameraX) * scale
                val sY = sy * scale
                val sW = spike.width * scale
                val sH = spike.height * scale

                if (sX + sW < 0f || sX > size.width) return@forEach

                val spikeBaseWidth = 16f * scale
                val numTriangles = maxOf(1, (sW / spikeBaseWidth).toInt())
                val triangleWidth = sW / numTriangles

                for (i in 0 until numTriangles) {
                    val x0 = sX + i * triangleWidth
                    val x1 = x0 + triangleWidth / 2f
                    val x2 = x0 + triangleWidth
                    val yBottom = sY + sH
                    val yTop = sY

                    val spikePath = Path().apply {
                        moveTo(x0, yBottom)
                        lineTo(x1, yTop)
                        lineTo(x2, yBottom)
                        close()
                    }
                    drawPath(spikePath, color = Color(0xFFFF4757))
                }
            }

            // 4. Draw Exit Door
            val door = uiState.door
            val dX = (door.x - cameraX) * scale
            val dY = door.y * scale
            val dW = door.width * scale
            val dH = door.height * scale

            if (dX + dW >= 0f && dX <= size.width) {
                // Outer Arch
                drawRoundRect(
                    color = Color(0xFF2ED573),
                    topLeft = Offset(dX, dY),
                    size = Size(dW, dH),
                    cornerRadius = CornerRadius(6f * scale, 6f * scale)
                )
                // Inner Dark Portal
                drawRoundRect(
                    color = Color(0xFF0F0F1B),
                    topLeft = Offset(dX + dW * 0.2f, dY + dH * 0.2f),
                    size = Size(dW * 0.6f, dH * 0.75f),
                    cornerRadius = CornerRadius(4f * scale, 4f * scale)
                )
                // Golden Door Knob
                drawCircle(
                    color = Color(0xFFFFA502),
                    radius = 2.5f * scale,
                    center = Offset(dX + dW * 0.72f, dY + dH * 0.58f)
                )
            }

            // 5. Draw Small Proportional Player
            val player = uiState.player
            if (!uiState.isDead) {
                val plX = (player.x - cameraX) * scale
                val plY = player.y * scale
                val plW = player.width * scale
                val plH = player.height * scale

                val isMoving = abs(player.vx) > 0.2f
                val legSwing = if (isMoving) sin(player.x * 0.25f) * 4f * scale else 0f

                // Legs
                val legW = 3.5f * scale
                val legH = 7f * scale
                val leftLegX = plX + plW * 0.2f + legSwing
                val rightLegX = plX + plW * 0.6f - legSwing
                val legY = plY + plH - 2f * scale

                drawRect(color = Color(0xFFD63031), topLeft = Offset(leftLegX, legY), size = Size(legW, legH))
                drawRect(color = Color(0xFFD63031), topLeft = Offset(rightLegX, legY), size = Size(legW, legH))

                // Arms
                val armW = 3.5f * scale
                val armH = 7f * scale
                val armY = if (player.isGrounded) plY + plH * 0.35f else plY + plH * 0.1f
                drawRect(color = Color(0xFFD63031), topLeft = Offset(plX - armW * 0.8f, armY), size = Size(armW, armH))
                drawRect(color = Color(0xFFD63031), topLeft = Offset(plX + plW - armW * 0.2f, armY), size = Size(armW, armH))

                // Devil Body
                val bodyPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = androidx.compose.ui.geometry.Rect(plX, plY, plX + plW, plY + plH),
                            cornerRadius = CornerRadius(4f * scale, 4f * scale)
                        )
                    )
                }
                drawPath(bodyPath, color = Color(0xFFFFA502))

                // Red Horns
                val hornPath = Path().apply {
                    // Left Horn
                    moveTo(plX + plW * 0.2f, plY)
                    lineTo(plX + plW * 0.05f, plY - 6f * scale)
                    lineTo(plX + plW * 0.38f, plY)
                    close()

                    // Right Horn
                    moveTo(plX + plW * 0.62f, plY)
                    lineTo(plX + plW * 0.95f, plY - 6f * scale)
                    lineTo(plX + plW * 0.8f, plY)
                    close()
                }
                drawPath(hornPath, color = Color(0xFFFF4757))

                // Expressive Eyes
                val eyeY = plY + plH * 0.25f
                val eyeSize = 6f * scale
                val eye1X = if (player.facingRight) plX + plW * 0.45f else plX + plW * 0.15f
                val eye2X = if (player.facingRight) plX + plW * 0.72f else plX + plW * 0.42f

                drawCircle(color = Color.White, radius = eyeSize / 2f, center = Offset(eye1X + eyeSize / 2f, eyeY + eyeSize / 2f))
                drawCircle(color = Color.White, radius = eyeSize / 2f, center = Offset(eye2X + eyeSize / 2f, eyeY + eyeSize / 2f))

                val pupilOffset = if (player.facingRight) 1.2f * scale else -1.2f * scale
                drawCircle(color = Color(0xFF0F0F1B), radius = 1.8f * scale, center = Offset(eye1X + eyeSize / 2f + pupilOffset, eyeY + eyeSize / 2f))
                drawCircle(color = Color(0xFF0F0F1B), radius = 1.8f * scale, center = Offset(eye2X + eyeSize / 2f + pupilOffset, eyeY + eyeSize / 2f))
            } else {
                // Particles
                uiState.particles.forEach { particle ->
                    val ptX = (particle.x - cameraX) * scale
                    val ptY = particle.y * scale
                    val ptSize = particle.size * scale

                    drawRect(
                        color = particle.color.copy(alpha = particle.alpha.coerceIn(0f, 1f)),
                        topLeft = Offset(ptX, ptY),
                        size = Size(ptSize, ptSize)
                    )
                }
            }
        }
    }
}

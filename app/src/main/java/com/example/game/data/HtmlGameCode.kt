package com.example.game.data

object HtmlGameCode {

    val fullHtmlCode: String = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover">
    <title>Level Devil - Minimal Small-Scale Platformer</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            user-select: none;
            -webkit-user-select: none;
            -webkit-touch-callout: none;
        }
        html, body {
            width: 100vw;
            height: 100dvh;
            overflow: hidden;
            background-color: #0f0f1b;
            font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            touch-action: none;
        }
        #game-container {
            position: relative;
            width: 100vw;
            height: 100dvh;
            overflow: hidden;
            background-color: #0f0f1b;
        }
        canvas {
            display: block;
            width: 100vw;
            height: 100dvh;
            touch-action: none;
        }

        /* Portrait Warning Overlay */
        #portrait-warning {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100vw;
            height: 100dvh;
            background: #0f0f1b;
            color: #ffffff;
            z-index: 99999;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 24px;
        }
        @media (orientation: portrait) {
            #portrait-warning {
                display: flex;
            }
        }

        /* HUD Overlay */
        #hud-top {
            position: absolute;
            top: 8px;
            left: 16px;
            right: 16px;
            z-index: 100;
            display: flex;
            justify-content: space-between;
            align-items: center;
            pointer-events: none;
        }
        .hud-chip {
            background: rgba(30, 27, 46, 0.85);
            backdrop-filter: blur(4px);
            border: 1px solid rgba(255,255,255,0.12);
            padding: 5px 12px;
            border-radius: 8px;
            color: white;
            font-weight: bold;
            font-size: 13px;
            pointer-events: auto;
        }
        .btn-action {
            background: rgba(255, 165, 2, 0.85);
            border: none;
            color: #0f0f1b;
            font-weight: 900;
            font-size: 11px;
            padding: 6px 12px;
            border-radius: 6px;
            cursor: pointer;
            pointer-events: auto;
            text-transform: uppercase;
            box-shadow: 0 3px 8px rgba(0,0,0,0.3);
        }
        .btn-action:active {
            transform: scale(0.95);
        }

        /* Touch On-Screen Controls */
        #touch-controls {
            position: absolute;
            bottom: 12px;
            left: 20px;
            right: 20px;
            z-index: 100;
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            pointer-events: none;
        }
        .touch-btn {
            background: rgba(47, 53, 66, 0.65);
            border: 1px solid rgba(255, 255, 255, 0.25);
            color: white;
            font-size: 22px;
            width: 56px;
            height: 56px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            touch-action: manipulation;
            pointer-events: auto;
            box-shadow: 0 4px 10px rgba(0,0,0,0.3);
            user-select: none;
        }
        .touch-btn:active {
            background: #ff4757;
            border-color: #ff6b81;
        }
        #btn-jump {
            width: 64px;
            height: 64px;
            background: rgba(255, 71, 87, 0.75);
            border-color: rgba(255, 107, 129, 0.8);
            font-size: 11px;
            font-weight: 900;
            letter-spacing: 0.5px;
        }
        #btn-jump:active {
            background: #ff6b81;
        }
    </style>
</head>
<body>

    <!-- Portrait Warning -->
    <div id="portrait-warning">
        <div style="font-size: 64px; margin-bottom: 16px;">📱</div>
        <div style="color: #ff4757; font-size: 24px; font-weight: 900; margin-bottom: 10px;">ROTATE YOUR PHONE</div>
        <div style="color: #a4b0be; font-size: 14px; max-width: 300px; line-height: 1.4;">
            Level Devil is designed for landscape mode. Please turn your device sideways!
        </div>
    </div>

    <div id="game-container">
        <!-- HUD Overlay -->
        <div id="hud-top">
            <div class="hud-chip" id="hud-level-name">Level 1: Looks Easy</div>
            <div style="display: flex; gap: 6px; align-items: center;">
                <div class="hud-chip" style="background: rgba(255, 71, 87, 0.85);" id="hud-fails">FAILS: 0</div>
                <button class="btn-action" onclick="toggleFullscreen()">FULLSCREEN</button>
            </div>
        </div>

        <canvas id="gameCanvas"></canvas>

        <!-- Touch Controls -->
        <div id="touch-controls">
            <div style="display: flex; gap: 10px;">
                <div class="touch-btn" id="btn-left">←</div>
                <div class="touch-btn" id="btn-right">→</div>
            </div>
            <div class="touch-btn" id="btn-jump">JUMP</div>
        </div>
    </div>

    <script>
        const canvas = document.getElementById('gameCanvas');
        const ctx = canvas.getContext('2d');

        function resizeCanvas() {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
        }
        window.addEventListener('resize', resizeCanvas);
        window.addEventListener('orientationchange', () => setTimeout(resizeCanvas, 200));
        document.addEventListener('fullscreenchange', resizeCanvas);
        resizeCanvas();

        function toggleFullscreen() {
            if (!document.fullscreenElement) {
                document.documentElement.requestFullscreen().catch(err => {});
            } else {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                }
            }
        }

        let currentLevel = 0;
        let deaths = 0;
        let gameState = 'PLAYING';
        let particles = [];
        let keys = { left: false, right: false, up: false };
        let controlsInverted = false;
        let deathMessage = "";
        let cameraX = 0;

        const taunts = [
            "Nice try, human!", "Level Devil 1 - You 0!", "Did you think it was that easy?",
            "Gravity says hi!", "Gravity: 100, Reflexes: 0", "Classic trap!", "Spikes love hugs!"
        ];

        // Small, proportional Player
        const player = {
            x: 50,
            y: 220,
            w: 24,
            h: 28,
            vx: 0,
            vy: 0,
            speed: 5.2,
            accel: 0.22,
            jumpPower: -11.5,
            gravity: 0.52,
            grounded: false,
            facingRight: true
        };

        const baseLevels = [
            {
                name: "Level 1: Looks Easy",
                worldWidth: 1200,
                start: { x: 50, y: 220 },
                door: { x: 730, y: 212, w: 28, h: 38, targetX: 730, moved: false },
                platforms: [
                    { x: 0, y: 250, w: 220, h: 60, color: '#3742fa' },
                    { x: 250, y: 250, w: 150, h: 60, color: '#3742fa', trap: 'disappear', visible: true, trigDist: 110 },
                    { x: 280, y: 285, w: 90, h: 20, color: '#e91e63' },
                    { x: 520, y: 250, w: 280, h: 60, color: '#3742fa' }
                ],
                spikes: [
                    { x: 220, y: 296, w: 300, h: 16, hidden: false, currentY: 296 }
                ]
            },
            {
                name: "Level 2: Watch Your Step",
                worldWidth: 1400,
                start: { x: 50, y: 220 },
                door: { x: 920, y: 212, w: 28, h: 38, targetX: 920, moved: false },
                platforms: [
                    { x: 0, y: 250, w: 1000, h: 60, color: '#3742fa' }
                ],
                spikes: [
                    { x: 220, y: 234, w: 24, h: 16, hidden: false, currentY: 234 },
                    { x: 340, y: 234, w: 24, h: 16, hidden: true, trigDist: 90, targetY: 234, currentY: 264 },
                    { x: 480, y: 234, w: 24, h: 16, hidden: true, trigDist: 90, targetY: 234, currentY: 264 },
                    { x: 620, y: 234, w: 24, h: 16, hidden: true, trigDist: 95, targetY: 234, currentY: 264 },
                    { x: 760, y: 234, w: 24, h: 16, hidden: true, trigDist: 95, targetY: 234, currentY: 264 }
                ]
            },
            {
                name: "Level 3: Shy Door",
                worldWidth: 1400,
                start: { x: 50, y: 220 },
                door: { x: 520, y: 212, w: 28, h: 38, targetX: 900, moved: false, trigDist: 100 },
                platforms: [
                    { x: 0, y: 250, w: 1000, h: 60, color: '#3742fa' },
                    { x: 400, y: 190, w: 80, h: 60, color: '#5f27cd' }
                ],
                spikes: [
                    { x: 400, y: 174, w: 80, h: 16, hidden: false, currentY: 174 }
                ]
            },
            {
                name: "Level 4: Mind Flip",
                worldWidth: 1400,
                start: { x: 50, y: 220 },
                door: { x: 720, y: 212, w: 28, h: 38, targetX: 720, moved: false },
                platforms: [
                    { x: 0, y: 250, w: 220, h: 60, color: '#009688' },
                    { x: 280, y: 220, w: 140, h: 90, color: '#ff5722', trap: 'invert' },
                    { x: 490, y: 250, w: 300, h: 60, color: '#009688' }
                ],
                spikes: [
                    { x: 220, y: 296, w: 270, h: 16, hidden: false, currentY: 296 }
                ]
            }
        ];

        let activeLevelData = null;

        function loadLevel(index) {
            currentLevel = index;
            controlsInverted = false;
            cameraX = 0;

            if (currentLevel < baseLevels.length) {
                activeLevelData = JSON.parse(JSON.stringify(baseLevels[currentLevel]));
            } else {
                activeLevelData = generateProceduralLevel(currentLevel);
            }

            document.getElementById('hud-level-name').innerText = "Level " + (currentLevel + 1) + ": " + activeLevelData.name;

            player.x = activeLevelData.start.x;
            player.y = activeLevelData.start.y;
            player.vx = 0;
            player.vy = 0;
            player.grounded = false;

            activeLevelData.platforms.forEach(p => {
                p.visible = true;
                p.touched = false;
            });

            activeLevelData.door.moved = false;

            activeLevelData.spikes.forEach(s => {
                if (s.hidden) s.currentY = s.targetY + 30;
            });

            gameState = 'PLAYING';
        }

        window.addEventListener('keydown', (e) => {
            if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') keys.left = true;
            if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') keys.right = true;
            if (e.key === 'ArrowUp' || e.key === 'w' || e.key === 'W' || e.key === ' ') {
                keys.up = true;
                if (gameState === 'DEAD' || gameState === 'WIN') {
                    if (gameState === 'WIN') loadLevel(currentLevel + 1);
                    else loadLevel(currentLevel);
                }
            }
            if (e.key === 'r' || e.key === 'R') loadLevel(currentLevel);
        });

        window.addEventListener('keyup', (e) => {
            if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') keys.left = false;
            if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') keys.right = false;
            if (e.key === 'ArrowUp' || e.key === 'w' || e.key === 'W' || e.key === ' ') keys.up = false;
        });

        const setupTouch = (btnId, keyName) => {
            const btn = document.getElementById(btnId);
            const handlePress = () => {
                keys[keyName] = true;
                if (gameState === 'DEAD' || gameState === 'WIN') {
                    if (gameState === 'WIN') loadLevel(currentLevel + 1);
                    else loadLevel(currentLevel);
                }
            };
            btn.addEventListener('touchstart', (e) => { e.preventDefault(); handlePress(); });
            btn.addEventListener('touchend', (e) => { e.preventDefault(); keys[keyName] = false; });
            btn.addEventListener('mousedown', () => handlePress());
            btn.addEventListener('mouseup', () => keys[keyName] = false);
        };
        setupTouch('btn-left', 'left');
        setupTouch('btn-right', 'right');
        setupTouch('btn-jump', 'up');

        canvas.addEventListener('click', () => {
            if (gameState === 'DEAD' || gameState === 'WIN') {
                if (gameState === 'WIN') loadLevel(currentLevel + 1);
                else loadLevel(currentLevel);
            }
        });

        function triggerDeath() {
            if (gameState === 'DEAD') return;
            gameState = 'DEAD';
            deaths++;
            document.getElementById('hud-fails').innerText = "FAILS: " + deaths;
            deathMessage = taunts[Math.floor(Math.random() * taunts.length)];

            particles = [];
            for (let i = 0; i < 30; i++) {
                particles.push({
                    x: player.x + player.w / 2,
                    y: player.y + player.h / 2,
                    vx: (Math.random() - 0.5) * 10,
                    vy: (Math.random() - 0.5) * 10,
                    size: Math.random() * 5 + 3,
                    color: Math.random() > 0.5 ? '#ff4757' : '#ffa502',
                    alpha: 1
                });
            }
        }

        function update() {
            if (gameState === 'DEAD') {
                particles.forEach(p => {
                    p.x += p.vx;
                    p.y += p.vy;
                    p.alpha -= 0.03;
                });
                return;
            }

            if (gameState === 'WIN') return;

            const lvl = activeLevelData;

            let dirLeft = controlsInverted ? keys.right : keys.left;
            let dirRight = controlsInverted ? keys.left : keys.right;

            let targetVx = 0;
            if (dirLeft) {
                targetVx = -player.speed;
                player.facingRight = false;
            } else if (dirRight) {
                targetVx = player.speed;
                player.facingRight = true;
            }

            player.vx += (targetVx - player.vx) * player.accel;

            if (keys.up && player.grounded) {
                player.vy = player.jumpPower;
                player.grounded = false;
            }

            player.vy += player.gravity;

            // Horizontal Collision
            player.x += player.vx;
            if (player.x < 0) player.x = 0;

            lvl.platforms.forEach(p => {
                if (!p.visible) return;
                if (player.x < p.x + p.w && player.x + player.w > p.x &&
                    player.y < p.y + p.h && player.y + player.h > p.y) {
                    if (player.vx > 0) player.x = p.x - player.w;
                    else if (player.vx < 0) player.x = p.x + p.w;
                }
            });

            // Vertical Collision
            player.y += player.vy;
            player.grounded = false;

            lvl.platforms.forEach(p => {
                if (!p.visible) return;
                if (player.x < p.x + p.w && player.x + player.w > p.x &&
                    player.y < p.y + p.h && player.y + player.h > p.y) {

                    if (player.vy > 0) {
                        player.y = p.y - player.h;
                        player.vy = 0;
                        player.grounded = true;

                        if (p.trap === 'touch_disappear' && !p.touched) {
                            p.touched = true;
                            setTimeout(() => { p.visible = false; }, 160);
                        }
                    } else if (player.vy < 0) {
                        player.y = p.y + p.h;
                        player.vy = 0;
                    }

                    if (p.trap === 'invert') {
                        controlsInverted = true;
                    }
                }

                if (p.trap === 'disappear' && p.visible) {
                    let dist = Math.abs((player.x + player.w / 2) - (p.x + p.w / 2));
                    if (dist < (p.trigDist || 110)) {
                        p.visible = false;
                    }
                }
            });

            // Spikes
            lvl.spikes.forEach(s => {
                let sy = s.hidden ? s.currentY : s.y;

                if (player.x < s.x + s.w && player.x + player.w > s.x &&
                    player.y < sy + s.h && player.y + player.h > sy) {
                    triggerDeath();
                }

                if (s.hidden) {
                    let dist = Math.abs((player.x + player.w / 2) - (s.x + s.w / 2));
                    if (dist < (s.trigDist || 90)) {
                        s.currentY += (s.targetY - s.currentY) * 0.25;
                    }
                }
            });

            // Door
            let door = lvl.door;
            if (door.trigDist) {
                let distToDoor = Math.hypot((player.x - door.x), (player.y - door.y));
                if (distToDoor < door.trigDist && !door.moved) {
                    door.x = door.targetX;
                    door.moved = true;
                }
            }

            if (player.x < door.x + door.w && player.x + player.w > door.x &&
                player.y < door.y + door.h && player.y + player.h > door.y) {
                gameState = 'WIN';
            }

            if (player.y > 340) {
                triggerDeath();
            }

            // Smooth Camera Lerp
            let targetCamX = Math.max(0, player.x - 200);
            cameraX += (targetCamX - cameraX) * 0.12;
        }

        function draw() {
            const VIRTUAL_HEIGHT = 320;
            const scale = canvas.height / VIRTUAL_HEIGHT;

            const lvl = activeLevelData;

            ctx.fillStyle = '#0f0f1b';
            ctx.fillRect(0, 0, canvas.width, canvas.height);

            // Subtle Grid
            let gridStep = 40 * scale;
            let offsetX = -(cameraX * 0.3 * scale) % gridStep;
            ctx.strokeStyle = '#1c1c30';
            ctx.lineWidth = 1;
            for (let gx = offsetX; gx < canvas.width; gx += gridStep) {
                if (gx >= 0) {
                    ctx.beginPath();
                    ctx.moveTo(gx, 0);
                    ctx.lineTo(gx, canvas.height);
                    ctx.stroke();
                }
            }
            for (let gy = 0; gy < canvas.height; gy += gridStep) {
                ctx.beginPath();
                ctx.moveTo(0, gy);
                ctx.lineTo(canvas.width, gy);
                ctx.stroke();
            }

            ctx.save();
            ctx.scale(scale, scale);
            ctx.translate(-cameraX, 0);

            // Platforms
            lvl.platforms.forEach(p => {
                if (!p.visible) return;
                ctx.fillStyle = p.color;
                ctx.fillRect(p.x, p.y, p.w, p.h);
                ctx.fillStyle = 'rgba(255,255,255,0.35)';
                ctx.fillRect(p.x, p.y, p.w, 2.5);
            });

            // Spikes
            lvl.spikes.forEach(s => {
                let sy = s.hidden ? s.currentY : s.y;
                ctx.fillStyle = '#ff4757';
                let count = Math.max(1, Math.floor(s.w / 16));
                let step = s.w / count;
                for (let i = 0; i < count; i++) {
                    ctx.beginPath();
                    ctx.moveTo(s.x + i * step, sy + s.h);
                    ctx.lineTo(s.x + (i + 0.5) * step, sy);
                    ctx.lineTo(s.x + (i + 1) * step, sy + s.h);
                    ctx.closePath();
                    ctx.fill();
                }
            });

            // Door
            let d = lvl.door;
            ctx.fillStyle = '#2ed573';
            ctx.fillRect(d.x, d.y, d.w, d.h);
            ctx.fillStyle = '#0f0f1b';
            ctx.fillRect(d.x + 5, d.y + 8, d.w - 10, d.h - 8);
            ctx.fillStyle = '#ffa502';
            ctx.beginPath();
            ctx.arc(d.x + d.w - 7, d.y + d.h / 2, 2.5, 0, Math.PI * 2);
            ctx.fill();

            // Player
            if (gameState !== 'DEAD') {
                let px = player.x;
                let py = player.y;
                let pw = player.w;
                let ph = player.h;

                let isMoving = Math.abs(player.vx) > 0.2;
                let legSwing = isMoving ? Math.sin(player.x * 0.25) * 4 : 0;

                ctx.fillStyle = '#d63031';
                ctx.fillRect(px + pw * 0.2 + legSwing, py + ph - 2, 3.5, 7);
                ctx.fillRect(px + pw * 0.6 - legSwing, py + ph - 2, 3.5, 7);

                let armY = player.grounded ? py + ph * 0.35 : py + ph * 0.1;
                ctx.fillRect(px - 3, armY, 3.5, 7);
                ctx.fillRect(px + pw - 1, armY, 3.5, 7);

                ctx.fillStyle = '#ffa502';
                ctx.fillRect(px, py, pw, ph);

                // Horns
                ctx.fillStyle = '#ff4757';
                ctx.beginPath();
                ctx.moveTo(px + pw * 0.2, py);
                ctx.lineTo(px + pw * 0.05, py - 6);
                ctx.lineTo(px + pw * 0.38, py);
                ctx.closePath();
                ctx.fill();

                ctx.beginPath();
                ctx.moveTo(px + pw * 0.62, py);
                ctx.lineTo(px + pw * 0.95, py - 6);
                ctx.lineTo(px + pw * 0.8, py);
                ctx.closePath();
                ctx.fill();

                // Eyes
                let eyeY = py + ph * 0.25;
                let eye1X = player.facingRight ? px + pw * 0.45 : px + pw * 0.15;
                let eye2X = player.facingRight ? px + pw * 0.72 : px + pw * 0.42;

                ctx.fillStyle = '#ffffff';
                ctx.beginPath();
                ctx.arc(eye1X + 3, eyeY + 3, 3, 0, Math.PI * 2);
                ctx.arc(eye2X + 3, eyeY + 3, 3, 0, Math.PI * 2);
                ctx.fill();

                let pupilOffset = player.facingRight ? 1.2 : -1.2;
                ctx.fillStyle = '#0f0f1b';
                ctx.beginPath();
                ctx.arc(eye1X + 3 + pupilOffset, eyeY + 3, 1.8, 0, Math.PI * 2);
                ctx.arc(eye2X + 3 + pupilOffset, eyeY + 3, 1.8, 0, Math.PI * 2);
                ctx.fill();
            } else {
                particles.forEach(p => {
                    ctx.fillStyle = p.color;
                    ctx.globalAlpha = Math.max(0, p.alpha);
                    ctx.fillRect(p.x, p.y, p.size, p.size);
                    ctx.globalAlpha = 1;
                });
            }

            ctx.restore();

            if (controlsInverted) {
                ctx.fillStyle = '#ff4757';
                ctx.font = 'bold 14px sans-serif';
                ctx.textAlign = 'center';
                ctx.fillText("⚠️ CONTROLS FLIPPED!", canvas.width / 2, 50);
                ctx.textAlign = 'left';
            }

            if (gameState === 'DEAD') {
                ctx.fillStyle = 'rgba(15, 15, 27, 0.88)';
                ctx.fillRect(0, 0, canvas.width, canvas.height);

                ctx.fillStyle = '#ff4757';
                ctx.font = 'bold ' + Math.max(22, Math.floor(canvas.height * 0.08)) + 'px sans-serif';
                ctx.textAlign = 'center';
                ctx.fillText("You Failed! 😂", canvas.width / 2, canvas.height / 2 - 20);

                ctx.fillStyle = '#a4b0be';
                ctx.font = Math.max(13, Math.floor(canvas.height * 0.04)) + 'px sans-serif';
                ctx.fillText(deathMessage, canvas.width / 2, canvas.height / 2 + 15);
                ctx.fillText("Tap screen or press JUMP to retry", canvas.width / 2, canvas.height / 2 + 45);
                ctx.textAlign = 'left';
            } else if (gameState === 'WIN') {
                ctx.fillStyle = 'rgba(15, 15, 27, 0.88)';
                ctx.fillRect(0, 0, canvas.width, canvas.height);

                ctx.fillStyle = '#2ed573';
                ctx.font = 'bold ' + Math.max(24, Math.floor(canvas.height * 0.08)) + 'px sans-serif';
                ctx.textAlign = 'center';
                ctx.fillText("LEVEL CLEARED! 🎉", canvas.width / 2, canvas.height / 2 - 20);

                ctx.fillStyle = '#ffffff';
                ctx.font = Math.max(13, Math.floor(canvas.height * 0.04)) + 'px sans-serif';
                ctx.fillText("Tap screen or press JUMP for next level", canvas.width / 2, canvas.height / 2 + 25);
                ctx.textAlign = 'left';
            }
        }

        function gameLoop() {
            update();
            draw();
            requestAnimationFrame(gameLoop);
        }

        loadLevel(0);
        gameLoop();
    </script>
</body>
</html>
"""
}

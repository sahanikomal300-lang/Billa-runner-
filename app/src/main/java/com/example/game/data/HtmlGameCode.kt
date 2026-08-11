package com.example.game.data

object HtmlGameCode {

    val fullHtmlCode: String = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover">
    <title>Level Devil - Upgraded Troll Platformer</title>
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

        /* Banner Notification Pop-up */
        #banner-toast {
            position: absolute;
            top: 48px;
            left: 50%;
            transform: translateX(-50%);
            z-index: 150;
            background: #ffa502;
            color: #0f0f1b;
            font-weight: 900;
            font-size: 14px;
            padding: 6px 16px;
            border-radius: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.4);
            opacity: 0;
            transition: opacity 0.3s ease;
            pointer-events: none;
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

        <div id="banner-toast">GIANT MODE!</div>

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
        const bannerEl = document.getElementById('banner-toast');

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
        let reverseGravity = false;
        let deathMessage = "";
        let cameraX = 0;
        let activeCheckpoint = null;
        let bannerTimer = null;

        function showBanner(text, bgColor = '#ffa502', textColor = '#0f0f1b') {
            bannerEl.innerText = text;
            bannerEl.style.backgroundColor = bgColor;
            bannerEl.style.color = textColor;
            bannerEl.style.opacity = '1';
            if (bannerTimer) clearTimeout(bannerTimer);
            bannerTimer = setTimeout(() => {
                bannerEl.style.opacity = '0';
            }, 1500);
        }

        const taunts = [
            "Nice try, human!", "Level Devil 1 - You 0!", "Did you think it was that easy?",
            "Gravity says hi!", "Gravity: 100, Reflexes: 0", "Classic trap!", "Spikes love hugs!",
            "Troll mode engaged! 😂", "Look before you leap!"
        ];

        // Player object with smooth size interpolation
        const player = {
            x: 50,
            y: 220,
            w: 24,
            h: 28,
            targetW: 24,
            targetH: 28,
            vx: 0,
            vy: 0,
            speed: 5.0,
            accel: 0.22,
            jumpPower: -15.5,
            gravity: 0.45,
            grounded: false,
            facingRight: true
        };

        const baseLevels = [
            // Level 1: Looks Easy
            {
                name: "Level 1: Looks Easy",
                worldWidth: 1000,
                start: { x: 50, y: 220 },
                door: { x: 730, y: 212, w: 28, h: 38 },
                platforms: [
                    { x: 0, y: 250, w: 220, h: 60, color: '#3742fa' },
                    { x: 250, y: 250, w: 150, h: 60, color: '#3742fa', trap: 'disappear', visible: true, trigDist: 110 },
                    { x: 280, y: 285, w: 90, h: 20, color: '#e91e63' },
                    { x: 520, y: 250, w: 280, h: 60, color: '#3742fa' }
                ],
                spikes: [
                    { x: 220, y: 296, w: 300, h: 16 }
                ]
            },
            // Level 2: Watch Your Step
            {
                name: "Level 2: Watch Your Step",
                worldWidth: 1100,
                start: { x: 50, y: 220 },
                door: { x: 920, y: 212, w: 28, h: 38 },
                platforms: [
                    { x: 0, y: 250, w: 1000, h: 60, color: '#3742fa' }
                ],
                spikes: [
                    { x: 220, y: 234, w: 24, h: 16 },
                    { x: 340, y: 234, w: 24, h: 16, hidden: true, trigDist: 90, targetY: 234, currentY: 264 },
                    { x: 480, y: 234, w: 24, h: 16, hidden: true, trigDist: 90, targetY: 234, currentY: 264 },
                    { x: 620, y: 234, w: 24, h: 16, hidden: true, trigDist: 95, targetY: 234, currentY: 264 },
                    { x: 760, y: 234, w: 24, h: 16, hidden: true, trigDist: 95, targetY: 234, currentY: 264 }
                ]
            },
            // Level 3: Shy Door
            {
                name: "Level 3: Shy Door",
                worldWidth: 1100,
                start: { x: 50, y: 220 },
                door: { x: 520, y: 212, w: 28, h: 38, targetX: 900, moved: false, trigDist: 100 },
                platforms: [
                    { x: 0, y: 250, w: 1000, h: 60, color: '#3742fa' },
                    { x: 400, y: 190, w: 80, h: 60, color: '#5f27cd' }
                ],
                spikes: [
                    { x: 400, y: 174, w: 80, h: 16 }
                ]
            },
            // Level 4: Mind Flip
            {
                name: "Level 4: Mind Flip",
                worldWidth: 1100,
                start: { x: 50, y: 220 },
                door: { x: 720, y: 212, w: 28, h: 38 },
                platforms: [
                    { x: 0, y: 250, w: 220, h: 60, color: '#009688' },
                    { x: 280, y: 220, w: 140, h: 90, color: '#ff5722', trap: 'invert' },
                    { x: 490, y: 250, w: 300, h: 60, color: '#009688' }
                ],
                spikes: [
                    { x: 220, y: 296, w: 270, h: 16 }
                ],
                triggers: [
                    { x: 280, y: 180, w: 140, h: 70, type: 'INVERT', text: '⚠️ CONTROLS FLIPPED!' }
                ]
            },
            // Level 5: Heavy Sky
            {
                name: "Level 5: Heavy Sky",
                worldWidth: 1100,
                start: { x: 50, y: 220 },
                door: { x: 820, y: 212, w: 28, h: 38 },
                platforms: [
                    { x: 0, y: 250, w: 900, h: 60, color: '#3742fa' },
                    { x: 220, y: 60, w: 80, h: 50, color: '#e91e63', trap: 'falling', trigDist: 100 },
                    { x: 450, y: 60, w: 80, h: 50, color: '#e91e63', trap: 'falling', trigDist: 100 }
                ],
                spikes: [
                    { x: 620, y: 234, w: 32, h: 16 }
                ]
            },
            // Level 6: Crumbling Steps & Checkpoint
            {
                name: "Level 6: Crumbling Steps",
                worldWidth: 1200,
                start: { x: 50, y: 220 },
                door: { x: 740, y: 72, w: 28, h: 38 },
                checkpoints: [
                    { x: 410, y: 114, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 120, h: 60, color: '#3742fa' },
                    { x: 160, y: 210, w: 75, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 270, y: 170, w: 75, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 380, y: 150, w: 100, h: 150, color: '#2ed573' }, // Checkpoint island
                    { x: 510, y: 130, w: 75, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 620, y: 110, w: 200, h: 200, color: '#3742fa' }
                ],
                spikes: [
                    { x: 120, y: 296, w: 500, h: 16 }
                ]
            },
            // Level 7: Size Shifter
            {
                name: "Level 7: Size Shifter",
                worldWidth: 1300,
                start: { x: 50, y: 220 },
                door: { x: 1020, y: 212, w: 28, h: 38 },
                checkpoints: [
                    { x: 440, y: 214, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 200, h: 60, color: '#3742fa' },
                    { x: 320, y: 130, w: 30, h: 120, color: '#ff4757', breakable: true }, // Fragile wall
                    { x: 200, y: 250, w: 300, h: 60, color: '#3742fa' },
                    { x: 520, y: 180, w: 250, h: 30, color: '#5f27cd' }, // Low ceiling
                    { x: 500, y: 250, w: 350, h: 60, color: '#3742fa' },
                    { x: 880, y: 250, w: 250, h: 60, color: '#3742fa' }
                ],
                triggers: [
                    { x: 120, y: 180, w: 60, h: 70, type: 'GIANT', text: '🔥 GIANT MODE!' },
                    { x: 420, y: 180, w: 60, h: 70, type: 'TINY', text: '🔬 TINY MODE!' },
                    { x: 850, y: 180, w: 50, h: 70, type: 'NORMAL_SIZE', text: '⚡ NORMAL SIZE' }
                ],
                spikes: [
                    { x: 550, y: 234, w: 24, h: 16, hidden: true, trigDist: 90, targetY: 234, currentY: 264 }
                ]
            },
            // Level 8: Anti-Gravity
            {
                name: "Level 8: Anti-Gravity",
                worldWidth: 1300,
                start: { x: 50, y: 220 },
                door: { x: 1020, y: 212, w: 28, h: 38 },
                checkpoints: [
                    { x: 500, y: 60, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 200, h: 60, color: '#009688' },
                    { x: 220, y: 30, w: 600, h: 30, color: '#9c27b0' }, // Ceiling runway
                    { x: 850, y: 250, w: 300, h: 60, color: '#009688' }
                ],
                spikes: [
                    { x: 200, y: 296, w: 650, h: 16 }
                ],
                triggers: [
                    { x: 170, y: 180, w: 60, h: 70, type: 'REVERSE_GRAV', text: '🌌 GRAVITY REVERSED!' },
                    { x: 800, y: 30, w: 50, h: 90, type: 'NORMAL_GRAV', text: '⬇️ GRAVITY NORMAL' }
                ]
            },
            // Level 9: Double Combo
            {
                name: "Level 9: Double Combo",
                worldWidth: 1400,
                start: { x: 50, y: 220 },
                door: { x: 900, y: 212, w: 28, h: 38, targetX: 1040, moved: false, trigDist: 90 },
                checkpoints: [
                    { x: 550, y: 65, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 180, h: 60, color: '#3742fa' },
                    { x: 220, y: 220, w: 100, h: 20, color: '#ff9800', moving: true, startX: 220, endX: 420, speed: 2.0, dir: 1, progress: 0 },
                    { x: 460, y: 40, w: 300, h: 25, color: '#9c27b0' },
                    { x: 800, y: 250, w: 300, h: 60, color: '#3742fa' }
                ],
                spikes: [
                    { x: 180, y: 296, w: 620, h: 16 }
                ],
                triggers: [
                    { x: 420, y: 150, w: 50, h: 100, type: 'REVERSE_GRAV', text: '🌌 REVERSE GRAVITY!' },
                    { x: 740, y: 40, w: 50, h: 80, type: 'NORMAL_GRAV', text: '⬇️ NORMAL GRAVITY' }
                ]
            },
            // Level 10: Devil's Gauntlet
            {
                name: "Level 10: Devil's Gauntlet",
                worldWidth: 1500,
                start: { x: 50, y: 220 },
                door: { x: 1050, y: 212, w: 28, h: 38, targetX: 1140, moved: false, trigDist: 85 },
                checkpoints: [
                    { x: 490, y: 144, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 140, h: 60, color: '#d32f2f' },
                    { x: 180, y: 60, w: 90, h: 50, color: '#e91e63', trap: 'falling', trigDist: 100 },
                    { x: 160, y: 250, w: 140, h: 60, color: '#d32f2f' },
                    { x: 330, y: 210, w: 80, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 450, y: 180, w: 120, h: 120, color: '#2ed573' },
                    { x: 610, y: 180, w: 90, h: 18, color: '#009688', moving: true, startX: 610, endX: 810, speed: 2.5, dir: 1, progress: 0 },
                    { x: 940, y: 250, w: 300, h: 60, color: '#d32f2f' }
                ],
                spikes: [
                    { x: 140, y: 296, w: 800, h: 16 },
                    { x: 450, y: 60, w: 120, h: 16, ceiling: true }
                ]
            },
            // Level 11: TROLL MODE (MASTERPIECE)
            {
                name: "Level 11: TROLL MODE 🔥",
                worldWidth: 2000,
                start: { x: 50, y: 220 },
                door: { x: 1880, y: 212, w: 28, h: 38, targetX: 1910, moved: false, trigDist: 70 },
                checkpoints: [
                    { x: 860, y: 214, w: 24, h: 36, active: false },
                    { x: 1350, y: 214, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 220, h: 60, color: '#d32f2f' },
                    { x: 280, y: 130, w: 30, h: 120, color: '#ff4757', breakable: true },
                    { x: 220, y: 250, w: 220, h: 60, color: '#d32f2f' },
                    { x: 460, y: 30, w: 350, h: 25, color: '#9c27b0' },
                    { x: 830, y: 250, w: 160, h: 60, color: '#2ed573' },
                    { x: 1010, y: 180, w: 260, h: 30, color: '#5f27cd' },
                    { x: 990, y: 250, w: 300, h: 60, color: '#d32f2f' },
                    { x: 1310, y: 250, w: 150, h: 60, color: '#2ed573' },
                    { x: 1490, y: 220, w: 100, h: 18, color: '#ff9800', moving: true, startX: 1490, endX: 1690, speed: 2.2, dir: 1, progress: 0 },
                    { x: 1810, y: 250, w: 180, h: 60, color: '#2ed573' }
                ],
                spikes: [
                    { x: 440, y: 296, w: 390, h: 16 },
                    { x: 1010, y: 210, w: 260, h: 16, ceiling: true },
                    { x: 1290, y: 296, w: 520, h: 16 }
                ],
                triggers: [
                    { x: 120, y: 180, w: 60, h: 70, type: 'GIANT', text: '🔥 GIANT MODE!' },
                    { x: 410, y: 150, w: 50, h: 100, type: 'REVERSE_GRAV', text: '🌌 REVERSE GRAVITY!' },
                    { x: 800, y: 30, w: 40, h: 70, type: 'NORMAL_GRAV', text: '⬇️ GRAVITY NORMAL' },
                    { x: 940, y: 180, w: 40, h: 70, type: 'TINY', text: '🔬 TINY MODE!' },
                    { x: 1280, y: 180, w: 40, h: 70, type: 'NORMAL_SIZE', text: '⚡ SIZE RESTORED' }
                ]
            },
            // Level 12: IMPOSSIBLE? 🔥💀
            {
                name: "Level 12: IMPOSSIBLE? 🔥💀",
                worldWidth: 2200,
                start: { x: 50, y: 220 },
                door: { x: 2000, y: 212, w: 28, h: 38, targetX: 2060, moved: false, trigDist: 75 },
                checkpoints: [
                    { x: 440, y: 184, w: 24, h: 36, active: false },
                    { x: 860, y: 214, w: 24, h: 36, active: false },
                    { x: 1460, y: 214, w: 24, h: 36, active: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 180, h: 60, color: '#d32f2f' },
                    { x: 210, y: 220, w: 60, h: 18, color: '#ff9800', trap: 'disappear', trigDist: 90, visible: true },
                    { x: 300, y: 200, w: 70, h: 18, color: '#ff5722', trap: 'invert' },
                    { x: 330, y: 40, w: 70, h: 50, color: '#e91e63', trap: 'falling', trigDist: 90 },
                    { x: 410, y: 220, w: 110, h: 80, color: '#2ed573' },
                    { x: 520, y: 250, w: 180, h: 60, color: '#d32f2f' },
                    { x: 560, y: 110, w: 30, h: 140, color: '#ff4757', breakable: true },
                    { x: 730, y: 220, w: 65, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 820, y: 250, w: 120, h: 60, color: '#2ed573' },
                    { x: 930, y: 30, w: 450, h: 25, color: '#9c27b0' },
                    { x: 1420, y: 250, w: 120, h: 60, color: '#2ed573' },
                    { x: 1570, y: 220, w: 90, h: 18, color: '#009688', moving: true, startX: 1570, endX: 1790, speed: 2.8, dir: 1, progress: 0 },
                    { x: 1850, y: 250, w: 250, h: 60, color: '#2ed573' }
                ],
                spikes: [
                    { x: 180, y: 296, w: 230, h: 16 },
                    { x: 520, y: 296, w: 300, h: 16 },
                    { x: 920, y: 296, w: 500, h: 16 },
                    { x: 1100, y: 55, w: 80, h: 16, ceiling: true },
                    { x: 1540, y: 296, w: 310, h: 16 },
                    { x: 1890, y: 234, w: 24, h: 16, hidden: true, trigDist: 85, targetY: 234, currentY: 264 }
                ],
                triggers: [
                    { x: 420, y: 150, w: 60, h: 70, type: 'GIANT', text: '🔥 GIANT SMASH!' },
                    { x: 840, y: 180, w: 40, h: 70, type: 'TINY', text: '🔬 TINY INFILTRATION!' },
                    { x: 880, y: 180, w: 40, h: 70, type: 'REVERSE_GRAV', text: '🌌 REVERSE GRAVITY TUNNEL!' },
                    { x: 1360, y: 30, w: 40, h: 70, type: 'NORMAL_GRAV', text: '⬇️ GRAVITY NORMAL' },
                    { x: 1400, y: 180, w: 40, h: 70, type: 'NORMAL_SIZE', text: '⚡ SIZE RESTORED' }
                ]
            },
            // Level 13: The Golden Key 🔑
            {
                name: "Level 13: The Golden Key 🔑",
                worldWidth: 1400,
                start: { x: 50, y: 220 },
                door: { x: 1120, y: 212, w: 28, h: 38, locked: true },
                checkpoints: [{ x: 400, y: 184, w: 24, h: 36, active: false }],
                keys: [{ x: 550, y: 175, w: 20, h: 20, moving: true, targetX: 830, trigDist: 90, collected: false, moved: false }],
                platforms: [
                    { x: 0, y: 250, w: 180, h: 60, color: '#3742fa' },
                    { x: 230, y: 220, w: 80, h: 18, color: '#ff9800', trap: 'touch_disappear' },
                    { x: 360, y: 220, w: 100, h: 60, color: '#2ed573' },
                    { x: 520, y: 200, w: 90, h: 18, color: '#3742fa' },
                    { x: 660, y: 220, w: 90, h: 18, color: '#ff5722', trap: 'invert' },
                    { x: 800, y: 200, w: 100, h: 18, color: '#3742fa' },
                    { x: 950, y: 250, w: 250, h: 60, color: '#2ed573' }
                ],
                spikes: [
                    { x: 180, y: 296, w: 770, h: 16 },
                    { x: 680, y: 204, w: 24, h: 16, hidden: true, trigDist: 80, targetY: 204, currentY: 234 }
                ],
                triggers: []
            },
            // Level 14: Sinking Sands ⏳
            {
                name: "Level 14: Sinking Sands ⏳",
                worldWidth: 1500,
                start: { x: 50, y: 220 },
                door: { x: 1200, y: 212, w: 28, h: 38 },
                checkpoints: [{ x: 580, y: 184, w: 24, h: 36, active: false }],
                platforms: [
                    { x: 0, y: 250, w: 160, h: 60, color: '#3742fa' },
                    { x: 210, y: 240, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 320, y: 220, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 430, y: 200, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 540, y: 220, w: 110, h: 60, color: '#2ed573' },
                    { x: 690, y: 220, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 800, y: 200, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 910, y: 180, w: 70, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 1050, y: 250, w: 250, h: 60, color: '#2ed573' }
                ],
                spikes: [{ x: 160, y: 296, w: 890, h: 16 }],
                triggers: []
            },
            // Level 15: Teleporting Portal 🚪
            {
                name: "Level 15: Teleporting Portal 🚪",
                worldWidth: 1600,
                start: { x: 50, y: 220 },
                door: { x: 600, y: 182, w: 28, h: 38, targetX: 1200, moved: false, trigDist: 80 },
                checkpoints: [{ x: 410, y: 184, w: 24, h: 36, active: false }],
                platforms: [
                    { x: 0, y: 250, w: 200, h: 60, color: '#3742fa' },
                    { x: 240, y: 220, w: 80, h: 18, color: '#ff9800', trap: 'disappear', trigDist: 100, visible: true },
                    { x: 370, y: 220, w: 110, h: 60, color: '#2ed573' },
                    { x: 520, y: 220, w: 90, h: 18, color: '#009688', moving: true, startX: 520, endX: 720, speed: 2, dir: 1, progress: 0 },
                    { x: 850, y: 220, w: 100, h: 18, color: '#ff5722', trap: 'invert' },
                    { x: 1000, y: 250, w: 300, h: 60, color: '#2ed573' }
                ],
                spikes: [
                    { x: 200, y: 296, w: 800, h: 16 },
                    { x: 1050, y: 234, w: 24, h: 16, hidden: true, trigDist: 85, targetY: 234, currentY: 264 }
                ],
                triggers: []
            },
            // Level 16: Devil's Gauntlet 😈
            {
                name: "Level 16: Devil's Gauntlet 😈",
                worldWidth: 2400,
                start: { x: 50, y: 220 },
                door: { x: 2050, y: 212, w: 28, h: 38, targetX: 2120, moved: false, trigDist: 75, locked: true },
                checkpoints: [
                    { x: 460, y: 184, w: 24, h: 36, active: false },
                    { x: 810, y: 184, w: 24, h: 36, active: false },
                    { x: 1380, y: 184, w: 24, h: 36, active: false }
                ],
                keys: [
                    { x: 335, y: 160, w: 20, h: 20, moving: true, targetX: 460, trigDist: 85, collected: false, moved: false },
                    { x: 1100, y: 65, w: 20, h: 20, collected: false }
                ],
                platforms: [
                    { x: 0, y: 250, w: 180, h: 60, color: '#3742fa' },
                    { x: 220, y: 220, w: 65, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 320, y: 200, w: 65, h: 18, color: '#9c27b0', trap: 'sinking' },
                    { x: 430, y: 220, w: 110, h: 60, color: '#2ed573' },
                    { x: 580, y: 250, w: 180, h: 60, color: '#d32f2f' },
                    { x: 620, y: 110, w: 30, h: 140, color: '#ff4757', breakable: true },
                    { x: 780, y: 250, w: 110, h: 60, color: '#2ed573' },
                    { x: 920, y: 30, w: 400, h: 25, color: '#9c27b0' },
                    { x: 1350, y: 250, w: 110, h: 60, color: '#2ed573' },
                    { x: 1500, y: 220, w: 80, h: 18, color: '#009688', moving: true, startX: 1500, endX: 1750, speed: 3, dir: 1, progress: 0 },
                    { x: 1880, y: 250, w: 300, h: 60, color: '#2ed573' }
                ],
                spikes: [
                    { x: 180, y: 296, w: 250, h: 16 },
                    { x: 540, y: 296, w: 240, h: 16 },
                    { x: 890, y: 296, w: 460, h: 16 },
                    { x: 1050, y: 55, w: 60, h: 16, ceiling: true },
                    { x: 1460, y: 296, w: 420, h: 16 },
                    { x: 1950, y: 234, w: 24, h: 16, hidden: true, trigDist: 85, targetY: 234, currentY: 264 }
                ],
                triggers: [
                    { x: 440, y: 150, w: 40, h: 70, type: 'GIANT', text: '🔥 GIANT MODE!' },
                    { x: 800, y: 180, w: 40, h: 70, type: 'TINY', text: '🔬 TINY MODE!' },
                    { x: 840, y: 180, w: 40, h: 70, type: 'REVERSE_GRAV', text: '🌌 REVERSE GRAVITY!' },
                    { x: 1300, y: 30, w: 40, h: 70, type: 'NORMAL_GRAV', text: '⬇️ GRAVITY NORMAL' },
                    { x: 1340, y: 180, w: 40, h: 70, type: 'NORMAL_SIZE', text: '⚡ SIZE RESTORED' }
                ]
            }
        ];

        let activeLevelData = null;

        // Procedural Generator for Level 13+ (100% Beatable!)
        function generateProceduralLevel(index) {
            const levelNum = index + 1;
            const seed = levelNum * 9301 + 49297;
            function pseudoRandom() {
                const x = Math.sin(seed + Math.random()) * 10000;
                return x - Math.floor(x);
            }

            const worldWidth = 1600 + Math.floor(pseudoRandom() * 4) * 200;
            const startPlatform = { x: 0, y: 250, w: 160, h: 60, color: '#3742fa' };
            const endPlatform = { x: worldWidth - 200, y: 230, w: 200, h: 80, color: '#3742fa' };

            const platforms = [startPlatform];
            const spikes = [{ x: 160, y: 296, w: worldWidth - 360, h: 16 }];
            const triggers = [];

            const platformCount = 7;
            let currentX = 160;
            let currentY = 240;

            for (let i = 1; i <= platformCount; i++) {
                const gap = 60 + Math.floor(pseudoRandom() * 70); // Max 130 gap (player max jump = 220+)
                const pWidth = 80 + Math.floor(pseudoRandom() * 50);
                const nextY = Math.max(140, Math.min(260, currentY + Math.floor((pseudoRandom() - 0.5) * 80)));

                const isMoving = (i % 3 === 0);
                const platformX = currentX + gap;

                if (isMoving) {
                    platforms.push({
                        x: platformX, y: nextY, w: pWidth, h: 18, color: '#009688',
                        moving: true, startX: platformX, endX: platformX + 90, speed: 1.8, dir: 1, progress: 0
                    });
                } else {
                    const trapRoll = Math.floor(pseudoRandom() * 4);
                    let trapType = null;
                    let pColor = '#3742fa';
                    if (trapRoll === 0) { trapType = 'disappear'; pColor = '#e91e63'; }
                    else if (trapRoll === 1) { trapType = 'touch_disappear'; pColor = '#ff9800'; }

                    platforms.push({
                        x: platformX, y: nextY, w: pWidth, h: 18, color: pColor,
                        trap: trapType, trigDist: 95, visible: true
                    });
                }

                currentX = platformX + pWidth;
                currentY = nextY;
            }

            platforms.push(endPlatform);

            const midPlatform = platforms[Math.floor(platforms.length / 2)];
            const checkpoints = [
                { x: midPlatform.x + midPlatform.w / 2 - 12, y: midPlatform.y - 36, w: 24, h: 36, active: false }
            ];

            return {
                name: "Level " + levelNum + ": Procedural Challenge",
                worldWidth: worldWidth,
                start: { x: 50, y: 220 },
                door: { x: worldWidth - 120, y: 192, w: 28, h: 38 },
                checkpoints: checkpoints,
                platforms: platforms,
                spikes: spikes,
                triggers: triggers
            };
        }

        function loadLevel(index) {
            currentLevel = index;
            controlsInverted = false;
            reverseGravity = false;
            cameraX = 0;
            activeCheckpoint = null;

            if (currentLevel < baseLevels.length) {
                activeLevelData = JSON.parse(JSON.stringify(baseLevels[currentLevel]));
            } else {
                activeLevelData = generateProceduralLevel(currentLevel);
            }

            document.getElementById('hud-level-name').innerText = activeLevelData.name;

            player.x = activeLevelData.start.x;
            player.y = activeLevelData.start.y;
            player.w = 24;
            player.h = 28;
            player.targetW = 24;
            player.targetH = 28;
            player.vx = 0;
            player.vy = 0;
            player.grounded = false;

            activeLevelData.platforms.forEach(p => {
                p.visible = true;
                p.touched = false;
                p.triggered = false;
                if (p.moving) {
                    p.progress = 0;
                    p.dir = 1;
                }
            });

            if (activeLevelData.door) activeLevelData.door.moved = false;

            activeLevelData.spikes.forEach(s => {
                if (s.hidden) s.currentY = s.targetY + 30;
            });

            gameState = 'PLAYING';
        }

        function respawnOrRestart() {
            if (activeCheckpoint) {
                player.x = activeCheckpoint.x;
                player.y = activeCheckpoint.y - player.h;
                player.vx = 0;
                player.vy = 0;
                player.grounded = false;

                activeLevelData.platforms.forEach(p => {
                    p.visible = true;
                });

                gameState = 'PLAYING';
            } else {
                loadLevel(currentLevel);
            }
        }

        window.addEventListener('keydown', (e) => {
            if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') keys.left = true;
            if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') keys.right = true;
            if (e.key === 'ArrowUp' || e.key === 'w' || e.key === 'W' || e.key === ' ') {
                keys.up = true;
                if (gameState === 'DEAD' || gameState === 'WIN') {
                    if (gameState === 'WIN') loadLevel(currentLevel + 1);
                    else respawnOrRestart();
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
                    else respawnOrRestart();
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
                else respawnOrRestart();
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

            // Interpolate player size
            player.w += (player.targetW - player.w) * 0.15;
            player.h += (player.targetH - player.h) * 0.15;

            let curGrav = reverseGravity ? -player.gravity : player.gravity;
            let curJump = reverseGravity ? -player.jumpPower : player.jumpPower;

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
                player.vy = curJump;
                player.grounded = false;
            }

            player.vy += curGrav;

            // Moving Platforms Update
            lvl.platforms.forEach(p => {
                if (p.moving && p.visible) {
                    let dx = p.endX - p.startX;
                    let dist = Math.abs(dx);
                    if (dist > 0) {
                        p.progress += (p.speed / dist) * p.dir;
                        if (p.progress >= 1) { p.progress = 1; p.dir = -1; }
                        else if (p.progress <= 0) { p.progress = 0; p.dir = 1; }

                        let oldX = p.x;
                        p.x = p.startX + dx * p.progress;

                        if (player.grounded && player.x < p.x + p.w && player.x + player.w > p.x &&
                            player.y + player.h >= p.y - 2 && player.y <= p.y + p.h) {
                            player.x += (p.x - oldX);
                        }
                    }
                }
            });

            // Trigger Zones
            if (lvl.triggers) {
                lvl.triggers.forEach(t => {
                    if (player.x < t.x + t.w && player.x + player.w > t.x &&
                        player.y < t.y + t.h && player.y + player.h > t.y) {
                        if (t.type === 'GIANT') { player.targetW = 44; player.targetH = 52; showBanner(t.text, '#ff4757', '#fff'); }
                        else if (t.type === 'TINY') { player.targetW = 12; player.targetH = 14; showBanner(t.text, '#00d2d3', '#0f0f1b'); }
                        else if (t.type === 'NORMAL_SIZE') { player.targetW = 24; player.targetH = 28; showBanner(t.text, '#2ed573', '#0f0f1b'); }
                        else if (t.type === 'REVERSE_GRAV') { reverseGravity = true; showBanner(t.text, '#9c27b0', '#fff'); }
                        else if (t.type === 'NORMAL_GRAV') { reverseGravity = false; showBanner(t.text, '#2ed573', '#0f0f1b'); }
                        else if (t.type === 'INVERT') { controlsInverted = true; showBanner(t.text, '#ff4757', '#fff'); }
                    }
                });
            }

            // Checkpoints
            if (lvl.checkpoints) {
                lvl.checkpoints.forEach(chk => {
                    if (!chk.active && player.x < chk.x + chk.w && player.x + player.w > chk.x &&
                        player.y < chk.y + chk.h && player.y + player.h > chk.y) {
                        chk.active = true;
                        activeCheckpoint = { x: chk.x, y: chk.y };
                        showBanner("🚩 CHECKPOINT!", '#2ed573', '#0f0f1b');
                    }
                });
            }

            // Horizontal Movement & Collisions
            player.x += player.vx;
            if (player.x < 0) player.x = 0;

            lvl.platforms.forEach(p => {
                if (!p.visible) return;

                if (p.trap === 'disappear') {
                    let dist = Math.abs((player.x + player.w / 2) - (p.x + p.w / 2));
                    if (dist < (p.trigDist || 110)) p.visible = false;
                }

                if (p.visible && player.x < p.x + p.w && player.x + player.w > p.x &&
                    player.y < p.y + p.h && player.y + player.h > p.y) {

                    if (p.breakable && player.targetW > 30) {
                        p.visible = false;
                        showBanner("💥 WALL SHATTERED!", '#ff4757', '#fff');
                    } else {
                        if (player.vx > 0) player.x = p.x - player.w;
                        else if (player.vx < 0) player.x = p.x + p.w;
                    }
                }
            });

            // Vertical Movement & Collisions
            player.y += player.vy;
            player.grounded = false;

            lvl.platforms.forEach(p => {
                if (!p.visible) return;

                if (p.trap === 'falling') {
                    let dist = Math.abs((player.x + player.w / 2) - (p.x + p.w / 2));
                    if (dist < (p.trigDist || 100) && !p.triggered) {
                        p.triggered = true;
                        p.vy = 10;
                    }
                    if (p.triggered) p.y += p.vy;
                }

                if (p.visible && player.x < p.x + p.w && player.x + player.w > p.x &&
                    player.y < p.y + p.h && player.y + player.h > p.y) {

                    if (!reverseGravity) {
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
                    } else {
                        if (player.vy < 0) {
                            player.y = p.y + p.h;
                            player.vy = 0;
                            player.grounded = true;
                        } else if (player.vy > 0) {
                            player.y = p.y - player.h;
                            player.vy = 0;
                        }
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
            if (door && door.trigDist) {
                let distToDoor = Math.hypot((player.x - door.x), (player.y - door.y));
                if (distToDoor < door.trigDist && !door.moved) {
                    door.x = door.targetX;
                    door.moved = true;
                }
            }

            if (door && player.x < door.x + door.w && player.x + player.w > door.x &&
                player.y < door.y + door.h && player.y + player.h > door.y) {
                gameState = 'WIN';
            }

            if (player.y > 360 || player.y < -100) {
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

            // Checkpoints
            if (lvl.checkpoints) {
                lvl.checkpoints.forEach(chk => {
                    ctx.fillStyle = '#a4b0be';
                    ctx.fillRect(chk.x + chk.w * 0.45, chk.y, 3, chk.h);

                    ctx.fillStyle = chk.active ? '#2ed573' : '#ff4757';
                    ctx.beginPath();
                    ctx.moveTo(chk.x + chk.w * 0.48, chk.y);
                    ctx.lineTo(chk.x + chk.w, chk.y + chk.h * 0.25);
                    ctx.lineTo(chk.x + chk.w * 0.48, chk.y + chk.h * 0.5);
                    ctx.closePath();
                    ctx.fill();
                });
            }

            // Platforms
            lvl.platforms.forEach(p => {
                if (!p.visible) return;
                ctx.fillStyle = p.color;
                ctx.fillRect(p.x, p.y, p.w, p.h);
                ctx.fillStyle = 'rgba(255,255,255,0.35)';
                ctx.fillRect(p.x, p.y, p.w, 2.5);

                if (p.breakable) {
                    ctx.strokeStyle = 'rgba(255,255,255,0.6)';
                    ctx.lineWidth = 2;
                    ctx.beginPath();
                    ctx.moveTo(p.x + p.w * 0.2, p.y + p.h * 0.1);
                    ctx.lineTo(p.x + p.w * 0.8, p.y + p.h * 0.9);
                    ctx.stroke();
                }
            });

            // Spikes
            lvl.spikes.forEach(s => {
                let sy = s.hidden ? s.currentY : s.y;
                ctx.fillStyle = '#ff4757';
                let count = Math.max(1, Math.floor(s.w / 16));
                let step = s.w / count;
                for (let i = 0; i < count; i++) {
                    ctx.beginPath();
                    if (s.ceiling) {
                        ctx.moveTo(s.x + i * step, sy);
                        ctx.lineTo(s.x + (i + 0.5) * step, sy + s.h);
                        ctx.lineTo(s.x + (i + 1) * step, sy);
                    } else {
                        ctx.moveTo(s.x + i * step, sy + s.h);
                        ctx.lineTo(s.x + (i + 0.5) * step, sy);
                        ctx.lineTo(s.x + (i + 1) * step, sy + s.h);
                    }
                    ctx.closePath();
                    ctx.fill();
                }
            });

            // Door
            let d = lvl.door;
            if (d) {
                ctx.fillStyle = '#2ed573';
                ctx.fillRect(d.x, d.y, d.w, d.h);
                ctx.fillStyle = '#0f0f1b';
                ctx.fillRect(d.x + 5, d.y + 8, d.w - 10, d.h - 8);
                ctx.fillStyle = '#ffa502';
                ctx.beginPath();
                ctx.arc(d.x + d.w - 7, d.y + d.h / 2, 2.5, 0, Math.PI * 2);
                ctx.fill();
            }

            // Player
            if (gameState !== 'DEAD') {
                let px = player.x;
                let py = player.y;
                let pw = player.w;
                let ph = player.h;

                let isMoving = Math.abs(player.vx) > 0.2;
                let legSwing = isMoving ? Math.sin(player.x * 0.25) * 4 : 0;

                ctx.fillStyle = '#d63031';
                ctx.fillRect(px + pw * 0.2 + legSwing, py + ph - 2, Math.max(2, pw * 0.14), Math.max(3, ph * 0.25));
                ctx.fillRect(px + pw * 0.6 - legSwing, py + ph - 2, Math.max(2, pw * 0.14), Math.max(3, ph * 0.25));

                let armY = player.grounded ? py + ph * 0.35 : py + ph * 0.1;
                ctx.fillRect(px - 3, armY, Math.max(2, pw * 0.14), Math.max(3, ph * 0.25));
                ctx.fillRect(px + pw - 1, armY, Math.max(2, pw * 0.14), Math.max(3, ph * 0.25));

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

                let eyeR = Math.max(2, 3 * (pw / 24));
                ctx.fillStyle = '#ffffff';
                ctx.beginPath();
                ctx.arc(eye1X + eyeR, eyeY + eyeR, eyeR, 0, Math.PI * 2);
                ctx.arc(eye2X + eyeR, eyeY + eyeR, eyeR, 0, Math.PI * 2);
                ctx.fill();

                let pupilOffset = player.facingRight ? 1.2 : -1.2;
                ctx.fillStyle = '#0f0f1b';
                ctx.beginPath();
                ctx.arc(eye1X + eyeR + pupilOffset, eyeY + eyeR, eyeR * 0.6, 0, Math.PI * 2);
                ctx.arc(eye2X + eyeR + pupilOffset, eyeY + eyeR, eyeR * 0.6, 0, Math.PI * 2);
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

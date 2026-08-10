const canvas=document.getElementById('game'),ctx=canvas.getContext('2d');
const livesEl=document.getElementById('lives'),scoreEl=document.getElementById('score'),levelEl=document.getElementById('level');
const menu=document.getElementById('menu'),message=document.getElementById('message'),startBtn=document.getElementById('startBtn');
let W=1100,H=650,dpr=1,running=false,last=0,score=0,lives=3,level=1,scroll=0,shake=0;
let player,objects=[],particles=[],keys={left:false,right:false},levelGoal=1100,levelDistance=0;

function resize(){dpr=Math.min(devicePixelRatio||1,2);const r=canvas.getBoundingClientRect();W=r.width;H=r.height;canvas.width=W*dpr;canvas.height=H*dpr;ctx.setTransform(dpr,0,0,dpr,0,0)}
addEventListener('resize',resize); resize();

function resetLevel(){player={x:W*.18,y:H*.62,w:42,h:58,vy:0,onGround:false,speed:300,inv:0};objects=[];particles=[];levelDistance=0;
 const gap=Math.max(260,420-level*25), count=7+level*2;
 for(let i=0;i<count;i++){let x=500+i*gap+Math.random()*150;let type=i%4===0?'spike':(i%4===1?'pit':'block');objects.push({x,y: type==='pit'?H*.74:H*.68,w:type==='spike'?55:75,h:type==='spike'?45:70,type,hit:false})}
 for(let i=0;i<4+level;i++) objects.push({x:650+i*gap*.72,y:H*.46,w:28,h:28,type:'coin',hit:false});
 levelGoal=objects.reduce((m,o)=>Math.max(m,o.x+200),1200+level*300);updateHud()}
function startGame(){score=0;lives=3;level=1;resetLevel();running=true;menu.classList.add('hidden');message.classList.add('hidden');last=performance.now();requestAnimationFrame(loop)}
startBtn.onclick=startGame;

function updateHud(){livesEl.textContent=lives;scoreEl.textContent=Math.floor(score);levelEl.textContent=level}
function jump(){if(player?.onGround){player.vy=-650;player.onGround=false}}
function hurt(){if(player.inv>0)return;lives--;player.inv=1.5;shake=.35;burst(player.x+20,player.y+25);updateHud();if(lives<=0)endGame(false)}
function endGame(win){running=false;message.innerHTML=`<h2>${win?'🏆 LEVEL COMPLETE!':'💥 GAME OVER'}</h2><p>${win?'You survived this level.':'Billa needs another try!'}</p><div class="msgBtns"><button id="again">RESTART</button>${win?'<button id="next">NEXT LEVEL</button>':''}</div>`;message.classList.remove('hidden');
 document.getElementById('again').onclick=()=>{message.classList.add('hidden');startGame()}; if(win)document.getElementById('next').onclick=()=>{level++;lives=3;resetLevel();message.classList.add('hidden');running=true;last=performance.now();requestAnimationFrame(loop)}}
function burst(x,y){for(let i=0;i<18;i++)particles.push({x,y,vx:(Math.random()-.5)*300,vy:(Math.random()-.8)*300,t:.5})}

function rect(a,b){return a.x<b.x+b.w&&a.x+a.w>b.x&&a.y<b.y+b.h&&a.y+a.h>b.y}
function update(dt){
 const accel=level*15+900, gravity=1700;
 if(keys.left)player.x-=accel*dt; if(keys.right)player.x+=accel*dt;
 player.x=Math.max(15,Math.min(W-player.w-15,player.x));
 player.vy+=gravity*dt; player.y+=player.vy*dt;
 const ground=H*.74; if(player.y+player.h>=ground){player.y=ground-player.h;player.vy=0;player.onGround=true}
 player.inv=Math.max(0,player.inv-dt); scroll+=dt*(250+level*28); levelDistance+=dt*(250+level*28);
 for(const o of objects){o.x-=dt*(250+level*28);
   if(o.type==='coin'&&!o.hit&&rect(player,{x:o.x,y:o.y,w:o.w,h:o.h})){o.hit=true;score+=25;burst(o.x,o.y)}
   if(!o.hit&&(o.type==='spike'||o.type==='block')&&rect(player,o)){o.hit=true;hurt()}
 }
 objects=objects.filter(o=>o.x>-150 && !o.hit || o.type==='pit');
 if(Math.random()<dt*(.55+level*.12))objects.push({x:W+80,y:H*.68,w:55,h:45,type:'spike',hit:false});
 if(levelDistance>levelGoal)endGame(true);
 for(const p of particles){p.x+=p.vx*dt;p.y+=p.vy*dt;p.vy+=500*dt;p.t-=dt}particles=particles.filter(p=>p.t>0);
 updateHud()
}
function draw(){
 ctx.clearRect(0,0,W,H);
 const sky=ctx.createLinearGradient(0,0,0,H);sky.addColorStop(0,'#65c7ff');sky.addColorStop(1,'#e7f8ff');ctx.fillStyle=sky;ctx.fillRect(0,0,W,H);
 ctx.fillStyle='#b9e6a2';for(let i=0;i<9;i++){let x=((i*180-scroll*.12)%1260)-80;ctx.beginPath();ctx.arc(x,H*.48,100,Math.PI,0);ctx.fill()}
 ctx.fillStyle='#78b85b';ctx.fillRect(0,H*.74,W,H*.26);
 ctx.fillStyle='#4e8c43';for(let x=-20;x<W;x+=45){ctx.fillRect(x,H*.74,25,5)}
 for(const o of objects){if(o.x<-100||o.x>W+100)continue; if(o.type==='spike'){ctx.fillStyle='#444';ctx.beginPath();ctx.moveTo(o.x,o.y+o.h);ctx.lineTo(o.x+o.w/2,o.y);ctx.lineTo(o.x+o.w,o.y+o.h);ctx.closePath();ctx.fill();ctx.fillStyle='#e33';ctx.fillRect(o.x+8,o.y+o.h-9,o.w-16,6)}
 else if(o.type==='block'){ctx.fillStyle='#8b5a3c';ctx.fillRect(o.x,o.y,o.w,o.h);ctx.fillStyle='#c98a55';ctx.fillRect(o.x+8,o.y+8,o.w-16,8)}
 else {ctx.fillStyle='#ffd43b';ctx.beginPath();ctx.arc(o.x+14,o.y+14,14,0,7);ctx.fill();ctx.fillStyle='#fff3a3';ctx.beginPath();ctx.arc(o.x+10,o.y+10,4,0,7);ctx.fill()}}
 // Billa
 if(!(player.inv>0&&Math.floor(player.inv*12)%2===0)){ctx.save();ctx.translate(player.x,player.y);ctx.fillStyle='#555';ctx.beginPath();ctx.roundRect(4,16,34,38,12);ctx.fill();ctx.fillStyle='#777';ctx.beginPath();ctx.arc(21,17,22,0,7);ctx.fill();ctx.fillStyle='#555';ctx.beginPath();ctx.moveTo(4,4);ctx.lineTo(9,-8);ctx.lineTo(17,5);ctx.moveTo(25,5);ctx.lineTo(34,-8);ctx.lineTo(39,7);ctx.fill();ctx.fillStyle='#fff';ctx.beginPath();ctx.arc(13,16,5,0,7);ctx.arc(29,16,5,0,7);ctx.fill();ctx.fillStyle='#111';ctx.beginPath();ctx.arc(14,17,2,0,7);ctx.arc(28,17,2,0,7);ctx.fill();ctx.fillStyle='#f59';ctx.beginPath();ctx.arc(21,25,5,0,7);ctx.fill();ctx.restore()}
 for(const p of particles){ctx.globalAlpha=Math.max(0,p.t*2);ctx.fillStyle='#ffd43b';ctx.fillRect(p.x,p.y,5,5);ctx.globalAlpha=1}
}
function loop(t){if(!running)return;const dt=Math.min((t-last)/1000,.033);last=t;update(dt);draw();requestAnimationFrame(loop)}

function bind(btn,prop){btn.addEventListener('pointerdown',e=>{e.preventDefault();keys[prop]=true});['pointerup','pointercancel','pointerleave'].forEach(ev=>btn.addEventListener(ev,()=>keys[prop]=false))}
bind(document.getElementById('left'),'left');bind(document.getElementById('right'),'right');
document.getElementById('jump').addEventListener('pointerdown',e=>{e.preventDefault();jump()});
addEventListener('keydown',e=>{if(e.key==='ArrowLeft')keys.left=true;if(e.key==='ArrowRight')keys.right=true;if(e.code==='Space'||e.key==='ArrowUp'){e.preventDefault();jump()}});
addEventListener('keyup',e=>{if(e.key==='ArrowLeft')keys.left=false;if(e.key==='ArrowRight')keys.right=false});

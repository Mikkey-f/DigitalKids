<template>
  <div class="loading-container">
    <div class="loading-overlay"></div>
    <div class="loading-content">
      <div class="scene">
        <div class="cube">
          <div class="cube__face cube__face--front"></div>
          <div class="cube__face cube__face--back"></div>
          <div class="cube__face cube__face--right"></div>
          <div class="cube__face cube__face--left"></div>
          <div class="cube__face cube__face--top"></div>
          <div class="cube__face cube__face--bottom"></div>
        </div>
      </div>
      <div class="loading-text">
        <span class="dot" v-for="i in 3" :key="i"></span>
        <span class="message">{{ message }}</span>
      </div>
      <div class="particles-container">
        <div class="particle" v-for="i in 20" :key="i"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, onMounted } from 'vue';

const props = defineProps({
  message: {
    type: String,
    default: '正在为你准备数字儿童世界'
  }
});
</script>

<style scoped>
.loading-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  perspective: 800px;
}
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(26, 32, 44, 0.85);
  backdrop-filter: blur(8px);
}
.loading-content {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2rem;
  transform-style: preserve-3d;
  z-index: 2;
}
.scene {
  width: 100px;
  height: 100px;
  perspective: 500px;
  margin-bottom: 1rem;
}
.cube {
  width: 100%;
  height: 100%;
  position: relative;
  transform-style: preserve-3d;
  transform: translateZ(-50px);
  animation: cube-rotate 6s infinite ease-in-out;
}
.cube__face {
  position: absolute;
  width: 100px;
  height: 100px;
  border: 2px solid rgba(79, 209, 197, 0.8);
  background: rgba(59, 130, 246, 0.2);
  box-shadow: 0 0 20px rgba(79, 209, 197, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: #fff;
}
.cube__face--front  { transform: rotateY(0deg) translateZ(50px); }
.cube__face--right  { transform: rotateY(90deg) translateZ(50px); }
.cube__face--back   { transform: rotateY(180deg) translateZ(50px); }
.cube__face--left   { transform: rotateY(-90deg) translateZ(50px); }
.cube__face--top    { transform: rotateX(90deg) translateZ(50px); }
.cube__face--bottom { transform: rotateX(-90deg) translateZ(50px); }
@keyframes cube-rotate {
  0% {
    transform: translateZ(-50px) rotateX(0deg) rotateY(0deg) rotateZ(0deg);
  }
  33% {
    transform: translateZ(-50px) rotateX(180deg) rotateY(90deg) rotateZ(0deg);
  }
  66% {
    transform: translateZ(-50px) rotateX(360deg) rotateY(180deg) rotateZ(180deg);
  }
  100% {
    transform: translateZ(-50px) rotateX(0deg) rotateY(360deg) rotateZ(360deg);
  }
}
.loading-text {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.2rem;
  font-weight: 500;
  letter-spacing: 0.05em;
}
.dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  margin: 0 4px;
  border-radius: 50%;
  background-color: #4FD1C5;
  animation: dot-pulse 1.5s infinite ease-in-out;
}
.dot:nth-child(1) {
  animation-delay: 0s;
}
.dot:nth-child(2) {
  animation-delay: 0.2s;
}
.dot:nth-child(3) {
  animation-delay: 0.4s;
  margin-right: 12px;
}
@keyframes dot-pulse {
  0%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
}
.message {
  background: linear-gradient(90deg, #4FD1C5, #3B82F6, #9333EA);
  background-size: 200% auto;
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  animation: text-gradient 3s linear infinite;
}
@keyframes text-gradient {
  0% {
    background-position: 0% center;
  }
  100% {
    background-position: 200% center;
  }
}
.particles-container {
  position: absolute;
  width: 300px;
  height: 300px;
  z-index: -1;
}
.particle {
  position: absolute;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(79, 209, 197, 0.8) 0%, rgba(79, 209, 197, 0) 70%);
  box-shadow: 0 0 10px 2px rgba(79, 209, 197, 0.3);
  animation: float 5s infinite ease-in-out;
}
@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.2;
  }
  25% {
    transform: translateY(-30px) translateX(15px) scale(1.2);
    opacity: 0.8;
  }
  50% {
    transform: translateY(0) translateX(30px) scale(1);
    opacity: 0.4;
  }
  75% {
    transform: translateY(30px) translateX(15px) scale(0.8);
    opacity: 0.6;
  }
}
.particle:nth-child(1) { top: 10%; left: 20%; animation-delay: 0s; }
.particle:nth-child(2) { top: 40%; left: 80%; animation-delay: 0.3s; }
.particle:nth-child(3) { top: 60%; left: 10%; animation-delay: 0.6s; }
.particle:nth-child(4) { top: 80%; left: 30%; animation-delay: 0.9s; }
.particle:nth-child(5) { top: 20%; left: 70%; animation-delay: 1.2s; }
.particle:nth-child(6) { top: 30%; left: 40%; animation-delay: 1.5s; }
.particle:nth-child(7) { top: 90%; left: 90%; animation-delay: 1.8s; }
.particle:nth-child(8) { top: 70%; left: 60%; animation-delay: 2.1s; }
.particle:nth-child(9) { top: 50%; left: 50%; animation-delay: 2.4s; }
.particle:nth-child(10) { top: 55%; left: 15%; animation-delay: 2.7s; }
.particle:nth-child(11) { top: 25%; left: 85%; animation-delay: 3.0s; }
.particle:nth-child(12) { top: 45%; left: 35%; animation-delay: 3.3s; }
.particle:nth-child(13) { top: 15%; left: 45%; animation-delay: 3.6s; }
.particle:nth-child(14) { top: 85%; left: 75%; animation-delay: 3.9s; }
.particle:nth-child(15) { top: 65%; left: 25%; animation-delay: 4.2s; }
.particle:nth-child(16) { top: 5%; left: 55%; animation-delay: 4.5s; }
.particle:nth-child(17) { top: 35%; left: 65%; animation-delay: 4.8s; }
.particle:nth-child(18) { top: 75%; left: 5%; animation-delay: 5.1s; }
.particle:nth-child(19) { top: 95%; left: 45%; animation-delay: 5.4s; }
.particle:nth-child(20) { top: 15%; left: 30%; animation-delay: 5.7s; }
</style> 
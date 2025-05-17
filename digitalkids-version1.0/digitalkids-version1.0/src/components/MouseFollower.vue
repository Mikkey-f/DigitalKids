<template>
  <div class="mouse-follower-container">
    <div class="cursor-follower" ref="follower"></div>
    <div class="cursor-dot" ref="dot"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const follower = ref<HTMLElement | null>(null);
const dot = ref<HTMLElement | null>(null);
let mouseX = 0;
let mouseY = 0;
let posX = 0;
let posY = 0;
let cursorVisible = false;
let cursorScale = 1;

const onMouseMove = (e: MouseEvent) => {
  mouseX = e.clientX;
  mouseY = e.clientY;
  if (!cursorVisible) {
    showCursor();
  }
};

const onMouseLeave = () => {
  hideCursor();
};

const onMouseEnter = () => {
  if (follower.value && dot.value) {
    cursorScale = 1.5;
    follower.value.style.transform = `translate3d(${mouseX - follower.value.clientWidth / 2}px, ${mouseY - follower.value.clientHeight / 2}px, 0) scale(${cursorScale})`;
    dot.value.style.transform = `translate3d(${mouseX - dot.value.clientWidth / 2}px, ${mouseY - dot.value.clientHeight / 2}px, 0)`;
  }
};

const onMouseOut = () => {
  cursorScale = 1;
};

const showCursor = () => {
  if (follower.value && dot.value) {
    cursorVisible = true;
    follower.value.style.opacity = '1';
    dot.value.style.opacity = '1';
  }
};

const hideCursor = () => {
  if (follower.value && dot.value) {
    cursorVisible = false;
    follower.value.style.opacity = '0';
    dot.value.style.opacity = '0';
  }
};

const render = () => {
  if (follower.value && dot.value) {
    posX += (mouseX - posX) * 0.1;
    posY += (mouseY - posY) * 0.1;
    follower.value.style.transform = `translate3d(${posX - follower.value.clientWidth / 2}px, ${posY - follower.value.clientHeight / 2}px, 0) scale(${cursorScale})`;
    dot.value.style.transform = `translate3d(${mouseX - dot.value.clientWidth / 2}px, ${mouseY - dot.value.clientHeight / 2}px, 0)`;
  }
  requestAnimationFrame(render);
};

const addEventListeners = () => {
  const clickables = document.querySelectorAll('a, button, .clickable, [role="button"]');
  clickables.forEach(el => {
    el.addEventListener('mouseenter', onMouseEnter);
    el.addEventListener('mouseleave', onMouseOut);
  });
};

onMounted(() => {
  document.addEventListener('mousemove', onMouseMove);
  document.addEventListener('mouseleave', onMouseLeave);
  addEventListeners();
  requestAnimationFrame(render);
  document.body.style.cursor = 'none';
});

onUnmounted(() => {
  document.removeEventListener('mousemove', onMouseMove);
  document.removeEventListener('mouseleave', onMouseLeave);
  const clickables = document.querySelectorAll('a, button, .clickable, [role="button"]');
  clickables.forEach(el => {
    el.removeEventListener('mouseenter', onMouseEnter);
    el.removeEventListener('mouseleave', onMouseOut);
  });
  document.body.style.cursor = 'auto';
});
</script>

<style scoped>
.mouse-follower-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9999;
}

.cursor-follower {
  position: absolute;
  width: 36px;
  height: 36px;
  background: rgba(237, 251, 233, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  transform: translate3d(0, 0, 0);
  transition: transform 0.12s ease-out, opacity 0.3s ease-out, background-color 0.3s ease;
  opacity: 0;
}

.cursor-dot {
  position: absolute;
  width: 16px;
  height: 16px;
  background-color: blue;
  border-radius: 50%;
  transform: translate3d(0, 0, 0);
  opacity: 0;
}
</style> 
<template>
  <footer class="app_footer">
    <div class="footer-slant"></div>
    <div class="footer-waves">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2880 320" preserveAspectRatio="none" class="waves">
        <path fill="rgba(255, 255, 255, 0.1)" d="M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,112C672,96,768,96,864,112C960,128,1056,160,1152,160C1248,160,1344,128,1392,112L1440,96L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z M1440,96L1488,112C1536,128,1632,160,1728,160C1824,160,1920,128,2016,112C2112,96,2208,96,2304,112C2400,128,2496,160,2592,160C2688,160,2784,128,2832,112L2880,96L2880,320L2832,320C2784,320,2688,320,2592,320C2496,320,2400,320,2304,320C2208,320,2112,320,2016,320C1920,320,1824,320,1728,320C1632,320,1536,320,1488,320L1440,320Z"></path>
        <path fill="rgba(255, 255, 255, 0.05)" d="M0,192L48,176C96,160,192,128,288,128C384,128,480,160,576,176C672,192,768,192,864,176C960,160,1056,128,1152,128C1248,128,1344,160,1392,176L1440,192L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z M1440,192L1488,176C1536,160,1632,128,1728,128C1824,128,1920,160,2016,176C2112,192,2208,192,2304,176C2400,160,2496,128,2592,128C2688,128,2784,160,2832,176L2880,192L2880,320L2832,320C2784,320,2688,320,2592,320C2496,320,2400,320,2304,320C2208,320,2112,320,2016,320C1920,320,1824,320,1728,320C1632,320,1536,320,1488,320L1440,320Z"></path>
      </svg>
    </div>
    <div class="footer-content">
      <div class="container">
        <div class="footer-column">
          <h2>快速导航</h2>
          <nav class="links-container">
            <a href="javascript:;">关于数字儿童</a>
            <a href="javascript:;">常见问题</a>
            <a href="javascript:;">联系我们</a>
            <a href="javascript:;">法律声明</a>
            <a href="javascript:;">隐私政策</a>
          </nav>
        </div>

        <div class="footer-column">
          <h2>热门标签</h2>
          <div class="tags-container">
            <a href="javascript:;" class="tag">全部资源</a>
            <a href="javascript:;" class="tag">Three.js</a>
            <a href="javascript:;" class="tag">Vue3</a>
            <a href="javascript:;" class="tag">健康检测</a>
            <a href="javascript:;" class="tag">舌苔检测</a>
            <a href="javascript:;" class="tag">优选商城</a>
            <a href="javascript:;" class="tag">柔和配色</a>
            <a href="javascript:;" class="tag">视觉美观</a>
            <a href="javascript:;" class="tag">智能助手</a>
            <a href="javascript:;" class="tag">MCP</a>
          </div>
        </div>
        <div class="footer-column">
          <div class="decoration-container">
            <img src="@/assets/images/footer/ocean.png" alt="Ocean decoration" class="ocean-img" />
          </div>
        </div>
      </div>
    </div>
    <div class="copyright-bar">
      <div class="container">
        <p>用 <span class="heart">❤</span> 制作 - 来自数字儿童</p>
      </div>
    </div>
  </footer>
</template>

<style scoped lang='scss'>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');

.app_footer {
  overflow: hidden;
  width: 100%;
  background: linear-gradient(90deg, #2cb5aa 0%, #4a90e2 100%);
  color: white;
  position: relative;
  font-family: 'Inter', sans-serif;
  
  .footer-slant {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 80px;
    background-color: #fff;
    clip-path: polygon(0 0, 100% 0, 100% 100%, 0 0);
    z-index: 1;
  }
  
  .footer-waves {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 200px;
    overflow: hidden;
    z-index: 1;
    pointer-events: none;
    
    .waves {
      position: absolute;
      bottom: 0;
      width: 200%;
      height: 100%;
      transform: translateX(-25%);
      animation: wave 20s linear infinite;
    }
  }
  
  .footer-content {
    padding: 100px 0 60px;
    position: relative;
    z-index: 2;
    margin-top: 30px;
    
    .container {
      width: 90%;
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      gap: 40px;
    }
    
    .footer-column {
      flex: 1;
      min-width: 280px;
      
      @media (max-width: 768px) {
        width: 100%;
        margin-bottom: 40px;
      }
      
      h2 {
        font-size: 28px;
        font-weight: 600;
        margin-bottom: 35px;
        color: white;
        letter-spacing: -0.5px;
      }
      
      .links-container {
        display: flex;
        flex-direction: column;
        gap: 12px;
        
        a {
          color: rgba(255, 255, 255, 0.9);
          text-decoration: none;
          font-size: 15px;
          font-weight: 400;
          transition: color 0.2s;
          
          &:hover {
            color: white;
          }
        }
      }
      
      .tags-container {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        
        .tag {
          display: inline-block;
          padding: 6px 16px;
          background-color: rgba(255, 255, 255, 0.15);
          border-radius: 100px;
          color: white;
          text-decoration: none;
          font-size: 13px;
          font-weight: 500;
          transition: all 0.2s ease;
          letter-spacing: 0;
          
          &:hover {
            background-color: rgba(255, 255, 255, 0.25);
            transform: translateY(-1px);
          }
        }
      }
      
      .decoration-container {
        position: relative;
        width: 100%;
        height: 100%;
        min-height: 300px;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow: hidden;
        border-radius: 30px;
        
        &::before {
          content: '';
          position: absolute;
          inset: 0;
          background: radial-gradient(
            circle at center,
            rgba(255, 255, 255, 0) 0%,
            rgba(255, 255, 255, 0) 60%,
            rgba(44, 181, 170, 0.8) 100%
          );
          z-index: 2;
          pointer-events: none;
        }
        
        .ocean-img {
          width: 120%;
          height: 120%;
          object-fit: cover;
          opacity: 0.8;
          animation: float 8s ease-in-out infinite;
          transform-origin: center;
          mask-image: radial-gradient(
            circle at center,
            black 0%,
            black 60%,
            transparent 100%
          );
          -webkit-mask-image: radial-gradient(
            circle at center,
            black 0%,
            black 60%,
            transparent 100%
          );
        }
      }
    }
  }
  
  .copyright-bar {
    text-align: center;
    padding: 25px 0;
    position: relative;
    z-index: 2;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    
    .container {
      width: 90%;
      max-width: 1200px;
      margin: 0 auto;
    }
    
    p {
      margin: 0;
      font-weight: 400;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.9);
      letter-spacing: 0;
      
      .heart {
        display: inline-block;
        color: white;
        margin: 0 1px;
        animation: heartBeat 1.5s ease infinite;
      }
    }
  }
}

@keyframes heartBeat {
  0% {
    transform: scale(1);
  }
  14% {
    transform: scale(1.3);
  }
  28% {
    transform: scale(1);
  }
  42% {
    transform: scale(1.3);
  }
  70% {
    transform: scale(1);
  }
}

@keyframes wave {
  0% {
    transform: translateX(-25%);
  }
  50% {
    transform: translateX(-50%);
  }
  100% {
    transform: translateX(-25%);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-20px) scale(1.05);
  }
}
</style>

<script>
export default {
  name: 'AppFooter'
}
</script>
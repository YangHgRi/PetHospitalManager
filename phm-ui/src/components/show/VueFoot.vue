<template>
  <footer class="vue-foot">
    <div class="foot-wave">
      <svg class="waves" preserveAspectRatio="none" shape-rendering="auto"
           viewBox="0 24 150 28" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
        <defs>
          <path id="gentle-wave"
                d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"/>
        </defs>
        <g class="parallax">
          <!--浅粉红-->
          <use fill="rgba(255,182,193)" x="48" xlink:href="#gentle-wave" y="0"/>
          <!--天蓝色-->
          <use fill="rgba(135,206,235)" x="48" xlink:href="#gentle-wave" y="3"/>
          <!--苍白绿-->
          <use fill="rgba(152,251,152)" x="48" xlink:href="#gentle-wave" y="5"/>
          <!--幽灵白，与页脚背景相同-->
          <use fill="rgba(248,248,255)" x="48" xlink:href="#gentle-wave" y="7"/>
        </g>
      </svg>
    </div>
    <!--页脚-->
    <div class="footer">宠物医院管理系统</div>
  </footer>
</template>

<script lang="ts" setup>
</script>

<style lang="scss" scoped>
.foot-wave {
  .waves {
    position: relative;
    width: 100%;
    height: 15vh;
    margin-bottom: -7px;
    min-height: 100px;
    max-height: 150px;
    @media (max-width: 768px) {
      height: 40px;
      min-height: 40px;
    }

    .parallax {
      > use {
        /* 使use元素执行move-forever动画 */
        animation: move-forever 25s cubic-bezier(.55, .5, .45, .5) infinite;

        &:nth-child(1) {
          /* 延迟2秒启动动画  */
          animation-delay: -2s;
          /* 设置动画持续时间为7秒 */
          animation-duration: 7s;
        }

        &:nth-child(2) {
          animation-delay: -3s;
          animation-duration: 10s;
        }

        &:nth-child(3) {
          animation-delay: -4s;
          animation-duration: 13s;
        }

        &:nth-child(4) {
          animation-delay: -5s;
          animation-duration: 20s;
        }
      }
    }

    @keyframes move-forever {
      0% {
        transform: translate3d(-90px, 0, 0);
      }

      100% {
        transform: translate3d(85px, 0, 0);
      }
    }
  }
}

.vue-foot {
  width: 100%;
  text-align: center;
  margin-top: 2.5rem;
  /* 简介外包装 */
  .features {
    background-color: rgba(248, 248, 255);
    border-top: 1px solid #eaecef;
    transition: border-color .3s ease;;
    padding: 1.2rem 0;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    align-content: stretch;
    justify-content: space-between;
    @media (max-width: 719px) {
      flex-direction: column;
    }
    /* 单个简介 */
    .feature {
      flex-grow: 1;
      flex-basis: 30%;
      max-width: 30%;
      @media (max-width: 719px) {
        max-width: 100%;
        padding: 0 2.5rem
      }
      /* 简介标题 */
      h2 {
        font-size: 1.4rem;
        font-weight: 500;
        border-bottom: none;
        padding-bottom: 0;
        color: #3a5169
      }

      /* 简介内容 */
      p {
        color: #3a5169
      }
    }
  }
}

/* 页脚 */
.footer {
  background-color: rgba(248, 248, 255);
  padding: 2.5rem;
  border-top: 1px solid #eaecef;
  text-align: center;
  color: #3a5169;
  transition: border-color .3s ease;
}


</style>
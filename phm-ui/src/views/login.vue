<template>
  <div class="login-page">
    <!-- 定义背景图片 -->
    <svg preserveAspectRatio="none" viewBox="0 0 1000 200" xmlns="http://www.w3.org/2000/svg">
      <!-- 定义填充色-->
      <defs>
        <linearGradient id="Gradient" x1="0" x2="1" y1="0" y2="0">
          <stop :stop-color="backColor.begin" offset="0%"/>
          <stop :stop-color="backColor.end" offset="100%"/>
        </linearGradient>
      </defs>
      <!-- 长方形区域，宽1000，高30，淡蓝色  -->
      <rect fill="url(#Gradient)" height="80" width="1000"/>
      <!-- 弧形区域，从(0,30)出发，低谷点为(500,130)，到(1000,30)结束 -->
      <path d="M0 80 Q500 280 ,1000 80" fill="url(#Gradient)" stroke="transparent"/>
    </svg>
    <!--登陆卡片-->
    <h2 class="my-h2">单击小猫切换登录模式</h2>
    <!-- 用户登录 -->
    <div :style="{ transform: pairClient }" class="phm-card">
      <img alt="管理员入口" height="48" src="/mia.svg" width="64" @click="clientToAdmin">
      <LoginForm :role="RoleEnum.CLIENT" @loginSuc="getLoginRole"/>
      <div>
        <el-button round type="success" @click="$router.push('/reception')">游客登录</el-button>
        <el-button round type="warning" @click="$router.push('/register')">账号注册</el-button>
        <el-button round type="danger" @click="$router.push('/findPwd')">找回密码</el-button>
      </div>
    </div>
    <!-- 医生登录 -->
    <div :style="{ transform: pairAdmin }" class="phm-card">
      <img alt="普通入口" height="48" src="/mia.svg" width="64" @click="adminToClient">
      <LoginForm :role="RoleEnum.DOCTOR" @loginSuc="getLoginRole"/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import LoginForm from "@/components/zoneView/LoginForm.vue";
import {onMounted, reactive, ref} from "vue";
import {reqGetLogin} from "@/request/PowerApi";
import {useRouter} from "vue-router";
import {RoleEnum} from "@/model/VO/LoginVo";

// 登陆的初始化
onMounted(() => {
  getLoginRole()
})
// 判断当前登陆角色
const router = useRouter()
const getLoginRole = () => {
  reqGetLogin().then(resp => {
    if (resp === RoleEnum.CLIENT) router.push("/reception")
    else if (resp === RoleEnum.DOCTOR) router.push("/backstage")
  })
}

const pairClient = ref<string>("rotateY(0deg)")
const pairAdmin = ref<string>("rotateY(180deg)")
const backColor = reactive({
  begin: "MediumVioletRed",
  end: "MediumOrchid"
})

const clientToAdmin = () => {
  pairClient.value = "rotateY(180deg)"
  pairAdmin.value = "rotateY(0deg)";
  backColor.begin = "ForestGreen"
  backColor.end = "DeepSkyBlue"
}

const adminToClient = () => {
  pairAdmin.value = "rotateY(-180deg)";
  pairClient.value = "rotateY(0deg)"
  backColor.begin = "MediumVioletRed"
  backColor.end = "MediumOrchid"
}
</script>

<style lang="scss" scoped>
/*整个登录组件*/
.login-page {
  width: 98vw;
  height: 90vh;
  /* 背景的svg */
  svg {
    position: fixed;
    height: 50vh;
    width: 98vw;
  }

  /* 欢迎文字 */
  .my-h2 {
    position: fixed;
    color: #FAFAFA;
    left: 50%;
    transform: translateX(-50%);
  }
}

/* 翻转的卡片 */
.phm-card {
  position: absolute;
  width: 320px;
  margin-top: 8%;
  left: 50%;
  margin-left: -160px;
  text-align: center;
  justify-content: center;
  align-items: center;
  box-shadow: 7px 7px 17px rgba(52, 56, 66, 0.5);
  background-color: #FAFAFA;
  border-radius: 5px;
  backface-visibility: hidden;
  transition: all 1s;
  -webkit-user-select: none;
  user-select: none;
  padding: 10px;
}
</style>

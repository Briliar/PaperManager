<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧插画/背景区 -->
      <div class="login-left">
        <div class="left-content">
          <h1 class="title">🎓 毕业设计管理系统</h1>
          <p class="desc">高效、智能的高校毕设全流程管理平台</p>
          <ul class="features">
            <li>✨ 智能选题推荐</li>
            <li>📝 进度全景追踪</li>
            <li>📊 论文查重无缝对接</li>
          </ul>
        </div>
      </div>
      
      <!-- 右侧表单区 -->
      <div class="login-right">
        <div class="form-wrapper">
          <div class="form-header">
            <h2>欢迎登录</h2>
            <p>Please login to your account</p>
          </div>
          <el-form :model="loginForm" label-position="top" size="large">
            <el-form-item label="账号">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入学号 / 工号"
                :prefix-icon="User"
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码" 
                show-password
                :prefix-icon="Lock"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <div class="login-options">
              <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
              <el-link type="primary" :underline="false">忘记密码？</el-link>
            </div>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleLogin" class="login-btn">
                立 即 登 录
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import { login } from '../../api/auth'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loginForm = reactive({
  username: '',
  password: ''
})
const loading = ref(false)
const rememberMe = ref(false)

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  
  loading.value = true
  try {
    const res = await login(loginForm)
    userStore.setToken(res.token)
    userStore.setUserInfo({
      userId: res.userId,
      username: res.username,
      realName: res.realName,
      roleId: res.roleId
    })
    ElMessage.success('登录成功，欢迎回来！')
    router.push('/layout')
  } catch (error) {
    console.error('登录失败', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%);
  padding: 20px;
}

.login-box {
  display: flex;
  width: 1000px;
  height: 600px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: slideUp 0.8s cubic-bezier(0.16, 1, 0.3, 1);
}

.login-left {
  flex: 1;
  /* 使用 Unsplash 高清免费的高校/图书馆背景图 */
  background: url('https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=1200&auto=format&fit=crop') no-repeat center center;
  background-size: cover;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.login-left::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* 黑色半透明遮罩, 提升文字可读性 */
}

.left-content {
  position: relative;
  z-index: 1;
  padding: 40px;
  text-align: left;
  animation: fadeIn 1.2s ease-in-out;
}

.title {
  font-size: 36px;
  margin-bottom: 10px;
  font-weight: 700;
  letter-spacing: 2px;
}

.desc {
  font-size: 18px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.features {
  list-style: none;
  padding: 0;
  font-size: 16px;
  line-height: 2.5;
  opacity: 0.95;
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: #fff;
}

.form-wrapper {
  width: 100%;
  max-width: 360px;
  animation: fadeIn 1.5s ease;
}

.form-header {
  margin-bottom: 40px;
  text-align: center;
}

.form-header h2 {
  font-size: 28px;
  color: #333;
  margin: 0 0 8px 0;
}

.form-header p {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  letter-spacing: 4px;
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 242, 254, 0.3);
}

@keyframes slideUp {
  0% {
    opacity: 0;
    transform: translateY(40px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  0% { opacity: 0; }
  100% { opacity: 1; }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    height: auto;
  }
  .login-left {
    padding: 60px 20px;
  }
  .login-right {
    padding: 40px 20px;
  }
}
</style>

<template>
  <el-container class="app-wrapper">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar-container">
      <div class="logo-container">
        <span v-if="!isCollapse" class="logo-title">Grad System</span>
        <span v-else class="logo-title-mini">G</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <template #title>工作台首页</template>
        </el-menu-item>
        <el-menu-item index="/topic">
          <el-icon><Document /></el-icon>
          <template #title>选题管理</template>
        </el-menu-item>
        <el-menu-item index="/thesis">
          <el-icon><Files /></el-icon>
          <template #title>论文与答辩</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体区域 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="navbar">
        <div class="nav-left">
          <el-icon class="fold-btn" @click="toggleCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="nav-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="avatar-wrapper">
              <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
              <span class="username">{{ userStore.userInfo?.realName || 'Admin' }}</span>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 路由出口 (核心内容区) -->
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { House, Document, Files, Fold, Expand, CaretBottom } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 当前路由的Title（用于面包屑）
const currentRouteTitle = computed(() => {
  return route.meta.title || '工作台'
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要注销并退出系统吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.setToken('')
      userStore.setUserInfo({})
      ElMessage.success('已退出登录')
      router.push('/login')
    }).catch(() => {})
  } else if (command === 'profile') {
    ElMessage.info('个人中心模块待开发')
  }
}
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  width: 100%;
}

/* 侧边栏 */
.sidebar-container {
  transition: width 0.3s;
  background-color: #304156;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.logo-container {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: #2b3643;
  color: #fff;
  overflow: hidden;
}
.logo-title {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}
.logo-title-mini {
  font-size: 20px;
  font-weight: bold;
}
.el-menu-vertical {
  border-right: none;
  flex: 1;
}

/* 顶部导航 */
.navbar {
  height: 60px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px 0 0;
}
.nav-left {
  display: flex;
  align-items: center;
}
.fold-btn {
  padding: 0 20px;
  font-size: 20px;
  cursor: pointer;
  color: #5a5e66;
  transition: color 0.3s;
}
.fold-btn:hover {
  color: #409EFF;
}
.breadcrumb {
  margin-left: 10px;
}
.nav-right {
  display: flex;
  align-items: center;
}
.avatar-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 10px;
}
.username {
  margin: 0 8px;
  font-size: 14px;
  color: #606266;
}

/* 主体内容 */
.app-main {
  background-color: #f0f2f5;
  padding: 20px;
  position: relative;
  overflow-y: auto;
}

/* 路由切换动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>

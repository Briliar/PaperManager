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
        
        <!-- 教师/管理员 专属菜单 (roleId: 1 或 2) -->
        <template v-if="roleId === 1 || roleId === 2">
          <el-menu-item index="/topic">
            <el-icon><Document /></el-icon>
            <template #title>课题申报与管理</template>
          </el-menu-item>
          <el-menu-item index="/applications">
            <el-icon><User /></el-icon>
            <template #title>学生选题审批</template>
          </el-menu-item>
          <el-menu-item index="/proposal/teacher">
            <el-icon><Tickets /></el-icon>
            <template #title>开题报告审核</template>
          </el-menu-item>
        </template>

        <!-- 学生 专属菜单 (roleId: 3) -->
        <template v-if="roleId === 3">
          <el-menu-item index="/select">
            <el-icon><List /></el-icon>
            <template #title>学生双选大厅</template>
          </el-menu-item>
          <el-menu-item index="/proposal/student">
            <el-icon><EditPen /></el-icon>
            <template #title>我的开题报告</template>
          </el-menu-item>
          <el-menu-item index="/weekly/student">
            <el-icon><Calendar /></el-icon>
            <template #title>我的周志</template>
          </el-menu-item>
          <el-menu-item index="/thesis/student">
            <el-icon><Files /></el-icon>
            <template #title>论文与成果</template>
          </el-menu-item>
        </template>

      </el-menu>
    </el-aside>

    <!-- 主体区域 -->
    <el-container class="main-container">
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
              <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}</span>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">角色: {{ roleName }}</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

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
import { House, Calendar, Document, Files, List, User, Fold, Expand, CaretBottom, Tickets, EditPen } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

const roleId = computed(() => userStore.userInfo?.roleId || 1)

const roleName = computed(() => {
  if (roleId.value === 1) return '管理员'
  if (roleId.value === 2) return '指导老师'
  if (roleId.value === 3) return '学生'
  return '未知角色'
})

const activeMenu = computed(() => {
  return route.path
})

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
  }
}
</script>

<style scoped>
.app-wrapper { height: 100vh; width: 100%; }
.sidebar-container { transition: width 0.3s; background-color: #304156; overflow: hidden; display: flex; flex-direction: column; }
.logo-container { height: 60px; line-height: 60px; text-align: center; background-color: #2b3643; color: #fff; overflow: hidden; }
.logo-title { font-size: 18px; font-weight: 600; letter-spacing: 1px; }
.logo-title-mini { font-size: 20px; font-weight: bold; }
.el-menu-vertical { border-right: none; flex: 1; }
.navbar { height: 60px; overflow: hidden; background: #fff; box-shadow: 0 1px 4px rgba(0,21,41,.08); display: flex; align-items: center; justify-content: space-between; padding: 0 20px 0 0; }
.nav-left { display: flex; align-items: center; }
.fold-btn { padding: 0 20px; font-size: 20px; cursor: pointer; color: #5a5e66; transition: color 0.3s; }
.fold-btn:hover { color: #409EFF; }
.breadcrumb { margin-left: 10px; }
.nav-right { display: flex; align-items: center; }
.avatar-wrapper { display: flex; align-items: center; cursor: pointer; padding: 0 10px; }
.username { margin: 0 8px; font-size: 14px; color: #606266; }
.app-main { background-color: #f0f2f5; padding: 20px; position: relative; overflow-y: auto; }
.fade-transform-leave-active, .fade-transform-enter-active { transition: all 0.3s; }
.fade-transform-enter-from { opacity: 0; transform: translateX(-30px); }
.fade-transform-leave-to { opacity: 0; transform: translateX(30px); }
</style>

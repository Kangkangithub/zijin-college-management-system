<template>
  <div class="layout" :class="{ 'mobile-sidebar-open': sidebarOpen }">
    <div class="sidebar-overlay" v-if="isMobile && sidebarOpen" @click="sidebarOpen = false"></div>
    <aside class="sidebar" :class="{ open: sidebarOpen }">
      <div class="sidebar-logo"><h2>紫金学院</h2><p>信息管理系统</p></div>
      <el-menu :default-active="route.path" router :collapse="false" class="sidebar-menu">
        <el-menu-item index="/home"><el-icon><HomeFilled /></el-icon><span>首页</span></el-menu-item>
        <el-menu-item index="/announcement"><el-icon><Notification /></el-icon><span>公告互动</span></el-menu-item>
        <el-menu-item index="/leave"><el-icon><Calendar /></el-icon><span>请假管理</span></el-menu-item>
        <template v-if="roleCode === 'admin'">
          <el-sub-menu index="hr">
            <template #title><el-icon><UserFilled /></el-icon><span>人事管理</span></template>
            <el-menu-item index="/department">部门管理</el-menu-item>
            <el-menu-item index="/major">专业管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="edu">
            <template #title><el-icon><School /></el-icon><span>教务管理</span></template>
            <el-menu-item index="/class">班级管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="sys">
            <template #title><el-icon><Setting /></el-icon><span>系统管理</span></template>
            <el-menu-item index="/account">账户维护</el-menu-item>
          </el-sub-menu>
        </template>
        <template v-if="roleCode === 'teacher'">
          <el-sub-menu index="edu">
            <template #title><el-icon><School /></el-icon><span>教务管理</span></template>
            <el-menu-item index="/class">班级管理</el-menu-item>
          </el-sub-menu>
        </template>
        <template v-if="roleCode === 'student'">
          <el-menu-item index="/profile"><el-icon><Reading /></el-icon><span>我的信息</span></el-menu-item>
        </template>
      </el-menu>
    </aside>
    <div class="main-area">
      <header class="topbar">
        <div class="topbar-left">
          <el-button class="menu-toggle" text @click="sidebarOpen = !sidebarOpen">
            <el-icon size="22"><component :is="sidebarOpen ? 'Close' : 'Menu'" /></el-icon>
          </el-button>
          <span class="page-title">{{ route.meta.title || '紫金学院管理系统' }}</span>
        </div>
        <div class="topbar-right">
          <el-tag :type="roleTagType" size="small">{{ roleName }}</el-tag>
          <span class="user-name">{{ userName }}</span>
          <el-dropdown>
            <el-avatar :size="34" :icon="UserFilled" style="background:#1a237e;cursor:pointer" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>{{ userName }}</el-dropdown-item>
                <el-dropdown-item disabled>{{ roleName }}</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <div class="mobile-breadcrumb" v-if="isMobile && route.meta.title && route.meta.title !== '首页'">
        <el-icon><ArrowRight /></el-icon> {{ route.meta.title }}
      </div>
      <main class="content"><router-view v-slot="{ Component }"><transition name="slide-up" mode="out-in"><component :is="Component" /></transition></router-view></main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { HomeFilled, UserFilled, School, Setting, Reading, Menu, Close, ArrowRight, Notification, Calendar } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const sidebarOpen = ref(false)
const isMobile = ref(window.innerWidth < 768)
const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const userName = user.realName || user.username || ''
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleMap = { admin: '管理员', teacher: '教师', student: '学生' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')
const roleName = computed(() => roleMap[roleCode.value])
const roleTagType = computed(() => roleCode.value === 'admin' ? 'danger' : roleCode.value === 'teacher' ? 'warning' : 'success')

const handleResize = () => { isMobile.value = window.innerWidth < 768 }
onMounted(() => window.addEventListener('resize', handleResize))
onUnmounted(() => window.removeEventListener('resize', handleResize))

const handleLogout = async () => {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  localStorage.removeItem('zijin_user')
  router.push('/login')
}
</script>

<style scoped>
.layout { display: flex; min-height: 100vh; background: #f0f2f5; }
.sidebar { width: 240px; background: linear-gradient(180deg, #1a237e 0%, #283593 100%); color: #fff; flex-shrink: 0; display: flex; flex-direction: column; transition: transform 0.3s; z-index: 100; }
.sidebar-logo { padding: 20px 16px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1); }
.sidebar-logo h2 { margin: 0; font-size: 18px; letter-spacing: 2px; }
.sidebar-logo p { margin: 4px 0 0; font-size: 12px; opacity: 0.7; }
.sidebar-menu { border-right: none; background: transparent; }
.sidebar-menu { --el-menu-text-color: rgba(255,255,255,0.85); --el-menu-bg-color: transparent; --el-menu-hover-bg-color: rgba(255,255,255,0.1); }
.sidebar-menu .el-menu-item { color: rgba(255,255,255,0.85) !important; }
.sidebar-menu .el-menu-item:hover { background: rgba(255,255,255,0.1) !important; color: #fff !important; }
.sidebar-menu .el-menu-item.is-active { background: rgba(255,255,255,0.2) !important; color: #fff !important; }
.sidebar-menu :deep(.el-sub-menu__title) { color: rgba(255,255,255,0.85) !important; border-bottom-color: transparent !important; }
.sidebar-menu :deep(.el-sub-menu__title):hover { color: #fff !important; background: rgba(255,255,255,0.1) !important; }
.sidebar-menu :deep(.el-sub-menu.is-opened) > .el-sub-menu__title { color: #fff !important; }
.sidebar-menu :deep(.el-menu--inline) { background: rgba(0,0,0,0.15) !important; }
.sidebar-menu :deep(.el-menu--inline .el-menu-item) { color: rgba(255,255,255,0.75) !important; }
.sidebar-menu :deep(.el-menu--inline .el-menu-item):hover { background: rgba(255,255,255,0.15) !important; color: #fff !important; }
.sidebar-menu :deep(.el-menu--inline .el-menu-item.is-active) { background: rgba(255,255,255,0.25) !important; color: #fff !important; }
.main-area { flex: 1; display: flex; flex-direction: column; min-width: 0; }
.topbar { height: 56px; background: #fff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.topbar-left { display: flex; align-items: center; gap: 12px; }
.page-title { font-size: 16px; font-weight: 600; color: #333; }
.topbar-right { display: flex; align-items: center; gap: 12px; }
.user-name { font-size: 14px; color: #666; }
.menu-toggle { display: none; }
.mobile-breadcrumb { background: #fff; padding: 10px 16px; font-size: 14px; color: #666; display: flex; align-items: center; gap: 4px; border-bottom: 1px solid #f0f0f0; }
.content { flex: 1; padding: 20px; overflow-y: auto; }
@media (max-width: 767px) {
  .menu-toggle { display: inline-flex; }
  .sidebar { position: fixed; left: 0; top: 0; height: 100vh; transform: translateX(-100%); }
  .sidebar.open { transform: translateX(0); }
  .sidebar-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 99; }
  .topbar { padding: 0 12px; }
  .topbar-right .user-name { display: none; }
  .content { padding: 12px; }
}
</style>
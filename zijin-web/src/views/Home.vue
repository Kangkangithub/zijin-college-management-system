<template>
  <div class="home">
    <div class="welcome-card">
      <el-icon size="48" color="#1a237e"><HomeFilled /></el-icon>
      <h1>欢迎回来，{{ userName }}</h1>
      <el-tag :type="roleTagType" size="large">{{ roleName }}</el-tag>
    </div>
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="8" v-if="roleCode==='admin'">
        <el-card shadow="hover" class="stat-card"><el-statistic title="部门总数" :value="stats.depts" /></el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" v-if="roleCode==='admin'||roleCode==='teacher'">
        <el-card shadow="hover" class="stat-card"><el-statistic title="班级总数" :value="stats.classes" /></el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" v-if="roleCode==='admin'">
        <el-card shadow="hover" class="stat-card"><el-statistic title="用户总数" :value="stats.users" /></el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" v-if="roleCode==='student'">
        <el-card shadow="hover" class="stat-card"><el-statistic title="我的班级" :value="studentInfo.className || '-'" /></el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" v-if="roleCode==='student'">
        <el-card shadow="hover" class="stat-card"><el-statistic title="我的专业" :value="studentInfo.majorName || '-'" /></el-card>
      </el-col>
    </el-row>
    <el-card shadow="hover" class="info-card">
      <template #header><span>系统信息</span></template>
      <p>紫金学院管理系统 v1.0</p>
      <p>南京理工大学紫金学院 · 跨平台开发实践课程设计</p>
      <p>技术栈：SpringBoot + MySQL + Vue3 + Element Plus</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { HomeFilled } from '@element-plus/icons-vue'
import api from '../api'

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const userName = user.realName || user.username || ''
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleMap = { admin: '管理员', teacher: '教师', student: '学生' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')
const roleName = computed(() => roleMap[roleCode.value])
const roleTagType = computed(() => roleCode.value === 'admin' ? 'danger' : roleCode.value === 'teacher' ? 'warning' : 'success')

const stats = ref({ depts: 0, classes: 0, users: 0 })
const studentInfo = ref({})

onMounted(async () => {
  try {
    if (roleCode.value === 'admin') {
      const [d, c, u] = await Promise.all([api.get('/department'), api.get('/class'), api.get('/user')])
      if (d.code === 200) stats.value.depts = d.data.length
      if (c.code === 200) stats.value.classes = c.data.length
      if (u.code === 200) stats.value.users = u.data.length
    } else if (roleCode.value === 'teacher') {
      const c = await api.get('/class')
      if (c.code === 200) stats.value.classes = c.data.length
    } else if (roleCode.value === 'student') {
      const [mRes, cRes] = await Promise.all([api.get('/major'), api.get('/class')])
      const majors = mRes.code === 200 ? mRes.data : []
      const classes = cRes.code === 200 ? cRes.data : []
      const m = majors.find(x => x.id === user.majorId)
      const c = classes.find(x => x.id === user.classId)
      studentInfo.value = { majorName: m ? m.majorName : '-', className: c ? c.className : '-' }
    }
  } catch { /* ignore */ }
})
</script>

<style scoped>
.home { max-width: 1200px; margin: 0 auto; }
.welcome-card { background: linear-gradient(135deg, #1a237e, #4a148c); color: #fff; padding: 36px; border-radius: 12px; display: flex; align-items: center; gap: 16px; margin-bottom: 20px; }
.welcome-card h1 { margin: 0; font-size: 22px; font-weight: 500; }
.stats-row { margin-bottom: 20px; }
.stat-card { text-align: center; }
.info-card { color: #666; font-size: 14px; }
.info-card p { margin: 6px 0; }
@media (max-width: 767px) { .welcome-card { padding: 20px; flex-direction: column; text-align: center; } .welcome-card h1 { font-size: 18px; } }
</style>
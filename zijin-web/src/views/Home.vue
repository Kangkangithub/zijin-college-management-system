<template>
  <div class="home">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-bg"></div>
      <div class="banner-content">
        <div class="banner-avatar">
          <span class="avatar-text">{{ userName.charAt(0) }}</span>
        </div>
        <div class="banner-text">
          <h1 class="greeting">{{ greeting }}，{{ userName }}</h1>
          <p class="subtitle">{{ subtitle }}</p>
        </div>
        <div class="banner-tag">
          <el-tag :type="roleTagType" size="large" effect="dark" round>{{ roleName }}</el-tag>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" v-for="card in statCards" :key="card.label" class="stat-col">
        <div class="stat-card" :style="{ animationDelay: card.delay }">
          <div class="stat-icon" :style="{ background: card.color }">
            <el-icon size="24"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">
              <span class="counter">{{ card.value }}</span>
            </div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="20" class="chart-row" v-if="roleCode==='admin'">
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover" class="chart-card">
          <template #header><div class="card-title"><el-icon><DataBoard /></el-icon> 部门班级统计</div></template>
          <v-chart :option="barOption" autoresize class="chart" />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover" class="chart-card">
          <template #header><div class="card-title"><el-icon><PieChart /></el-icon> 角色分布</div></template>
          <v-chart :option="pieOption" autoresize class="chart" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 教师/学生专属卡片 -->
    <el-row :gutter="20" class="chart-row" v-if="roleCode==='teacher'||roleCode==='student'">
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover" class="chart-card">
          <template #header><div class="card-title"><el-icon><DataBoard /></el-icon> 系统数据概览</div></template>
          <v-chart :option="overviewOption" autoresize class="chart" />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover" class="quick-card">
          <template #header><div class="card-title"><el-icon><Opportunity /></el-icon> 快捷入口</div></template>
          <div class="quick-links">
            <div v-for="link in quickLinks" :key="link.path" class="quick-link" @click="$router.push(link.path)">
              <el-icon size="22" :color="link.color"><component :is="link.icon" /></el-icon>
              <span>{{ link.label }}</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部信息 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="info-card">
          <template #header><div class="card-title"><el-icon><InfoFilled /></el-icon> 系统信息</div></template>
          <div class="info-list">
            <div class="info-item">
              <span class="info-key">系统版本</span>
              <el-tag size="small" type="success">v2.0</el-tag>
            </div>
            <div class="info-item">
              <span class="info-key">技术架构</span>
              <span>SpringBoot + Vue3 + MySQL</span>
            </div>
            <div class="info-item">
              <span class="info-key">UI 组件</span>
              <span>Element Plus + ECharts</span>
            </div>
            <div class="info-item">
              <span class="info-key">数据表</span>
              <span>8 张</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="info-card">
          <template #header><div class="card-title"><el-icon><Clock /></el-icon> 最近更新</div></template>
          <div class="timeline">
            <div class="tl-item" v-for="(item, i) in timeline" :key="i">
              <div class="tl-dot"></div>
              <div>
                <div class="tl-text">{{ item.text }}</div>
                <div class="tl-time">{{ item.time }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { HomeFilled, UserFilled, School, OfficeBuilding, Files, DataBoard, PieChart, Opportunity, ArrowRight, InfoFilled, Clock, ChatDotRound, DocumentChecked } from '@element-plus/icons-vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { BarChart, PieChart as ECPieChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import api from '../api'

use([BarChart, ECPieChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent, CanvasRenderer])

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const userName = user.realName || user.username || ''
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleMap = { admin: '管理员', teacher: '教师', student: '学生' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')
const roleName = computed(() => roleMap[roleCode.value])
const roleTagType = computed(() => roleCode.value === 'admin' ? 'danger' : roleCode.value === 'teacher' ? 'warning' : 'success')

const hour = new Date().getHours()
const greeting = computed(() => hour < 12 ? '早上好' : hour < 18 ? '下午好' : '晚上好')
const subtitle = computed(() => roleCode.value === 'admin' ? '掌控全局，高效管理' : roleCode.value === 'teacher' ? '教书育人，桃李芬芳' : '学海无涯，砥砺前行')

const stats = ref({})
const statCards = computed(() => {
  const cards = []
  if (roleCode.value === 'admin') {
    cards.push({ label: '部门总数', value: stats.value.deptCount||0, icon: OfficeBuilding, color: 'linear-gradient(135deg, #667eea, #764ba2)', delay: '0s' })
    cards.push({ label: '专业总数', value: stats.value.majorCount||0, icon: Files, color: 'linear-gradient(135deg, #f093fb, #f5576c)', delay: '0.1s' })
    cards.push({ label: '班级总数', value: stats.value.classCount||0, icon: School, color: 'linear-gradient(135deg, #4facfe, #00f2fe)', delay: '0.2s' })
    cards.push({ label: '用户总数', value: stats.value.userCount||0, icon: UserFilled, color: 'linear-gradient(135deg, #43e97b, #38f9d7)', delay: '0.3s' })
  } else if (roleCode.value === 'teacher') {
    cards.push({ label: '班级总数', value: stats.value.classCount||0, icon: School, color: 'linear-gradient(135deg, #4facfe, #00f2fe)', delay: '0s' })
    cards.push({ label: '学生总数', value: stats.value.studentCount||0, icon: UserFilled, color: 'linear-gradient(135deg, #43e97b, #38f9d7)', delay: '0.1s' })
    cards.push({ label: '公告总数', value: stats.value.announcementCount||0, icon: ChatDotRound, color: 'linear-gradient(135deg, #f093fb, #f5576c)', delay: '0.2s' })
    cards.push({ label: '待审批', value: stats.value.leaveCount||0, icon: DocumentChecked, color: 'linear-gradient(135deg, #fa709a, #fee140)', delay: '0.3s' })
  } else {
    cards.push({ label: '班级人数', value: stats.value.studentCount||'-', icon: School, color: 'linear-gradient(135deg, #4facfe, #00f2fe)', delay: '0s' })
    cards.push({ label: '同学总数', value: stats.value.userCount||'-', icon: UserFilled, color: 'linear-gradient(135deg, #43e97b, #38f9d7)', delay: '0.1s' })
    cards.push({ label: '公告总数', value: stats.value.announcementCount||0, icon: ChatDotRound, color: 'linear-gradient(135deg, #f093fb, #f5576c)', delay: '0.2s' })
    cards.push({ label: '我的请假', value: stats.value.leaveCount||0, icon: DocumentChecked, color: 'linear-gradient(135deg, #fa709a, #fee140)', delay: '0.3s' })
  }
  return cards
})

const quickLinks = computed(() => {
  if (roleCode.value === 'teacher') return [
    { path: '/class', label: '班级管理', icon: School, color: '#4facfe' },
    { path: '/announcement', label: '发布公告', icon: ChatDotRound, color: '#f093fb' },
    { path: '/leave', label: '审批请假', icon: DocumentChecked, color: '#43e97b' },
  ]
  return [
    { path: '/announcement', label: '查看公告', icon: ChatDotRound, color: '#f093fb' },
    { path: '/leave', label: '我的请假', icon: DocumentChecked, color: '#43e97b' },
    { path: '/profile', label: '个人信息', icon: UserFilled, color: '#667eea' },
  ]
})

const timeline = [
  { text: '新增公告互动系统', time: '2026-06-06' },
  { text: '新增请假审批系统', time: '2026-06-06' },
  { text: '上线 ECharts 数据可视化', time: '2026-06-10' },
  { text: '首页 UI 全面升级', time: '2026-06-10' },
]

const barOption = computed(() => ({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category', data: ['部门', '专业', '班级', '用户', '公告', '请假'], axisLabel: { color: '#666' } },
  yAxis: { type: 'value', axisLabel: { color: '#666' }, splitLine: { lineStyle: { color: '#f0f0f0' } } },
  series: [{
    type: 'bar',
    data: [stats.value.deptCount||0, stats.value.majorCount||0, stats.value.classCount||0, stats.value.userCount||0, stats.value.announcementCount||0, stats.value.leaveCount||0],
    itemStyle: {
      borderRadius: [6, 6, 0, 0],
      color: function(p) {
        const colors = ['#667eea','#f093fb','#4facfe','#43e97b','#fa709a','#f5af19']
        return colors[p.dataIndex]
      }
    },
    barWidth: '50%',
    animationDelay: function(idx) { return idx * 100 }
  }]
}))

const pieOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, textStyle: { color: '#666' } },
  series: [{
    type: 'pie',
    radius: ['50%', '75%'],
    avoidLabelOverlap: false,
    itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
    label: { show: false },
    emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
    data: [
      { value: stats.value.adminCount||0, name: '管理员', itemStyle: { color: '#f56c6c' } },
      { value: stats.value.teacherCount||0, name: '教师', itemStyle: { color: '#e6a23c' } },
      { value: stats.value.studentCount||0, name: '学生', itemStyle: { color: '#67c23a' } },
    ]
  }]
}))

const overviewOption = computed(() => ({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category', data: ['班级', '学生', '公告', '请假'], axisLabel: { color: '#666' } },
  yAxis: { type: 'value', axisLabel: { color: '#666' }, splitLine: { lineStyle: { color: '#f0f0f0' } } },
  series: [{
    type: 'bar',
    data: [stats.value.classCount||0, stats.value.studentCount||0, stats.value.announcementCount||0, stats.value.leaveCount||0],
    itemStyle: {
      borderRadius: [6, 6, 0, 0],
      color: function(p) {
        return ['#4facfe','#43e97b','#f093fb','#fa709a'][p.dataIndex]
      }
    },
    barWidth: '50%',
  }]
}))

onMounted(async () => {
  try {
    const s = await api.get('/stats')
    if (s.code === 200) stats.value = s.data
  } catch { /* ignore */ }
})
</script>

<style scoped>
.home { max-width: 1300px; margin: 0 auto; }

/* 欢迎横幅 */
.welcome-banner {
  position: relative; overflow: hidden; border-radius: 16px;
  background: linear-gradient(135deg, #1a237e 0%, #283593 30%, #4a148c 70%, #6a1b9a 100%);
  margin-bottom: 24px; padding: 28px 32px;
}
.banner-bg {
  position: absolute; inset: 0;
  background: radial-gradient(circle at 80% 20%, rgba(255,255,255,0.08) 0%, transparent 50%),
              radial-gradient(circle at 20% 80%, rgba(255,255,255,0.05) 0%, transparent 40%);
}
.banner-content {
  position: relative; display: flex; align-items: center; gap: 20px;
  flex-wrap: wrap;
}
.banner-avatar {
  width: 56px; height: 56px; border-radius: 50%;
  background: rgba(255,255,255,0.2); backdrop-filter: blur(8px);
  display: flex; align-items: center; justify-content: center;
  border: 2px solid rgba(255,255,255,0.3);
  flex-shrink: 0;
}
.avatar-text { font-size: 24px; font-weight: 700; color: #fff; }
.greeting { font-size: 22px; font-weight: 600; color: #fff; margin: 0 0 4px; }
.subtitle { font-size: 14px; color: rgba(255,255,255,0.75); margin: 0; }
.banner-tag { margin-left: auto; }

/* 统计卡片 */
.stats-row { margin-bottom: 24px; }
.stat-col { margin-bottom: 16px; }
.stat-card {
  background: #fff; border-radius: 14px; padding: 20px; display: flex;
  align-items: center; gap: 16px; cursor: pointer;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  animation: fadeInUp 0.5s both;
}
.stat-card:hover { transform: translateY(-6px); box-shadow: 0 12px 28px rgba(0,0,0,0.12); }
.stat-icon {
  width: 52px; height: 52px; border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  color: #fff; flex-shrink: 0;
}
.stat-info { flex: 1; min-width: 0; }
.counter { font-size: 32px; font-weight: 700; color: #1a237e; line-height: 1.2; }
.stat-label { font-size: 13px; color: #999; margin-top: 2px; }

/* 图表区 */
.chart-row { margin-bottom: 24px; }
.chart-card, .quick-card, .info-card { border-radius: 14px; overflow: hidden; }
.chart-card :deep(.el-card__header) { background: #fafbff; border-bottom: 1px solid #f0f0f5; padding: 14px 20px; }
.quick-card :deep(.el-card__header) { background: #fafbff; padding: 14px 20px; }
.info-card :deep(.el-card__header) { background: #fafbff; padding: 14px 20px; }
.card-title { display: flex; align-items: center; gap: 8px; font-size: 15px; font-weight: 600; color: #333; }
.chart { height: 300px; }

/* 快捷入口 */
.quick-links { display: flex; flex-direction: column; gap: 4px; }
.quick-link {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 16px; border-radius: 10px;
  transition: all 0.25s; cursor: pointer;
}
.quick-link:hover { background: #f5f7ff; transform: translateX(4px); }
.quick-link span { flex: 1; font-size: 14px; font-weight: 500; color: #333; }

/* 系统信息 */
.bottom-row { margin-bottom: 24px; }
.info-list { display: flex; flex-direction: column; gap: 10px; }
.info-item { display: flex; align-items: center; justify-content: space-between; font-size: 14px; }
.info-key { color: #999; }
.timeline { position: relative; padding-left: 16px; }
.timeline::before { content: ''; position: absolute; left: 4px; top: 6px; bottom: 6px; width: 2px; background: #e8e8f0; border-radius: 1px; }
.tl-item { position: relative; padding: 6px 0 6px 16px; }
.tl-dot { position: absolute; left: -14px; top: 12px; width: 8px; height: 8px; border-radius: 50%; background: #667eea; }
.tl-text { font-size: 14px; color: #333; }
.tl-time { font-size: 12px; color: #bbb; margin-top: 2px; }

/* 动画 */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 767px) {
  .welcome-banner { padding: 20px; }
  .greeting { font-size: 16px; }
  .counter { font-size: 24px; }
  .stat-card { padding: 14px; }
  .stat-icon { width: 40px; height: 40px; border-radius: 10px; }
  .chart { height: 240px; }
}
</style>
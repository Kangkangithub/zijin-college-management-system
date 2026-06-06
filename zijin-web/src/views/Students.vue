<template>
  <div class="page">
    <div class="page-header">
      <h2>学生管理</h2>
      <el-select v-model="filterClass" placeholder="按班级筛选" clearable style="width:200px" @change="fetchData">
        <el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" />
      </el-select>
    </div>
    <el-card shadow="hover">
      <el-table :data="filteredList" stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="studentNo" label="学号" width="110" />`n        <el-table-column prop="realName" label="姓名" width="100" />`n        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column label="班级" width="150">
          <template #default="{ row }">{{ classMap[row.classId] || '-' }}</template>
        </el-table-column>
        <el-table-column label="专业" width="150">
          <template #default="{ row }">{{ majorMap[row.majorId] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="grade" label="年级" width="90" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api'

const loading = ref(false)
const allUsers = ref([])
const classes = ref([])
const classMap = ref({})
const majorMap = ref({})
const filterClass = ref(null)

const filteredList = computed(() => {
  if (!filterClass.value) return allUsers.value
  return allUsers.value.filter(u => u.classId === filterClass.value)
})

onMounted(async () => {
  loading.value = true
  const [uRes, cRes, mRes] = await Promise.all([api.get('/user'), api.get('/class'), api.get('/major')])
  if (uRes.code === 200) allUsers.value = uRes.data.filter(u => u.roleId === 3)
  if (cRes.code === 200) { classes.value = cRes.data; classMap.value = Object.fromEntries(cRes.data.map(c => [c.id, c.className])) }
  if (mRes.code === 200) majorMap.value = Object.fromEntries(mRes.data.map(m => [m.id, m.majorName]))
  loading.value = false
})
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h2 { margin: 0; font-size: 18px; color: #333; }
@media (max-width: 767px) {
  .page-header { flex-wrap: wrap; gap: 8px; }
  .page-header h2 { font-size: 16px; }
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .cell) { padding: 6px 4px !important; }
}
</style>
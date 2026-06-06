<template>
  <div class="page">
    <div class="page-header"><h2>我的信息</h2></div>
    <el-row :gutter="20">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="info-card">
          <template #header><span style="font-weight:600">个人资料</span></template>
          <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
            <el-form-item label="学号"><el-input v-model="form.studentNo" disabled /></el-form-item>
            <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
            <el-form-item label="姓名" prop="realName"><el-input v-model="form.realName" placeholder="请输入姓名" /></el-form-item>
            <el-form-item label="性别"><el-select v-model="form.gender" style="width:100%"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item>
            <el-form-item label="手机号"><el-input v-model="form.phone" placeholder="请输入手机号" /></el-form-item>
            <el-form-item label="邮箱"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item>
            <el-form-item label="新密码"><el-input v-model="form.newPwd" type="password" placeholder="留空则不修改" show-password /></el-form-item>
            <el-form-item><el-button type="primary" @click="handleSave" :loading="saving">保存修改</el-button></el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="info-card">
          <template #header><span style="font-weight:600">班级信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="年级">{{ userInfo.grade || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ majorName }}</el-descriptions-item>
            <el-descriptions-item label="班级">{{ className }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        <el-card shadow="hover" class="info-card" style="margin-top:16px">
          <template #header><span style="font-weight:600">同班同学</span></template>
          <el-table :data="classmates" stripe max-height="300" empty-text="暂无数据">
            <el-table-column prop="studentNo" label="学号" width="100" />
            <el-table-column prop="realName" label="姓名" width="80" />
            <el-table-column prop="gender" label="性别" width="60" />
            <el-table-column prop="phone" label="手机号" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const formRef = ref(null); const saving = ref(false)
const classmates = ref([])
const majorName = ref('未设置')
const className = ref('未设置')
const userInfo = ref({})

const form = reactive({
  id: user.id, username: user.username || '',
  studentNo: user.studentNo || '未设置', realName: user.realName || '',
  gender: user.gender || '男', phone: user.phone || '', email: user.email || '',
  newPwd: ''
})
const rules = { realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }] }

onMounted(async () => {
  try {
    // Get full user info
    const uRes = await api.get('/user')
    if (uRes.code === 200) {
      const full = uRes.data.find(u => u.id === user.id)
      if (full) {
        userInfo.value = full
        Object.assign(form, {
          studentNo: full.studentNo || '未设置',
          realName: full.realName || '',
          gender: full.gender || '男',
          phone: full.phone || '',
          email: full.email || ''
        })
      }
    }
    // Get major and class names
    const [mRes, cRes] = await Promise.all([api.get('/major'), api.get('/class')])
    const majors = mRes.code === 200 ? mRes.data : []
    const classes = cRes.code === 200 ? cRes.data : []
    const m = majors.find(x => x.id === user.majorId)
    const c = classes.find(x => x.id === user.classId)
    if (m) majorName.value = m.majorName
    if (c) className.value = c.className

    // Get classmates
    const allUsers = uRes.data || []
    classmates.value = allUsers.filter(u => u.roleId === 3 && u.classId === user.classId && u.id !== user.id)
  } catch { /* ignore */ }
})

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    const data = {
      id: form.id, realName: form.realName, gender: form.gender,
      phone: form.phone, email: form.email
    }
    if (form.newPwd) data.pwd = form.newPwd
    await api.put('/user', data)
    // Update localStorage
    const stored = JSON.parse(localStorage.getItem('zijin_user') || '{}')
    stored.realName = form.realName
    localStorage.setItem('zijin_user', JSON.stringify(stored))
    ElMessage.success('保存成功')
  } catch { ElMessage.error('保存失败') }
  saving.value = false
}
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 16px; }
.page-header h2 { margin: 0; font-size: 18px; color: #333; }
.info-card { margin-bottom: 0; }
@media (max-width: 767px) {
  .page-header h2 { font-size: 16px; }
  :deep(.el-form-item__label) { width: 70px !important; font-size: 13px; }
  :deep(.el-table) { font-size: 13px; }
}
</style>
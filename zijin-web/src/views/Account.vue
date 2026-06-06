<template>
  <div class="page">
    <div class="page-header">
      <h2>账户维护</h2>
      <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增用户</el-button>
    </div>
    <el-card shadow="hover">
      <el-table :data="list" stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="studentNo" label="学号" width="100" />`n        <el-table-column prop="realName" label="真实姓名" width="100" />`n        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.roleId===1?'danger':row.roleId===2?'warning':'success'" size="small">{{ roleMap[row.roleId] || '学生' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="年级" width="90" />
        <el-table-column label="专业" width="130" show-overflow-tooltip><template #default="{ row }">{{ majorMap[row.majorId] || '-' }}</template></el-table-column>
        <el-table-column label="班级" width="130" show-overflow-tooltip><template #default="{ row }">{{ classMap[row.classId] || '-' }}</template></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }"><el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="toggleStatus(row)" size="small" /></template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" /></el-form-item>
        <el-form-item label="密码" :prop="isEdit ? '' : 'pwd'"><el-input v-model="form.pwd" type="password" :placeholder="isEdit ? '留空则不修改' : '请输入密码'" show-password /></el-form-item>
        <el-form-item label="真实姓名" prop="realName"><el-input v-model="form.realName" placeholder="请输入真实姓名" /></el-form-item>
        <el-form-item label="学号"><el-input v-model="form.studentNo" placeholder="请输入学号" /></el-form-item>`n        <el-form-item label="性别"><el-select v-model="form.gender" placeholder="请选择" style="width:100%" clearable><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item>`n        <el-form-item label="角色" prop="roleId"><el-select v-model="form.roleId" placeholder="请选择角色" style="width:100%"><el-option label="管理员" :value="1" /><el-option label="教师" :value="2" /><el-option label="学生" :value="3" /></el-select></el-form-item>
        <el-form-item label="年级"><el-select v-model="form.grade" placeholder="请选择年级" style="width:100%" clearable><el-option label="2021级" value="2021级" /><el-option label="2022级" value="2022级" /><el-option label="2023级" value="2023级" /><el-option label="2024级" value="2024级" /></el-select></el-form-item>
        <el-form-item label="专业"><el-select v-model="form.majorId" placeholder="请选择专业" style="width:100%" clearable><el-option v-for="m in majors" :key="m.id" :label="m.majorName" :value="m.id" /></el-select></el-form-item>
        <el-form-item label="班级"><el-select v-model="form.classId" placeholder="请选择班级" style="width:100%" clearable><el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" /></el-select></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" placeholder="请输入手机号" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave" :loading="saving">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '../api'

const roleMap = { 1: '管理员', 2: '教师', 3: '学生' }
const list = ref([]); const majors = ref([]); const classes = ref([]); const majorMap = ref({}); const classMap = ref({}); const loading = ref(false); const dialogVisible = ref(false); const saving = ref(false); const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, username: '', pwd: '', realName: '', studentNo: '', gender: '男', roleId: 3, grade: '', majorId: null, classId: null, phone: '', email: '' })
const rules = { username: [{ required: true, message: '请输入用户名', trigger: 'blur' }], realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }], roleId: [{ required: true, message: '请选择角色', trigger: 'change' }] }

const fetchData = async () => { loading.value = true; const [uRes, mRes, cRes] = await Promise.all([api.get('/user'), api.get('/major'), api.get('/class')]); if (uRes.code === 200) list.value = uRes.data; if (mRes.code === 200) { majors.value = mRes.data; majorMap.value = Object.fromEntries(mRes.data.map(m => [m.id, m.majorName])) }; if (cRes.code === 200) { classes.value = cRes.data; classMap.value = Object.fromEntries(cRes.data.map(c => [c.id, c.className])) }; loading.value = false }
const openDialog = (row) => { isEdit.value = !!row; if (row) { Object.assign(form, { ...row, pwd: '' }) } else { Object.assign(form, { id: null, username: '', pwd: '', realName: '', studentNo: '', gender: '男', roleId: 3, grade: '', majorId: null, classId: null, phone: '', email: '' }) }; dialogVisible.value = true }
const handleSave = async () => { const valid = await formRef.value.validate().catch(() => false); if (!valid) return; if (!isEdit.value && !form.pwd) { ElMessage.error('请输入密码'); return }; saving.value = true; try { if (isEdit.value) await api.put('/user', form); else await api.post('/user', form); ElMessage.success(isEdit.value ? '修改成功' : '新增成功'); dialogVisible.value = false; fetchData() } catch { ElMessage.error('操作失败') } saving.value = false }
const handleDelete = async (id) => { await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' }); await api.delete('/user/' + id); ElMessage.success('删除成功'); fetchData() }
const toggleStatus = async (row) => { try { await api.put('/user', { id: row.id, status: row.status }); ElMessage.success(row.status === 1 ? '已启用' : '已禁用') } catch { ElMessage.error('操作失败'); fetchData() } }
onMounted(fetchData)
</script>

<style scoped>
.page { max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h2 { margin: 0; font-size: 18px; color: #333; }

@media (max-width: 767px) {
  .page-header { flex-wrap: wrap; gap: 8px; }
  .page-header h2 { font-size: 16px; }
  :deep(.el-dialog) { width: 94% !important; margin: 20px auto !important; border-radius: 12px; }
  :deep(.el-dialog__body) { padding: 16px 12px !important; }
  :deep(.el-dialog__header) { padding: 14px 16px 8px !important; }
  :deep(.el-dialog__footer) { padding: 8px 16px 14px !important; }
  :deep(.el-form-item__label) { width: 70px !important; font-size: 13px; }
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .cell) { padding: 6px 4px !important; }
}
</style>
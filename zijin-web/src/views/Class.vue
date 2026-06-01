<template>
  <div class="page">
    <div class="page-header">
      <h2>班级管理</h2>
      <el-button type="primary" @click="openDialog()" v-if="roleCode==='admin'"><el-icon><Plus /></el-icon>新增班级</el-button>
    </div>
    <el-card shadow="hover">
      <el-table :data="list" stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="className" label="班级名称" />
        <el-table-column prop="classCode" label="班级编码" width="130" />
        <el-table-column label="所属专业" width="180"><template #default="{ row }">{{ majorMap[row.majorId] || '-' }}</template></el-table-column>
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="160" fixed="right" v-if="roleCode==='admin'">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑班级' : '新增班级'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="班级名称" prop="className"><el-input v-model="form.className" placeholder="请输入班级名称" /></el-form-item>
        <el-form-item label="班级编码" prop="classCode"><el-input v-model="form.classCode" placeholder="请输入班级编码" /></el-form-item>
        <el-form-item label="所属专业" prop="majorId"><el-select v-model="form.majorId" placeholder="请选择专业" style="width:100%"><el-option v-for="m in majors" :key="m.id" :label="m.majorName" :value="m.id" /></el-select></el-form-item>
        <el-form-item label="年级" prop="grade"><el-select v-model="form.grade" placeholder="请选择年级" style="width:100%"><el-option label="2021级" value="2021级" /><el-option label="2022级" value="2022级" /><el-option label="2023级" value="2023级" /><el-option label="2024级" value="2024级" /></el-select></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" placeholder="请输入描述" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSave" :loading="saving">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '../api'

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')
const list = ref([]); const majors = ref([]); const majorMap = ref({}); const loading = ref(false); const dialogVisible = ref(false); const saving = ref(false); const isEdit = ref(false)
const formRef = ref(null); const form = reactive({ id: null, className: '', classCode: '', majorId: null, grade: '', description: '' })
const rules = { className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }], classCode: [{ required: true, message: '请输入班级编码', trigger: 'blur' }], majorId: [{ required: true, message: '请选择专业', trigger: 'change' }], grade: [{ required: true, message: '请选择年级', trigger: 'change' }] }

const fetchData = async () => { loading.value = true; const [cRes, mRes] = await Promise.all([api.get('/class'), api.get('/major')]); if (cRes.code === 200) list.value = cRes.data; if (mRes.code === 200) { majors.value = mRes.data; majorMap.value = Object.fromEntries(mRes.data.map(m => [m.id, m.majorName])) }; loading.value = false }
const openDialog = (row) => { isEdit.value = !!row; if (row) Object.assign(form, row); else Object.assign(form, { id: null, className: '', classCode: '', majorId: null, grade: '', description: '' }); dialogVisible.value = true }
const handleSave = async () => { const valid = await formRef.value.validate().catch(() => false); if (!valid) return; saving.value = true; try { if (isEdit.value) await api.put('/class', form); else await api.post('/class', form); ElMessage.success(isEdit.value ? '修改成功' : '新增成功'); dialogVisible.value = false; fetchData() } catch { ElMessage.error('操作失败') } saving.value = false }
const handleDelete = async (id) => { await ElMessageBox.confirm('确定删除该班级吗？', '提示', { type: 'warning' }); await api.delete('/class/' + id); ElMessage.success('删除成功'); fetchData() }
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
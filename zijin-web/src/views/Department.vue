<template>
  <div class="page">
    <div class="page-header">
      <h2>部门管理</h2>
      <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增部门</el-button>
    </div>
    <el-card shadow="hover">
      <el-table :data="list" stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="deptName" label="部门名称" />
        <el-table-column prop="deptCode" label="部门编码" width="120" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑部门' : '新增部门'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="部门名称" prop="deptName"><el-input v-model="form.deptName" placeholder="请输入部门名称" /></el-form-item>
        <el-form-item label="部门编码" prop="deptCode"><el-input v-model="form.deptCode" placeholder="请输入部门编码" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" placeholder="请输入描述" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '../api'

const list = ref([]); const loading = ref(false); const dialogVisible = ref(false); const saving = ref(false); const isEdit = ref(false)
const formRef = ref(null); const form = reactive({ id: null, deptName: '', deptCode: '', description: '' })
const rules = { deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }], deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }] }

const fetchData = async () => { loading.value = true; const res = await api.get('/department'); if (res.code === 200) list.value = res.data; loading.value = false }
const openDialog = (row) => { isEdit.value = !!row; if (row) Object.assign(form, row); else Object.assign(form, { id: null, deptName: '', deptCode: '', description: '' }); dialogVisible.value = true }
const handleSave = async () => { const valid = await formRef.value.validate().catch(() => false); if (!valid) return; saving.value = true; try { if (isEdit.value) await api.put('/department', form); else await api.post('/department', form); ElMessage.success(isEdit.value ? '修改成功' : '新增成功'); dialogVisible.value = false; fetchData() } catch { ElMessage.error('操作失败') } saving.value = false }
const handleDelete = async (id) => { await ElMessageBox.confirm('确定删除该部门吗？', '提示', { type: 'warning' }); await api.delete('/department/' + id); ElMessage.success('删除成功'); fetchData() }
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
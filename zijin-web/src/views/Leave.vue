<template>
  <div class="leave-page">
    <div class="page-header">
      <h2>请假管理</h2>
      <el-button v-if="roleCode==='student'" type="primary" @click="showDialog=true">
        <el-icon><Plus /></el-icon>发起请假
      </el-button>
    </div>

    <div class="status-filter" v-if="list.length">
      <el-radio-group v-model="statusFilter" size="small" @change="fetchList">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="pending">待审批</el-radio-button>
        <el-radio-button label="approved">已通过</el-radio-button>
        <el-radio-button label="rejected">已驳回</el-radio-button>
      </el-radio-group>
    </div>

    <div v-loading="loading">
      <el-empty v-if="list.length===0 && !loading" description="暂无请假记录" />
      <el-card v-for="item in list" :key="item.id" class="leave-card" shadow="hover">
        <div class="leave-card-header">
          <div class="leave-student">
            <el-avatar :size="36" :icon="UserFilled" style="background:#1a237e" />
            <div>
              <div class="student-name">{{ item.studentName }}</div>
              <div class="leave-time">{{ item.createTime }}</div>
            </div>
          </div>
          <el-tag :type="statusTag[item.status]" size="small">{{ statusMap[item.status] }}</el-tag>
        </div>
        <div class="leave-body">
          <div class="leave-info">
            <span class="info-label">请假日期：</span>{{ item.startDate }} 至 {{ item.endDate || item.startDate }}
          </div>
          <div class="leave-info"><span class="info-label">请假原因：</span>{{ item.reason }}</div>
          <div class="leave-info" v-if="item.approverName">
            <span class="info-label">审批人：</span>{{ item.approverName }}
          </div>
        </div>
        <div class="leave-actions" v-if="roleCode==='teacher' && item.status==='pending'">
          <el-button type="success" size="small" @click="handleApprove(item.id, 'approved')">
            <el-icon><Check /></el-icon>通过
          </el-button>
          <el-button type="danger" size="small" @click="handleApprove(item.id, 'rejected')">
            <el-icon><Close /></el-icon>驳回
          </el-button>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="showDialog" title="发起请假" width="450px" :close-on-click-modal="false">
      <el-form :model="form" label-width="80px">
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="form.endDate" type="date" placeholder="同开始日期可不选" style="width:100%" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="请假原因"><el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入请假原因" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog=false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Plus, Check, Close, UserFilled } from '@element-plus/icons-vue'
import api from '../api'
import { ElMessage } from 'element-plus'

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')

const statusMap = { pending: '待审批', approved: '已通过', rejected: '已驳回' }
const statusTag = { pending: 'warning', approved: 'success', rejected: 'danger' }

const list = ref([])
const loading = ref(false)
const showDialog = ref(false)
const submitting = ref(false)
const statusFilter = ref('all')
const form = ref({ startDate: '', endDate: '', reason: '' })

const fetchList = async () => {
  loading.value = true
  try {
    let url = '/leave'
    let params = []
    if (roleCode.value === 'student') params.push('studentId=' + user.id)
    const sep = params.length ? '?' : ''
    const query = params.join('&')
    const res = await api.get(url + sep + query)
    if (res.code === 200) {
      let data = res.data || []
      if (statusFilter.value !== 'all') {
        data = data.filter(x => x.status === statusFilter.value)
      }
      list.value = data
    }
  } catch { /* ignore */ }
  loading.value = false
}

const handleSubmit = async () => {
  if (!form.value.startDate) { ElMessage.warning('请选择日期'); return }
  if (!form.value.reason.trim()) { ElMessage.warning('请填写原因'); return }
  submitting.value = true
  try {
    const res = await api.post('/leave', {
      studentId: user.id, studentName: user.realName || user.username,
      classId: user.classId, reason: form.value.reason.trim(),
      startDate: form.value.startDate,
      endDate: form.value.endDate || form.value.startDate
    })
    if (res.code === 200) {
      ElMessage.success('请假申请已提交')
      showDialog.value = false
      form.value = { startDate: '', endDate: '', reason: '' }
      fetchList()
    } else {
      ElMessage.error(res.msg || '提交失败')
    }
  } catch {
    ElMessage.error('请求失败，请确认后端是否已重启')
  }
  submitting.value = false
}

const handleApprove = async (id, status) => {
  try {
    const res = await api.put('/leave/approve/' + id, {
      status,
      approverName: user.realName || user.username
    })
    if (res.code === 200) {
      ElMessage.success(status === 'approved' ? '已通过' : '已驳回')
      fetchList()
    } else {
      ElMessage.error('操作失败')
    }
  } catch {
    ElMessage.error('请求失败')
  }
}

onMounted(fetchList)
</script>

<style scoped>
.leave-page { max-width: 800px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h2 { margin: 0; font-size: 20px; color: #333; }
.status-filter { margin-bottom: 16px; }
.leave-card { margin-bottom: 12px; }
.leave-card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.leave-student { display: flex; align-items: center; gap: 12px; }
.student-name { font-weight: 600; font-size: 15px; }
.leave-time { font-size: 12px; color: #999; margin-top: 2px; }
.leave-body { padding-left: 48px; }
.leave-info { font-size: 14px; color: #555; margin-bottom: 6px; }
.info-label { color: #999; display: inline-block; width: 80px; }
.leave-actions { padding-left: 48px; margin-top: 10px; display: flex; gap: 8px; }
@media (max-width: 767px) {
  .leave-body { padding-left: 0; }
  .leave-actions { padding-left: 0; }
  .info-label { width: auto; margin-right: 4px; }
  .page-header { flex-direction: column; gap: 10px; align-items: flex-start; }
}
</style>
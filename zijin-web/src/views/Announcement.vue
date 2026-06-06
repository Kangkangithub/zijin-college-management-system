<template>
  <div class="announcement-page">
    <div class="page-header">
      <h2>公告互动</h2>
      <el-button v-if="roleCode==='admin'||roleCode==='teacher'" type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>发布公告
      </el-button>
    </div>

    <div class="announcement-list" v-loading="loading">
      <el-empty v-if="list.length === 0" description="暂无公告" />
      <el-card v-for="item in list" :key="item.id" class="ann-card" shadow="hover">
        <template #header>
          <div class="ann-header">
            <div class="ann-title-row">
              <h3>{{ item.title }}</h3>
              <el-tag size="small" :type="item.publisherId===1?'danger':''">{{ item.publisherName }}</el-tag>
            </div>
            <div class="ann-actions">
              <el-button text type="primary" @click="handleLike(item)">
                <el-icon><StarFilled v-if="item.liked" /><Star v-else /></el-icon>
                {{ item.likeCount || 0 }}
              </el-button>
              <el-button text type="danger" v-if="roleCode==='admin'" @click="handleDelete(item.id)">
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </div>
          </div>
        </template>
        <div class="ann-content">{{ item.content }}</div>
        <div class="ann-time">{{ item.createTime }}</div>

        <el-divider />
        <div class="comment-section">
          <div class="comment-title">评论 ({{ item.comments?.length || 0 }})</div>
          <div v-for="c in item.comments" :key="c.id" class="comment-item">
            <span class="comment-user">{{ c.userName }}：</span>
            <span class="comment-text">{{ c.content }}</span>
            <span class="comment-time">{{ c.createTime }}</span>
          </div>
          <div class="comment-input">
            <el-input v-model="commentText[item.id]" placeholder="写下你的评论..." size="small" @keyup.enter="handleComment(item)">
              <template #append>
                <el-button @click="handleComment(item)" :icon="Position">发送</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="showAddDialog" title="发布公告" width="500px" :close-on-click-modal="false">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" placeholder="请输入公告标题" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入公告内容" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="submitting">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Plus, Star, StarFilled, Delete, Position } from '@element-plus/icons-vue'
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const user = JSON.parse(localStorage.getItem('zijin_user') || '{}')
const roleIdMap = { 1: 'admin', 2: 'teacher', 3: 'student' }
const roleCode = computed(() => roleIdMap[user.roleId] || 'student')

const list = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const submitting = ref(false)
const commentText = ref({})
const form = ref({ title: '', content: '' })

const fetchList = async () => {
  loading.value = true
  try {
    const res = await api.get('/announcement')
    if (res.code === 200) list.value = res.data || []
  } catch { /* ignore */ }
  loading.value = false
}

const handleAdd = async () => {
  if (!form.value.title || !form.value.content) { ElMessage.warning('请填写完整信息'); return }
  submitting.value = true
  try {
    const res = await api.post('/announcement', {
      title: form.value.title, content: form.value.content,
      publisherId: user.id, publisherName: user.realName || user.username
    })
    if (res.code === 200) {
      ElMessage.success('发布成功')
      showAddDialog.value = false
      form.value = { title: '', content: '' }
      fetchList()
    } else {
      ElMessage.error(res.msg || '发布失败')
    }
  } catch {
    ElMessage.error('请求失败，请确认后端是否已重启')
  }
  submitting.value = false
}

const handleLike = async (item) => {
  try {
    const res = await api.put('/announcement/like/' + item.id)
    if (res.code === 200) { item.likeCount++; item.liked = true; ElMessage.success('点赞成功') }
  } catch { ElMessage.error('操作失败') }
}

const handleComment = async (item) => {
  const text = commentText.value[item.id]
  if (!text?.trim()) return
  try {
    const res = await api.post('/announcement/comment', {
      announcementId: item.id, userId: user.id,
      userName: user.realName || user.username, content: text.trim()
    })
    if (res.code === 200) { commentText.value[item.id] = ''; fetchList(); ElMessage.success('评论成功') }
  } catch { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除该公告及所有评论？', '提示', { type: 'warning' })
  try {
    const res = await api.delete('/announcement/' + id)
    if (res.code === 200) { ElMessage.success('已删除'); fetchList() }
    else { ElMessage.error('删除失败') }
  } catch { ElMessage.error('操作失败') }
}

onMounted(fetchList)
</script>

<style scoped>
.announcement-page { max-width: 800px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { margin: 0; font-size: 20px; color: #333; }
.ann-card { margin-bottom: 16px; }
.ann-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 8px; }
.ann-title-row { display: flex; align-items: center; gap: 10px; flex: 1; }
.ann-title-row h3 { margin: 0; font-size: 16px; }
.ann-actions { display: flex; align-items: center; gap: 8px; }
.ann-content { color: #555; line-height: 1.8; white-space: pre-wrap; padding: 8px 0; }
.ann-time { color: #999; font-size: 12px; margin-top: 8px; }
.comment-section { margin-top: 4px; }
.comment-title { font-size: 14px; font-weight: 600; color: #666; margin-bottom: 8px; }
.comment-item { padding: 6px 0; font-size: 14px; display: flex; flex-wrap: wrap; align-items: baseline; gap: 4px; }
.comment-user { color: #1a237e; font-weight: 500; }
.comment-text { color: #333; flex: 1; }
.comment-time { color: #bbb; font-size: 12px; }
.comment-input { margin-top: 10px; }
@media (max-width: 767px) {
  .ann-title-row { flex-direction: column; align-items: flex-start; gap: 4px; }
  .ann-title-row h3 { font-size: 15px; }
  .page-header { flex-direction: column; gap: 10px; align-items: flex-start; }
}
</style>
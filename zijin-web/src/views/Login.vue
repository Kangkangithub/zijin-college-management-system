<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>紫金学院信息管理系统</h1>
        <p>Nanjing University of Science & Technology Zijin College</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="pwd">
          <el-input v-model="form.pwd" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password
            @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登 录</el-button>
        </el-form-item>
        <div class="login-footer">
          还没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
        </div>
      </el-form>
      <div class="test-accounts">
        <el-divider>测试账号</el-divider>
        <p>管理员: admin / 123456</p>
        <p>教师: teacher1 / 123456</p>
        <p>学生: student1 / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import api from '../api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', pwd: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await api.post('/login', form)
    if (res.code === 200) {
      localStorage.setItem('zijin_user', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/')
    } else if (res.msg === 'NOT_EXIST') {
      ElMessage.warning('用户不存在，请先注册')
      router.push('/register')
    } else {
      ElMessage.error(res.msg)
    }
  } catch {
    ElMessage.error('登录失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a237e 0%, #4a148c 50%, #1a237e 100%);
  padding: 20px;
}
.login-card {
  width: 420px;
  max-width: 100%;
  background: rgba(255,255,255,0.95);
  border-radius: 16px;
  padding: 40px 36px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-header {
  text-align: center;
  margin-bottom: 32px;
}
.login-header h1 {
  font-size: 24px;
  color: #1a237e;
  margin: 0 0 8px 0;
}
.login-header p {
  font-size: 12px;
  color: #999;
  margin: 0;
}
.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  background: linear-gradient(135deg, #1a237e, #4a148c);
  border: none;
}
.login-footer {
  text-align: center;
  font-size: 14px;
  color: #666;
}
.test-accounts {
  margin-top: 16px;
  font-size: 12px;
  color: #999;
  text-align: center;
}
.test-accounts p {
  margin: 4px 0;
}

@media (max-width: 480px) {
  .login-card { padding: 28px 20px; border-radius: 12px; }
  .login-header h1 { font-size: 20px; }
}
</style>
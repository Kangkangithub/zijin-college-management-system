<template>
  <div class="register-container">
    <div class="register-card">
      <h1>用户注册</h1>
      <p class="subtitle">紫金学院信息管理系统</p>
      <el-form :model="form" :rules="rules" ref="formRef" size="large" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input v-model="form.pwd" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input v-model="form.confirmPwd" type="password" placeholder="请再次输入密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-select v-model="form.grade" placeholder="请选择年级" style="width:100%">
            <el-option label="2021级" value="2021级" />
            <el-option label="2022级" value="2022级" />
            <el-option label="2023级" value="2023级" />
            <el-option label="2024级" value="2024级" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业" prop="majorId">
          <el-select v-model="form.majorId" placeholder="请选择专业" style="width:100%">
            <el-option v-for="m in majors" :key="m.id" :label="m.majorName" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="classId">
          <el-select v-model="form.classId" placeholder="请选择班级" style="width:100%">
            <el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="reg-btn" @click="handleRegister" :loading="loading">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="reg-footer">
        已有账号？<el-link type="primary" @click="$router.push('/login')">返回登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '../api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const majors = ref([])
const classes = ref([])
const form = reactive({
  username: '', pwd: '', confirmPwd: '', realName: '', grade: '',
  majorId: null, classId: null, phone: '', email: ''
})
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPwd: [{ required: true, message: '请确认密码', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
}

onMounted(async () => {
  const [mr, cr] = await Promise.all([api.get('/major'), api.get('/class')])
  if (mr.code === 200) majors.value = mr.data
  if (cr.code === 200) classes.value = cr.data
})

const handleRegister = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (form.pwd !== form.confirmPwd) { ElMessage.error('两次密码不一致'); return }
  loading.value = true
  try {
    const res = await api.post('/register', form)
    if (res.code === 200) { ElMessage.success('注册成功，请登录'); router.push('/login') }
    else { ElMessage.error(res.msg) }
  } catch { ElMessage.error('注册失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #1a237e 0%, #4a148c 50%, #1a237e 100%);
  padding: 20px;
}
.register-card {
  width: 500px; max-width: 100%; background: rgba(255,255,255,0.95);
  border-radius: 16px; padding: 36px 32px; box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.register-card h1 { text-align: center; color: #1a237e; font-size: 22px; margin: 0 0 4px 0; }
.subtitle { text-align: center; color: #999; font-size: 13px; margin-bottom: 24px; }
.reg-btn { width: 100%; height: 44px; background: linear-gradient(135deg, #1a237e, #4a148c); border: none; }
.reg-footer { text-align: center; font-size: 14px; color: #666; margin-top: 8px; }
@media (max-width: 480px) { .register-card { padding: 24px 16px; border-radius: 12px; } }
</style>
<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>{{ isAdminMode ? 'Admin Login' : 'User Login' }}</h2>
        </div>
      </template>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="Account" prop="account">
          <el-input
            v-model="loginForm.account"
            placeholder="Please enter your account"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Please enter your password"
            show-password
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            Login
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <div class="links">
            <el-link type="primary" @click="toggleAdminMode">
              {{ isAdminMode ? 'User Login' : 'Admin Login' }}
            </el-link>
            <el-link type="primary" @click="goToRegister">
              Register New Account
            </el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const isAdminMode = ref(false)

const loginForm = reactive({
  account: '',
  password: ''
})

const rules: FormRules = {
  account: [
    { required: true, message: 'Please enter your account', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isAdminMode.value) {
          await authStore.adminLogin(loginForm)
          router.push('/admin')
        } else {
          await authStore.login(loginForm)
          router.push('/forum')
        }
      } catch (error) {
        console.error('Login error:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const toggleAdminMode = () => {
  isAdminMode.value = !isAdminMode.value
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
}

.card-header h2 {
  text-align: center;
  margin: 0;
  color: #303133;
}

.links {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>

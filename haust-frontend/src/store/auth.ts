import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/api'
import type { AccountDTO } from '@/types'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const role = ref<number>(parseInt(localStorage.getItem('role') || '0'))
  const isLoggedIn = ref<boolean>(!!token.value)

  // Login
  const login = async (data: AccountDTO) => {
    try {
      const response = await authApi.login(data)
      token.value = response.token
      role.value = response.role
      isLoggedIn.value = true
      
      localStorage.setItem('token', response.token)
      localStorage.setItem('role', response.role.toString())
      
      ElMessage.success('Login successful')
      return response
    } catch (error) {
      ElMessage.error('Login failed')
      throw error
    }
  }

  // Admin login
  const adminLogin = async (data: AccountDTO) => {
    try {
      const response = await authApi.adminLogin(data)
      token.value = response.token
      role.value = response.role
      isLoggedIn.value = true
      
      localStorage.setItem('token', response.token)
      localStorage.setItem('role', response.role.toString())
      
      ElMessage.success('Admin login successful')
      return response
    } catch (error) {
      ElMessage.error('Admin login failed')
      throw error
    }
  }

  // Register
  const register = async (data: AccountDTO) => {
    try {
      await authApi.register(data)
      ElMessage.success('Registration successful, please login')
    } catch (error) {
      ElMessage.error('Registration failed')
      throw error
    }
  }

  // Logout
  const logout = () => {
    token.value = ''
    role.value = 0
    isLoggedIn.value = false
    
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    
    ElMessage.success('Logout successful')
  }

  // Check if user is admin
  const isAdmin = () => {
    return role.value === 1
  }

  return {
    token,
    role,
    isLoggedIn,
    login,
    adminLogin,
    register,
    logout,
    isAdmin
  }
})

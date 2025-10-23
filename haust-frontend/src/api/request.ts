import axios, { type AxiosInstance, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// Create axios instance
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// Request interceptor
service.interceptors.request.use(
  (config: any) => {
    // Add token to headers
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    
    // If response is successful
    if (response.status === 200) {
      return res
    }
    
    // Handle error response
    ElMessage.error(res.msg || 'Request failed')
    return Promise.reject(new Error(res.msg || 'Error'))
  },
  (error) => {
    console.error('Response error:', error)
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('Unauthorized, please login again')
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('Access denied')
          break
        case 404:
          ElMessage.error('Request not found')
          break
        case 500:
          ElMessage.error('Server error')
          break
        default:
          ElMessage.error(error.response.data.msg || 'Request failed')
      }
    } else {
      ElMessage.error('Network error')
    }
    
    return Promise.reject(error)
  }
)

export default service

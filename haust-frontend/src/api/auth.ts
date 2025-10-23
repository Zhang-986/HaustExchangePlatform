import request from './request'
import type { AccountDTO, RoleVo } from '@/types'

// User authentication APIs
export const authApi = {
  // User registration
  register: (data: AccountDTO): Promise<void> => {
    return request({
      url: '/user/register',
      method: 'post',
      data
    })
  },

  // User login
  login: async (data: AccountDTO): Promise<RoleVo> => {
    // 使用相对路径，开发时走 Vite proxy（VITE_API_BASE_URL=/api）
    const res = await request.post('/user/login', data)
    // 假设后端返回结构为 { token, role, ... }
    return res.data as RoleVo
  },

  // Admin login
  adminLogin: (data: AccountDTO): Promise<RoleVo> => {
    return request({
      url: '/admin/login',
      method: 'post',
      data
    })
  },

  // AI Q&A
  askQuestion: (text: string): Promise<string> => {
    return request({
      url: '/user/info',
      method: 'get',
      params: { text }
    })
  }
}

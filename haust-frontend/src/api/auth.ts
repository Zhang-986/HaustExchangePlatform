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
  login: (data: AccountDTO): Promise<RoleVo> => {
    return request({
      url: '/user/login',
      method: 'post',
      data
    })
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

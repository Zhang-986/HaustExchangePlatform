import request from './request'
import type { CodingSharingDTO, CodingSharing, CodingSharingVo, PageDTO, PageVO } from '@/types'

// Referral APIs
export const referralApi = {
  // Submit referral info
  addInfo: (data: CodingSharingDTO): Promise<void> => {
    return request({
      url: '/user/addInfo',
      method: 'post',
      data
    })
  },

  // Update referral info
  modifyInfo: (data: CodingSharingDTO): Promise<void> => {
    return request({
      url: '/user/modify',
      method: 'put',
      data
    })
  },

  // Get referrals with pagination
  getPage: (params: PageDTO): Promise<PageVO<CodingSharingVo>> => {
    return request({
      url: '/user/page',
      method: 'get',
      params
    })
  },

  // Delete referral
  deleteInfo: (id: number): Promise<void> => {
    return request({
      url: `/user/delete/${id}`,
      method: 'delete'
    })
  },

  // Get my referrals
  getMyInfo: (params: PageDTO): Promise<PageVO<CodingSharing>> => {
    return request({
      url: '/user/myInfo',
      method: 'get',
      params
    })
  },

  // Admin: Get referrals for approval
  adminGetPage: (params: PageDTO): Promise<PageVO<CodingSharingVo>> => {
    return request({
      url: '/admin/page',
      method: 'get',
      params
    })
  },

  // Admin: Approve/reject referral
  adminPermit: (id: number, status: number): Promise<void> => {
    return request({
      url: '/admin/permit',
      method: 'put',
      params: { id, status }
    })
  }
}

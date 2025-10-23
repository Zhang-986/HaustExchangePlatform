// User related types
export interface AccountDTO {
  account: string
  password: string
}

export interface RoleVo {
  token: string
  role: number
}

// Post related types
export interface CreatePostDTO {
  title: string
  description?: string
  anonymity?: boolean
}

export interface Post {
  id: number
  title: string
  description: string
  anonymity: boolean
  userId: number
  username?: string
  createTime: string
  updateTime: string
  likedTimes: number
  isLiked?: boolean
}

export interface PostVO extends Post {
  replies?: ReplyVO[]
}

// Reply related types
export interface CreateReplyDTO {
  postId: number
  content: string
  parentId?: number
}

export interface ReplyVO {
  id: number
  postId: number
  userId: number
  username: string
  content: string
  parentId?: number
  createTime: string
  likedTimes: number
  isLiked?: boolean
  children?: ReplyVO[]
}

export interface HotReplyVo extends ReplyVO {
  hotScore: number
}

// Referral related types
export interface CodingSharingDTO {
  id?: number
  companyName: string
  detail: string
  remark?: string
  recommanderEmail: string
  recommandIndex: number
  codeId?: string
}

export interface CodingSharing extends CodingSharingDTO {
  id: number
  userId: number
  status: number
  createTime: string
  updateTime: string
}

export interface CodingSharingVo extends CodingSharing {
  username?: string
}

// Pagination types
export interface PageDTO {
  pageNum: number
  pageSize: number
}

export interface PageVO<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
  pages: number
}

// Chat types
export type MessageType = 'CHAT' | 'JOIN' | 'LEAVE' | 'PRIVATE'

export interface ChatMessage {
  type: MessageType
  sender: string
  receiver?: string
  content: string
  timestamp?: string
}

// API Response types
export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

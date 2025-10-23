import request from './request'
import type { CreatePostDTO, Post, PostVO, PageDTO, PageVO, CreateReplyDTO, ReplyVO, HotReplyVo } from '@/types'

// Forum post APIs
export const postApi = {
  // Create post
  createPost: (data: CreatePostDTO): Promise<void> => {
    return request({
      url: '/user/posts',
      method: 'post',
      data
    })
  },

  // Update post
  updatePost: (id: number, data: CreatePostDTO): Promise<void> => {
    return request({
      url: `/user/posts/${id}`,
      method: 'put',
      data
    })
  },

  // Get posts with pagination
  getPosts: (params: PageDTO): Promise<PageVO<Post>> => {
    return request({
      url: '/user/posts',
      method: 'get',
      params
    })
  },

  // Delete post
  deletePost: (id: number): Promise<void> => {
    return request({
      url: `/user/posts/${id}`,
      method: 'delete'
    })
  },

  // Like/unlike post
  likePost: (id: number, flag: number): Promise<number> => {
    return request({
      url: `/user/posts/${id}/like`,
      method: 'post',
      params: { flag }
    })
  },

  // Get post details
  getPostDetail: (id: number): Promise<PostVO> => {
    return request({
      url: `/user/posts/${id}`,
      method: 'get'
    })
  },

  // Get my posts
  getMyPosts: (params: PageDTO): Promise<PageVO<Post>> => {
    return request({
      url: '/user/posts/myPost',
      method: 'get',
      params
    })
  }
}

// Forum reply APIs
export const replyApi = {
  // Add reply
  addReply: (data: CreateReplyDTO): Promise<void> => {
    return request({
      url: '/user/replies',
      method: 'post',
      data
    })
  },

  // Get replies
  getReplies: (postId: number, params: PageDTO): Promise<PageVO<ReplyVO>> => {
    return request({
      url: '/user/replies',
      method: 'get',
      params: { postId, ...params }
    })
  },

  // Like/unlike reply
  likeReply: (id: number, flag: number): Promise<number> => {
    return request({
      url: `/user/replies/${id}/like`,
      method: 'post',
      params: { flag }
    })
  },

  // Get hot replies
  getHotReplies: (postId: number): Promise<HotReplyVo[]> => {
    return request({
      url: `/user/replies/hot/${postId}`,
      method: 'get'
    })
  },

  // Admin delete reply
  adminDeleteReply: (id: number): Promise<void> => {
    return request({
      url: `/admin/replies/${id}`,
      method: 'delete'
    })
  }
}

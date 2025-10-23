import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/store'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/forum'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/forum',
    name: 'Forum',
    component: () => import('@/views/forum/ForumList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/forum/:id',
    name: 'PostDetail',
    component: () => import('@/views/forum/PostDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/forum/create',
    name: 'CreatePost',
    component: () => import('@/views/forum/CreatePost.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/forum/my-posts',
    name: 'MyPosts',
    component: () => import('@/views/forum/MyPosts.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/referral',
    name: 'Referral',
    component: () => import('@/views/referral/ReferralList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/referral/create',
    name: 'CreateReferral',
    component: () => import('@/views/referral/CreateReferral.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/referral/my-referrals',
    name: 'MyReferrals',
    component: () => import('@/views/referral/MyReferrals.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/chat',
    name: 'Chat',
    component: () => import('@/views/chat/ChatRoom.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/AdminDashboard.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  
  // Check authentication
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')
    return
  }
  
  // Check admin permission
  if (to.meta.requiresAdmin && !authStore.isAdmin()) {
    next('/forum')
    return
  }
  
  next()
})

export default router

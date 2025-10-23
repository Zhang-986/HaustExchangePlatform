<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="logo">HAUST Exchange Platform</div>
        <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          class="menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/forum">
            <el-icon><ChatDotRound /></el-icon>
            <span>Forum</span>
          </el-menu-item>
          <el-menu-item index="/referral">
            <el-icon><Document /></el-icon>
            <span>Referrals</span>
          </el-menu-item>
          <el-menu-item index="/chat">
            <el-icon><Message /></el-icon>
            <span>Chat</span>
          </el-menu-item>
          <el-menu-item v-if="authStore.isAdmin()" index="/admin">
            <el-icon><Setting /></el-icon>
            <span>Admin</span>
          </el-menu-item>
        </el-menu>
        <div class="user-actions">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-icon><User /></el-icon>
              <span>My Account</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="myPosts">My Posts</el-dropdown-item>
                <el-dropdown-item command="myReferrals">My Referrals</el-dropdown-item>
                <el-dropdown-item divided command="logout">Logout</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store'
import { ChatDotRound, Document, Message, Setting, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleCommand = (command: string) => {
  switch (command) {
    case 'myPosts':
      router.push('/forum/my-posts')
      break
    case 'myReferrals':
      router.push('/referral/my-referrals')
      break
    case 'logout':
      authStore.logout()
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.layout {
  height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,.08);
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.menu {
  flex: 1;
  margin: 0 40px;
  border-bottom: none;
}

.user-actions {
  cursor: pointer;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.el-dropdown-link:hover {
  color: #409EFF;
}

.main {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>

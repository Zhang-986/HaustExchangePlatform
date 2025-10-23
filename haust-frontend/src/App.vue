<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import MainLayout from '@/components/layout/MainLayout.vue'

const route = useRoute()

// Routes that don't need the main layout
const publicRoutes = ['/login', '/register']
const isPublicRoute = computed(() => publicRoutes.includes(route.path))
</script>

<template>
  <div id="app">
    <MainLayout v-if="!isPublicRoute">
      <router-view />
    </MainLayout>
    <router-view v-else />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>

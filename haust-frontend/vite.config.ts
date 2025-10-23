import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      // 修复 sockjs-client 相关路径问题
      'sockjs-client': path.resolve(__dirname, 'node_modules/sockjs-client/dist/sockjs.min.js')
    }
  },
  server: {
    port: 5173,
    hmr: {
      // 强制使用 WebSocket，避免 sockjs 问题
      protocol: 'ws',
      host: 'localhost',
      port: 5173
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  define: {
    global: 'window',
    // 修复 CommonJS 模块导出问题
    'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV)
  },
  optimizeDeps: {
    include: ['sockjs-client'], // 改为包含而不是排除
    esbuildOptions: {
      // 为依赖提供必要的定义
      define: {
        global: 'globalThis'
      }
    }
  }
})
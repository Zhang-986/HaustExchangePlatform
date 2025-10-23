<template>
  <div class="my-posts">
    <el-card>
      <template #header>
        <h2>My Posts</h2>
      </template>
      
      <el-table
        v-loading="loading"
        :data="posts"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="Title" min-width="200" />
        <el-table-column prop="description" label="Description" min-width="250" show-overflow-tooltip />
        <el-table-column prop="likedTimes" label="Likes" width="100" align="center" />
        <el-table-column prop="createTime" label="Created" width="180" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row.id)">
              View
            </el-button>
            <el-button type="warning" link @click="editPost(row.id)">
              Edit
            </el-button>
            <el-button type="danger" link @click="deletePost(row.id)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadPosts"
        @current-change="loadPosts"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { postApi } from '@/api'
import type { Post } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const posts = ref<Post[]>([])
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadPosts = async () => {
  loading.value = true
  try {
    const response = await postApi.getMyPosts({
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize
    })
    posts.value = response.records
    pagination.value.total = response.total
  } catch (error) {
    console.error('Error loading posts:', error)
    ElMessage.error('Failed to load posts')
  } finally {
    loading.value = false
  }
}

const viewDetail = (id: number) => {
  router.push(`/forum/${id}`)
}

const editPost = (_id: number) => {
  // Navigate to edit page (to be implemented)
  ElMessage.info('Edit functionality coming soon')
}

const deletePost = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete this post?',
      'Warning',
      {
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    await postApi.deletePost(id)
    ElMessage.success('Post deleted successfully')
    loadPosts()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error deleting post:', error)
      ElMessage.error('Failed to delete post')
    }
  }
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.my-posts {
  max-width: 1200px;
  margin: 0 auto;
}
</style>

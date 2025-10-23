<template>
  <div class="forum-list">
    <el-card>
      <template #header>
        <div class="header">
          <h2>Forum Posts</h2>
          <el-button type="primary" @click="goToCreate">
            <el-icon><Plus /></el-icon>
            Create Post
          </el-button>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="posts"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="Title" min-width="200" />
        <el-table-column prop="description" label="Description" min-width="250" show-overflow-tooltip />
        <el-table-column label="Author" width="120">
          <template #default="{ row }">
            {{ row.anonymity ? 'Anonymous' : row.username || 'Unknown' }}
          </template>
        </el-table-column>
        <el-table-column prop="likedTimes" label="Likes" width="100" align="center" />
        <el-table-column prop="createTime" label="Created" width="180" />
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row.id)">
              View
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleLike(row)"
            >
              {{ row.isLiked ? 'Unlike' : 'Like' }}
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
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
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
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

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
    const response = await postApi.getPosts({
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

const handleSizeChange = () => {
  loadPosts()
}

const handleCurrentChange = () => {
  loadPosts()
}

const goToCreate = () => {
  router.push('/forum/create')
}

const viewDetail = (id: number) => {
  router.push(`/forum/${id}`)
}

const handleLike = async (post: Post) => {
  try {
    const flag = post.isLiked ? 0 : 1
    const likedTimes = await postApi.likePost(post.id, flag)
    post.likedTimes = likedTimes
    post.isLiked = !post.isLiked
    ElMessage.success(post.isLiked ? 'Liked' : 'Unliked')
  } catch (error) {
    console.error('Error liking post:', error)
    ElMessage.error('Failed to like post')
  }
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.forum-list {
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  margin: 0;
}
</style>

<template>
  <div class="post-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="header">
          <h2>{{ post.title }}</h2>
          <div class="actions">
            <el-button type="danger" @click="handleLike">
              {{ post.isLiked ? 'Unlike' : 'Like' }} ({{ post.likedTimes }})
            </el-button>
          </div>
        </div>
      </template>
      
      <div class="post-content">
        <p>{{ post.description }}</p>
        <div class="post-meta">
          <span>Author: {{ post.anonymity ? 'Anonymous' : post.username || 'Unknown' }}</span>
          <span>Created: {{ post.createTime }}</span>
        </div>
      </div>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>
        <h3>Replies ({{ pagination.total }})</h3>
      </template>
      
      <div class="reply-form">
        <el-input
          v-model="replyContent"
          type="textarea"
          :rows="3"
          placeholder="Write a reply..."
          maxlength="500"
          show-word-limit
        />
        <el-button
          type="primary"
          :loading="submitting"
          style="margin-top: 10px;"
          @click="handleSubmitReply"
        >
          Submit Reply
        </el-button>
      </div>
      
      <el-divider />
      
      <div v-if="replies.length > 0" class="replies-list">
        <div
          v-for="reply in replies"
          :key="reply.id"
          class="reply-item"
        >
          <div class="reply-header">
            <span class="username">{{ reply.username }}</span>
            <span class="time">{{ reply.createTime }}</span>
          </div>
          <div class="reply-content">{{ reply.content }}</div>
          <div class="reply-actions">
            <el-button type="text" @click="handleLikeReply(reply)">
              {{ reply.isLiked ? 'Unlike' : 'Like' }} ({{ reply.likedTimes }})
            </el-button>
          </div>
        </div>
      </div>
      <el-empty v-else description="No replies yet" />
      
      <el-pagination
        v-if="pagination.total > 0"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadReplies"
        @current-change="loadReplies"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { postApi, replyApi } from '@/api'
import type { PostVO, ReplyVO } from '@/types'
import { ElMessage } from 'element-plus'

const route = useRoute()
const postId = parseInt(route.params.id as string)

const loading = ref(false)
const submitting = ref(false)
const post = ref<PostVO>({} as PostVO)
const replies = ref<ReplyVO[]>([])
const replyContent = ref('')
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadPost = async () => {
  loading.value = true
  try {
    post.value = await postApi.getPostDetail(postId)
  } catch (error) {
    console.error('Error loading post:', error)
    ElMessage.error('Failed to load post')
  } finally {
    loading.value = false
  }
}

const loadReplies = async () => {
  try {
    const response = await replyApi.getReplies(postId, {
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize
    })
    replies.value = response.records
    pagination.value.total = response.total
  } catch (error) {
    console.error('Error loading replies:', error)
    ElMessage.error('Failed to load replies')
  }
}

const handleLike = async () => {
  try {
    const flag = post.value.isLiked ? 0 : 1
    const likedTimes = await postApi.likePost(postId, flag)
    post.value.likedTimes = likedTimes
    post.value.isLiked = !post.value.isLiked
    ElMessage.success(post.value.isLiked ? 'Liked' : 'Unliked')
  } catch (error) {
    console.error('Error liking post:', error)
    ElMessage.error('Failed to like post')
  }
}

const handleSubmitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('Please enter reply content')
    return
  }
  
  submitting.value = true
  try {
    await replyApi.addReply({
      postId,
      content: replyContent.value
    })
    replyContent.value = ''
    ElMessage.success('Reply submitted successfully')
    loadReplies()
  } catch (error) {
    console.error('Error submitting reply:', error)
    ElMessage.error('Failed to submit reply')
  } finally {
    submitting.value = false
  }
}

const handleLikeReply = async (reply: ReplyVO) => {
  try {
    const flag = reply.isLiked ? 0 : 1
    const likedTimes = await replyApi.likeReply(reply.id, flag)
    reply.likedTimes = likedTimes
    reply.isLiked = !reply.isLiked
    ElMessage.success(reply.isLiked ? 'Liked' : 'Unliked')
  } catch (error) {
    console.error('Error liking reply:', error)
    ElMessage.error('Failed to like reply')
  }
}

onMounted(() => {
  loadPost()
  loadReplies()
})
</script>

<style scoped>
.post-detail {
  max-width: 1000px;
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

.post-content {
  padding: 20px 0;
}

.post-content p {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 20px;
}

.post-meta {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 14px;
}

.reply-form {
  margin-bottom: 20px;
}

.replies-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.reply-item {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.username {
  font-weight: bold;
  color: #303133;
}

.time {
  color: #909399;
  font-size: 14px;
}

.reply-content {
  margin-bottom: 10px;
  line-height: 1.6;
}

.reply-actions {
  display: flex;
  gap: 10px;
}
</style>

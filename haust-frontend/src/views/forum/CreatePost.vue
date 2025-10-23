<template>
  <div class="create-post">
    <el-card>
      <template #header>
        <h2>Create New Post</h2>
      </template>
      
      <el-form
        ref="postFormRef"
        :model="postForm"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="Title" prop="title">
          <el-input
            v-model="postForm.title"
            placeholder="Enter post title"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="Description" prop="description">
          <el-input
            v-model="postForm.description"
            type="textarea"
            :rows="10"
            placeholder="Enter post content"
            maxlength="2048"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="Anonymous">
          <el-switch v-model="postForm.anonymity" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            Submit
          </el-button>
          <el-button @click="handleCancel">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { postApi } from '@/api'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'

const router = useRouter()

const postFormRef = ref<FormInstance>()
const loading = ref(false)

const postForm = reactive({
  title: '',
  description: '',
  anonymity: true
})

const rules: FormRules = {
  title: [
    { required: true, message: 'Please enter post title', trigger: 'blur' },
    { max: 255, message: 'Title cannot exceed 255 characters', trigger: 'blur' }
  ],
  description: [
    { max: 2048, message: 'Description cannot exceed 2048 characters', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!postFormRef.value) return
  
  await postFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await postApi.createPost(postForm)
        ElMessage.success('Post created successfully')
        router.push('/forum')
      } catch (error) {
        console.error('Error creating post:', error)
        ElMessage.error('Failed to create post')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleCancel = () => {
  router.back()
}
</script>

<style scoped>
.create-post {
  max-width: 800px;
  margin: 0 auto;
}
</style>

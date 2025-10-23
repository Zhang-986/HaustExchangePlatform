<template>
  <div class="create-referral">
    <el-card>
      <template #header>
        <h2>Submit Job Referral</h2>
      </template>
      
      <el-form
        ref="referralFormRef"
        :model="referralForm"
        :rules="rules"
        label-width="150px"
      >
        <el-form-item label="Company Name" prop="companyName">
          <el-input
            v-model="referralForm.companyName"
            placeholder="Enter company name"
          />
        </el-form-item>
        
        <el-form-item label="Details" prop="detail">
          <el-input
            v-model="referralForm.detail"
            type="textarea"
            :rows="5"
            placeholder="Enter job details and requirements"
          />
        </el-form-item>
        
        <el-form-item label="Remark" prop="remark">
          <el-input
            v-model="referralForm.remark"
            type="textarea"
            :rows="3"
            placeholder="Any additional remarks"
          />
        </el-form-item>
        
        <el-form-item label="Contact Email" prop="recommanderEmail">
          <el-input
            v-model="referralForm.recommanderEmail"
            type="email"
            placeholder="Enter contact email"
          />
        </el-form-item>
        
        <el-form-item label="Rating" prop="recommandIndex">
          <el-rate v-model="referralForm.recommandIndex" />
        </el-form-item>
        
        <el-form-item label="Referral Code" prop="codeId">
          <el-input
            v-model="referralForm.codeId"
            placeholder="Enter referral code (if any)"
          />
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
import { referralApi } from '@/api'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'

const router = useRouter()

const referralFormRef = ref<FormInstance>()
const loading = ref(false)

const referralForm = reactive({
  companyName: '',
  detail: '',
  remark: '',
  recommanderEmail: '',
  recommandIndex: 3,
  codeId: ''
})

const rules: FormRules = {
  companyName: [
    { required: true, message: 'Please enter company name', trigger: 'blur' }
  ],
  detail: [
    { required: true, message: 'Please enter job details', trigger: 'blur' }
  ],
  recommanderEmail: [
    { required: true, message: 'Please enter contact email', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email', trigger: 'blur' }
  ],
  recommandIndex: [
    { required: true, message: 'Please rate the referral', trigger: 'change' }
  ]
}

const handleSubmit = async () => {
  if (!referralFormRef.value) return
  
  await referralFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await referralApi.addInfo(referralForm)
        ElMessage.success('Referral submitted successfully')
        router.push('/referral')
      } catch (error) {
        console.error('Error submitting referral:', error)
        ElMessage.error('Failed to submit referral')
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
.create-referral {
  max-width: 800px;
  margin: 0 auto;
}
</style>

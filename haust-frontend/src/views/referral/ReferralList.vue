<template>
  <div class="referral-list">
    <el-card>
      <template #header>
        <div class="header">
          <h2>Job Referrals</h2>
          <el-button type="primary" @click="goToCreate">
            <el-icon><Plus /></el-icon>
            Submit Referral
          </el-button>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="referrals"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="companyName" label="Company" min-width="150" />
        <el-table-column prop="detail" label="Details" min-width="200" show-overflow-tooltip />
        <el-table-column prop="remark" label="Remark" min-width="150" show-overflow-tooltip />
        <el-table-column prop="recommanderEmail" label="Email" min-width="180" />
        <el-table-column label="Rating" width="120" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.recommandIndex" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="codeId" label="Code" width="120" />
        <el-table-column prop="createTime" label="Created" width="180" />
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadReferrals"
        @current-change="loadReferrals"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { referralApi } from '@/api'
import type { CodingSharingVo } from '@/types'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const referrals = ref<CodingSharingVo[]>([])
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadReferrals = async () => {
  loading.value = true
  try {
    const response = await referralApi.getPage({
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize
    })
    referrals.value = response.records
    pagination.value.total = response.total
  } catch (error) {
    console.error('Error loading referrals:', error)
    ElMessage.error('Failed to load referrals')
  } finally {
    loading.value = false
  }
}

const goToCreate = () => {
  router.push('/referral/create')
}

onMounted(() => {
  loadReferrals()
})
</script>

<style scoped>
.referral-list {
  max-width: 1400px;
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

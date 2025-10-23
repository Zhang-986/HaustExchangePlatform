<template>
  <div class="admin-dashboard">
    <el-card>
      <template #header>
        <h2>Admin Dashboard</h2>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Pending Referrals" name="referrals">
          <el-table
            v-loading="loading"
            :data="pendingReferrals"
            style="width: 100%"
          >
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="companyName" label="Company" min-width="150" />
            <el-table-column prop="detail" label="Details" min-width="200" show-overflow-tooltip />
            <el-table-column prop="recommanderEmail" label="Email" min-width="180" />
            <el-table-column label="Rating" width="120" align="center">
              <template #default="{ row }">
                <el-rate v-model="row.recommandIndex" disabled />
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="Submitted" width="180" />
            <el-table-column label="Actions" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="success" @click="approveReferral(row.id)">
                  Approve
                </el-button>
                <el-button type="danger" @click="rejectReferral(row.id)">
                  Reject
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            v-model:current-page="referralPagination.pageNum"
            v-model:page-size="referralPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="referralPagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="loadPendingReferrals"
            @current-change="loadPendingReferrals"
            style="margin-top: 20px; justify-content: center;"
          />
        </el-tab-pane>
        
        <el-tab-pane label="Statistics" name="stats">
          <div class="stats-container">
            <el-card class="stat-card">
              <div class="stat-number">{{ stats.totalPosts }}</div>
              <div class="stat-label">Total Posts</div>
            </el-card>
            <el-card class="stat-card">
              <div class="stat-number">{{ stats.totalReferrals }}</div>
              <div class="stat-label">Total Referrals</div>
            </el-card>
            <el-card class="stat-card">
              <div class="stat-number">{{ stats.pendingReferrals }}</div>
              <div class="stat-label">Pending Referrals</div>
            </el-card>
            <el-card class="stat-card">
              <div class="stat-number">{{ stats.activeUsers }}</div>
              <div class="stat-label">Active Users</div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { referralApi } from '@/api'
import type { CodingSharingVo } from '@/types'
import { ElMessage } from 'element-plus'

const activeTab = ref('referrals')
const loading = ref(false)
const pendingReferrals = ref<CodingSharingVo[]>([])
const referralPagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const stats = ref({
  totalPosts: 0,
  totalReferrals: 0,
  pendingReferrals: 0,
  activeUsers: 0
})

const loadPendingReferrals = async () => {
  loading.value = true
  try {
    const response = await referralApi.adminGetPage({
      pageNum: referralPagination.value.pageNum,
      pageSize: referralPagination.value.pageSize
    })
    pendingReferrals.value = response.records
    referralPagination.value.total = response.total
    stats.value.pendingReferrals = response.total
  } catch (error) {
    console.error('Error loading pending referrals:', error)
    ElMessage.error('Failed to load pending referrals')
  } finally {
    loading.value = false
  }
}

const approveReferral = async (id: number) => {
  try {
    await referralApi.adminPermit(id, 1)
    ElMessage.success('Referral approved')
    loadPendingReferrals()
  } catch (error) {
    console.error('Error approving referral:', error)
    ElMessage.error('Failed to approve referral')
  }
}

const rejectReferral = async (id: number) => {
  try {
    await referralApi.adminPermit(id, 2)
    ElMessage.success('Referral rejected')
    loadPendingReferrals()
  } catch (error) {
    console.error('Error rejecting referral:', error)
    ElMessage.error('Failed to reject referral')
  }
}

onMounted(() => {
  loadPendingReferrals()
})
</script>

<style scoped>
.admin-dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  text-align: center;
  padding: 30px;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: #606266;
}
</style>

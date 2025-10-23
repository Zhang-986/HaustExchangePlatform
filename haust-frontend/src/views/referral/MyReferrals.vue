<template>
  <div class="my-referrals">
    <el-card>
      <template #header>
        <h2>My Referrals</h2>
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
        <el-table-column label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Rating" width="120" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.recommandIndex" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Created" width="180" />
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="warning" link @click="editReferral(row)">
              Edit
            </el-button>
            <el-button type="danger" link @click="deleteReferral(row.id)">
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
        @size-change="loadReferrals"
        @current-change="loadReferrals"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { referralApi } from '@/api'
import type { CodingSharing } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const referrals = ref<CodingSharing[]>([])
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadReferrals = async () => {
  loading.value = true
  try {
    const response = await referralApi.getMyInfo({
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

const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'warning'
    case 1: return 'success'
    case 2: return 'danger'
    default: return 'info'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return 'Pending'
    case 1: return 'Approved'
    case 2: return 'Rejected'
    default: return 'Unknown'
  }
}

const editReferral = (_referral: CodingSharing) => {
  // Edit functionality to be implemented
  ElMessage.info('Edit functionality coming soon')
}

const deleteReferral = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete this referral?',
      'Warning',
      {
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    await referralApi.deleteInfo(id)
    ElMessage.success('Referral deleted successfully')
    loadReferrals()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error deleting referral:', error)
      ElMessage.error('Failed to delete referral')
    }
  }
}

onMounted(() => {
  loadReferrals()
})
</script>

<style scoped>
.my-referrals {
  max-width: 1400px;
  margin: 0 auto;
}
</style>

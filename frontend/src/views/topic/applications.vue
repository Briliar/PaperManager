<template>
  <div class="applications-container">
    <el-card class="filter-card" shadow="never">
      <div class="clearfix">
        <span>学生选题审批大厅</span>
        <span style="font-size: 13px; color: #999; margin-left: 10px;">(演示: 默认拉取所有关联课题的申请记录)</span>
      </div>
      <el-form :inline="true" :model="queryParams" class="mt-20">
        <el-form-item label="关联课题ID">
          <el-input v-model="queryParams.topicId" placeholder="输入课题ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询申请</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card mt-20" shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="studentId" label="申请人ID (模拟)" width="150" />
        <el-table-column prop="topicId" label="申请课题ID" width="150" />
        <el-table-column prop="createTime" label="申请时间" min-width="180" />
        <el-table-column prop="status" label="审核状态" width="120" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已同意</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="scope">
            <el-button 
              type="success" 
              link 
              icon="Check" 
              v-if="scope.row.status === 0" 
              @click="handleAudit(scope.row, 1)"
            >同意</el-button>
            <el-button 
              type="danger" 
              link 
              icon="Close" 
              v-if="scope.row.status === 0" 
              @click="handleAudit(scope.row, 2)"
            >拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTeacherAuditList, auditApplication } from '../../api/studentTopic'
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({
  current: 1,
  size: 10,
  topicId: 1 // 模拟查询测试数据的ID
})

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const getList = async () => {
  if (!queryParams.topicId) {
    ElMessage.warning('请输入课题ID进行查询')
    return
  }
  loading.value = true
  try {
    const res = await getTeacherAuditList(queryParams.topicId, {
      current: queryParams.current,
      size: queryParams.size
    })
    tableData.value = res.records
    total.value = res.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.current = 1
  getList()
}

const handleAudit = (row, status) => {
  const action = status === 1 ? '同意' : '拒绝'
  ElMessageBox.confirm(`确定要 ${action} 该学生的选题申请吗？`, '审批确认', {
    type: status === 1 ? 'success' : 'warning'
  }).then(async () => {
    try {
      await auditApplication(row.id, status)
      ElMessage.success(`已${action}`)
      getList()
    } catch (error) {
      // 错误已由拦截器处理
    }
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}
.mt-20 {
  margin-top: 20px;
}
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

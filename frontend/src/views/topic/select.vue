<template>
  <div class="topic-select-container">
    <el-card class="filter-card" shadow="never">
      <div class="clearfix">
        <span>可选课题大厅</span>
      </div>
      <el-form :inline="true" :model="queryParams" class="mt-20">
        <el-form-item label="课题名称">
          <el-input v-model="queryParams.title" placeholder="输入课题名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索课题</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="mt-20" v-loading="loading">
      <el-col :span="8" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px;">
        <el-card shadow="hover" class="topic-card">
          <template #header>
            <div class="card-header">
              <span class="topic-title">{{ item.title }}</span>
              <el-tag type="success" size="small">{{ item.type }}</el-tag>
            </div>
          </template>
          <div class="topic-body">
            <p><strong>课题来源：</strong>{{ item.source }}</p>
            <p><strong>限选人数：</strong>{{ item.maxStudents }} 人</p>
            <p class="summary-text"><strong>简介：</strong>{{ item.summary || '暂无简介' }}</p>
          </div>
          <div class="topic-footer">
            <el-button type="primary" plain size="small" @click="handleApply(item.id)">申请该课题</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        layout="prev, pager, next"
        :total="total"
        @current-change="getList"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTopicPage } from '../../api/topic'
import { applyTopic } from '../../api/studentTopic'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const queryParams = reactive({
  current: 1,
  size: 9, // 每页展示9个卡片
  title: '',
  status: 1 // 只查询已通过的课题
})

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const getList = async () => {
  loading.value = true
  try {
    const res = await getTopicPage(queryParams)
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

const handleApply = (topicId) => {
  const studentId = userStore.userInfo?.userId || 2 // 临时兜底模拟
  ElMessageBox.confirm('确定申请该课题吗？申请后需等待导师审核。', '申请确认', {
    type: 'info'
  }).then(async () => {
    try {
      await applyTopic(studentId, topicId)
      ElMessage.success('申请已提交！请前往工作台查看进度')
    } catch (e) {
      // 错误由拦截器弹出
    }
  }).catch(() => {})
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.topic-card {
  height: 250px;
  display: flex;
  flex-direction: column;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.topic-title {
  font-weight: bold;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}
.topic-body {
  flex: 1;
  font-size: 14px;
  color: #606266;
}
.summary-text {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}
.topic-footer {
  margin-top: 15px;
  text-align: right;
}
.mt-20 { margin-top: 20px; }
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

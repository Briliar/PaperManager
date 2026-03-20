<template>
  <div class="proposal-teacher-container">
    <el-card class="filter-card" shadow="never">
      <div class="clearfix">
        <span>开题报告审批大厅</span>
      </div>
      <el-form :inline="true" :model="queryParams" class="mt-20">
        <el-form-item label="关联课题ID">
          <el-input v-model="queryParams.topicId" placeholder="输入课题ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询开题报告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card mt-20" shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="studentId" label="申请人ID" width="100" align="center" />
        <el-table-column prop="createTime" label="提交时间" width="180" />
        <el-table-column prop="status" label="审核状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherComment" label="导师意见" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link icon="View" @click="handleView(scope.row)">审阅报告</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 审阅弹窗 -->
    <el-dialog title="开题报告审阅" v-model="dialogVisible" width="700px" append-to-body top="5vh">
      <el-descriptions border :column="2">
        <el-descriptions-item label="学生ID">{{ currentRow.studentId }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentRow.createTime }}</el-descriptions-item>
      </el-descriptions>
      
      <div style="margin-top: 15px;">
        <p style="font-weight: bold; margin-bottom: 5px;">报告正文：</p>
        <el-input type="textarea" :rows="12" v-model="currentRow.content" disabled />
      </div>

      <div style="margin-top: 15px;" v-if="currentRow.status === 0">
        <p style="font-weight: bold; margin-bottom: 5px;">审核意见 (驳回时必填)：</p>
        <el-input type="textarea" :rows="3" v-model="auditForm.comment" placeholder="请输入修改意见..." />
      </div>

      <template #footer>
        <div class="dialog-footer" v-if="currentRow.status === 0">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitAudit(2)">驳 回 修 改</el-button>
          <el-button type="success" @click="submitAudit(1)">审 核 通 过</el-button>
        </div>
        <div class="dialog-footer" v-else>
          <el-button @click="dialogVisible = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTeacherProposalList, auditProposal } from '../../api/proposal'
import { ElMessage } from 'element-plus'

const queryParams = reactive({
  current: 1,
  size: 10,
  topicId: 1
})

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const dialogVisible = ref(false)
const currentRow = reactive({})
const auditForm = reactive({
  comment: ''
})

const getList = async () => {
  if (!queryParams.topicId) {
    ElMessage.warning('请输入课题ID进行查询')
    return
  }
  loading.value = true
  try {
    const res = await getTeacherProposalList(queryParams.topicId, {
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

const handleView = (row) => {
  Object.assign(currentRow, row)
  auditForm.comment = ''
  dialogVisible.value = true
}

const submitAudit = async (status) => {
  if (status === 2 && !auditForm.comment.trim()) {
    ElMessage.warning('驳回时必须填写审核意见！')
    return
  }
  try {
    await auditProposal(currentRow.id, status, auditForm.comment)
    ElMessage.success(status === 1 ? '已通过该开题报告' : '已打回要求修改')
    dialogVisible.value = false
    getList()
  } catch (error) {
    console.error(error)
  }
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

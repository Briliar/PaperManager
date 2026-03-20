<template>
  <div class="weekly-container">
    <el-card shadow="never">
      <template #header>
        <div class="clearfix" style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight:bold; font-size:16px;">我的周志</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">填写周志</el-button>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="weekNum" label="周次" width="80" align="center">
          <template #default="scope">第 {{ scope.row.weekNum }} 周</template>
        </el-table-column>
        <el-table-column prop="content" label="周志内容" show-overflow-tooltip />
        <el-table-column prop="createTime" label="提交时间" width="180" />
        <el-table-column prop="status" label="导师状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已阅</el-tag>
            <el-tag v-else type="danger">需改</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherComment" label="导师批语" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-dialog title="填写周志" v-model="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="周次">
          <el-input-number v-model="form.weekNum" :min="1" :max="20" />
        </el-form-item>
        <el-form-item label="周志内容">
          <el-input type="textarea" :rows="8" v-model="form.content" placeholder="记录本周完成的任务、遇到的问题以及下周计划..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit" :loading="submitLoading">提 交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getWeeklyPage, saveWeekly } from '../../api/weekly'
import { useUserStore } from '../../store/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const submitLoading = ref(false)
const form = reactive({ weekNum: 1, content: '', studentId: null, topicId: 1 }) // simulate topicId

const getList = async () => {
  loading.value = true
  const studentId = userStore.userInfo?.userId || 2
  form.studentId = studentId
  try {
    const res = await getWeeklyPage({ studentId })
    tableData.value = res.records
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.content = ''
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.content) return ElMessage.warning('请填写周志内容')
  submitLoading.value = true
  try {
    await saveWeekly(form)
    ElMessage.success('提交成功')
    dialogVisible.value = false
    getList()
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => getList())
</script>

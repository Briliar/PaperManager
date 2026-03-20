<template>
  <div class="thesis-container">
    <el-card shadow="never">
      <template #header>
        <div class="clearfix" style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight:bold; font-size:16px;">论文与成果提交</span>
          <el-button type="primary" icon="Upload" @click="dialogVisible = true">上传新文档</el-button>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="type" label="文档类型" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 0" type="info">初稿</el-tag>
            <el-tag v-else type="primary">定稿</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名" />
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column prop="status" label="审核状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">通过</el-tag>
            <el-tag v-else type="danger">打回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="最终得分" width="100" align="center">
          <template #default="scope">
            <span style="color:red; font-weight:bold" v-if="scope.row.score">{{ scope.row.score }} 分</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="teacherComment" label="导师反馈" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-dialog title="上传论文文档" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="文档类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">初稿</el-radio>
            <el-radio :label="1">定稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="文档链接">
          <el-input v-model="form.fileUrl" placeholder="由于演示环境不支持上传文件，请输入云盘链接" />
        </el-form-item>
        <el-form-item label="文档名称">
          <el-input v-model="form.fileName" placeholder="例如：毕业设计论文_张三_v1.pdf" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit" :loading="submitLoading">确认上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getThesisPage, submitThesis } from '../../api/thesis'
import { useUserStore } from '../../store/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const submitLoading = ref(false)
const form = reactive({ type: 0, fileUrl: '', fileName: '', studentId: null, topicId: 1 })

const getList = async () => {
  loading.value = true
  const studentId = userStore.userInfo?.userId || 2
  form.studentId = studentId
  try {
    const res = await getThesisPage({ studentId })
    tableData.value = res.records
  } finally {
    loading.value = false
  }
}

const submit = async () => {
  if (!form.fileName || !form.fileUrl) return ElMessage.warning('请填写文档名称和链接')
  submitLoading.value = true
  try {
    await submitThesis(form)
    ElMessage.success('上传成功')
    dialogVisible.value = false
    getList()
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => getList())
</script>

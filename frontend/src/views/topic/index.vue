<template>
  <div class="topic-container">
    <!-- 搜索与操作区 -->
    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="form-inline">
        <el-form-item label="课题名称">
          <el-input v-model="queryParams.title" placeholder="请输入课题名称关键字" clearable />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已驳回" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="Plus" @click="handleAdd">申报课题</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格区 -->
    <el-card class="table-card" shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="title" label="课题名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="source" label="课题来源" width="120" />
        <el-table-column prop="type" label="课题类型" width="120" />
        <el-table-column prop="maxStudents" label="限选人数" width="100" align="center" />
        <el-table-column prop="status" label="审核状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申报时间" width="160" />
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              type="success" 
              link 
              icon="Check" 
              v-if="scope.row.status === 0 && userStore.userInfo?.roleId === 1" 
              @click="handleAudit(scope.row.id, 1)"
            >通过</el-button>
            <el-button 
              type="warning" 
              link 
              icon="Close" 
              v-if="scope.row.status === 0 && userStore.userInfo?.roleId === 1" 
              @click="handleAudit(scope.row.id, 2)"
            >驳回</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 申报/编辑 弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
      <el-form ref="topicFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课题名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入课题名称" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="课题来源" prop="source">
              <el-select v-model="form.source" placeholder="请选择来源" style="width: 100%">
                <el-option label="自拟" value="自拟" />
                <el-option label="企业实际" value="企业实际" />
                <el-option label="科研项目" value="科研项目" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课题类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
                <el-option label="工程设计" value="工程设计" />
                <el-option label="理论研究" value="理论研究" />
                <el-option label="软件开发" value="软件开发" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="限选人数" prop="maxStudents">
          <el-input-number v-model="form.maxStudents" :min="1" :max="5" />
        </el-form-item>
        <el-form-item label="课题简介" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入课题简介与目标" />
        </el-form-item>
        <el-form-item label="任务与要求" prop="requireDesc">
          <el-input v-model="form.requireDesc" type="textarea" :rows="4" placeholder="请输入具体任务与学生要求" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTopicPage, saveTopic, deleteTopic, auditTopic } from '../../api/topic'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

// 查询参数
const queryParams = reactive({
  current: 1,
  size: 10,
  title: '',
  status: null
})

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const topicFormRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  source: '',
  type: '',
  maxStudents: 1,
  summary: '',
  requireDesc: ''
})

const rules = {
  title: [{ required: true, message: '请输入课题名称', trigger: 'blur' }],
  source: [{ required: true, message: '请选择课题来源', trigger: 'change' }],
  type: [{ required: true, message: '请选择课题类型', trigger: 'change' }]
}

// 获取列表数据
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

const resetQuery = () => {
  queryParams.title = ''
  queryParams.status = null
  handleSearch()
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.title = ''
  form.source = ''
  form.type = ''
  form.maxStudents = 1
  form.summary = ''
  form.requireDesc = ''
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '申报新课题'
  dialogVisible.value = true
  if (topicFormRef.value) topicFormRef.value.clearValidate()
}

const handleEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑课题信息'
  dialogVisible.value = true
}

const submitForm = () => {
  topicFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        await saveTopic(form)
        ElMessage.success(form.id ? '修改成功' : '申报成功，等待审核')
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除课题 "${row.title}" 吗？`, '警告', {
    type: 'warning'
  }).then(async () => {
    await deleteTopic(row.id)
    ElMessage.success('删除成功')
    getList()
  }).catch(() => {})
}

const handleAudit = (id, status) => {
  const action = status === 1 ? '通过' : '驳回'
  ElMessageBox.confirm(`确认${action}该课题吗？`, '审核提示', {
    type: status === 1 ? 'success' : 'warning'
  }).then(async () => {
    await auditTopic(id, status)
    ElMessage.success(`已${action}`)
    getList()
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
.form-inline .el-form-item {
  margin-bottom: 0;
}
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

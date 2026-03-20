<template>
  <div class="proposal-student-container">
    <el-card shadow="never">
      <div class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">我的开题报告</span>
      </div>

      <div v-if="loading" style="padding: 50px; text-align: center;">加载中...</div>
      
      <div v-else class="content-wrapper">
        <el-alert
          v-if="proposal.status === 2"
          title="您的开题报告被驳回！"
          type="error"
          show-icon
          :description="'指导老师意见: ' + proposal.teacherComment"
          class="mb-20"
        />
        <el-alert
          v-else-if="proposal.status === 1"
          title="恭喜！您的开题报告已通过审核"
          type="success"
          show-icon
          class="mb-20"
        />
        <el-alert
          v-else-if="proposal.status === 0"
          title="开题报告审核中，请耐心等待..."
          type="warning"
          show-icon
          class="mb-20"
        />

        <el-form label-position="top">
          <el-form-item label="报告正文内容 (支持直接填写或粘贴内容)">
            <el-input 
              v-model="proposal.content" 
              type="textarea" 
              :rows="15" 
              placeholder="请输入研究背景、目的意义、研究内容与方法、进度安排等..."
              :disabled="proposal.status === 1 || proposal.status === 0"
            />
          </el-form-item>
          <el-form-item v-if="proposal.status !== 1 && proposal.status !== 0">
            <el-button type="primary" icon="Upload" @click="handleSubmit" :loading="submitLoading">提交审核</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMyProposal, submitProposal } from '../../api/proposal'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const loading = ref(false)
const submitLoading = ref(false)

const proposal = reactive({
  id: null,
  studentId: null,
  topicId: null,
  content: '',
  status: -1, // -1: 未提交
  teacherComment: ''
})

const getProposal = async () => {
  loading.value = true
  try {
    const studentId = userStore.userInfo?.userId || 2
    proposal.studentId = studentId
    const res = await getMyProposal(studentId)
    if (res) {
      proposal.id = res.id
      proposal.topicId = res.topicId
      proposal.content = res.content
      proposal.status = res.status
      proposal.teacherComment = res.teacherComment
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSubmit = () => {
  if (!proposal.content) {
    ElMessage.warning('开题报告内容不能为空')
    return
  }
  ElMessageBox.confirm('提交后将进入审核流程，暂不能修改。确认提交？', '提交确认', {
    type: 'warning'
  }).then(async () => {
    submitLoading.value = true
    try {
      await submitProposal({
        studentId: proposal.studentId,
        content: proposal.content
      })
      ElMessage.success('开题报告提交成功')
      getProposal()
    } catch (e) {
      // 报错已经在axios拦截器处理
    } finally {
      submitLoading.value = false
    }
  }).catch(() => {})
}

onMounted(() => {
  getProposal()
})
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}
.content-wrapper {
  margin-top: 20px;
}
</style>

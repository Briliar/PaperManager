<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card bg-blue">
          <div class="card-header">我的课题数</div>
          <div class="card-value">2</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card bg-green">
          <div class="card-header">开题报告状态</div>
          <div class="card-value text-sm">已通过</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card bg-orange">
          <div class="card-header">待办任务</div>
          <div class="card-value">5</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card bg-purple">
          <div class="card-header">答辩倒计时</div>
          <div class="card-value">45 <span class="unit">天</span></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="clearfix">
              <span>系统公告</span>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item timestamp="2024-03-20" placement="top" type="primary">
              <el-card>
                <h4>关于提交毕业设计（论文）开题报告的通知</h4>
                <p>请各位同学于本周五前完成开题报告系统的提交与导师审核。</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="2024-03-15" placement="top">
              <el-card>
                <h4>毕业设计系统上线试运行</h4>
                <p>系统已完成基础模块升级，欢迎师生使用。</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div class="clearfix">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" plain>选择课题</el-button>
            <el-button type="success" plain>上传开题</el-button>
            <el-button type="warning" plain>周志填写</el-button>
            <el-button type="danger" plain>联系导师</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 10px;
}
.data-card {
  color: #fff;
  border: none;
  border-radius: 8px;
}
.bg-blue { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.bg-green { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.bg-orange { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
.bg-purple { background: linear-gradient(135deg, #c471f5 0%, #fa71cd 100%); }

.card-header {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 10px;
}
.card-value {
  font-size: 32px;
  font-weight: bold;
}
.card-value.text-sm {
  font-size: 24px;
}
.unit {
  font-size: 16px;
  font-weight: normal;
}
.mt-20 {
  margin-top: 20px;
}
.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}
.quick-actions .el-button {
  margin: 0;
  width: 100%;
}
</style>

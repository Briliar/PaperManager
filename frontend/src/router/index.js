import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue')
  },
  {
    path: '/',
    component: () => import('../views/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: '工作台首页', icon: 'House' }
      },
      {
        path: 'topic',
        name: 'Topic',
        component: () => import('../views/topic/index.vue'),
        meta: { title: '课题申报与管理', icon: 'Document' }
      },
      {
        path: 'select',
        name: 'TopicSelect',
        component: () => import('../views/topic/select.vue'),
        meta: { title: '学生双选大厅', icon: 'List' }
      },
      {
        path: 'applications',
        name: 'Applications',
        component: () => import('../views/topic/applications.vue'),
        meta: { title: '学生选题审批', icon: 'User' }
      },
      {
        path: 'proposal/student',
        name: 'StudentProposal',
        component: () => import('../views/proposal/student.vue'),
        meta: { title: '我的开题报告', icon: 'EditPen' }
      },
      {
        path: 'proposal/teacher',
        name: 'TeacherProposal',
        component: () => import('../views/proposal/teacher.vue'),
        meta: { title: '开题报告审核', icon: 'Tickets' }
      },
      {
        path: 'thesis',
        name: 'Thesis',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: '论文与答辩', icon: 'Files' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

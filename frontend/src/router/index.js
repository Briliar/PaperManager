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
        meta: { title: '选题管理', icon: 'Document' }
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

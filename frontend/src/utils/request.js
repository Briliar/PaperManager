import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '/api', // 代理前缀
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 可以在此处添加 token: config.headers['Authorization'] = 'Bearer ' + token
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // 如果返回的 code 不是 200，说明后端业务报错
    if (res.code !== 200) {
      ElMessage.error(res.msg || 'Error')
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res.data
    }
  },
  error => {
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service

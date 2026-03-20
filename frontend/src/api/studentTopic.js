import request from '../utils/request'

export function applyTopic(studentId, topicId) {
  return request({
    url: '/student-topic/apply',
    method: 'post',
    params: { studentId, topicId }
  })
}

export function getMyApply(studentId, params) {
  return request({
    url: '/student-topic/my-apply',
    method: 'get',
    params: { studentId, ...params }
  })
}

export function getTeacherAuditList(topicId, params) {
  return request({
    url: '/student-topic/teacher-list',
    method: 'get',
    params: { topicId, ...params }
  })
}

export function auditApplication(id, status) {
  return request({
    url: `/student-topic/audit/${id}`,
    method: 'post',
    params: { status }
  })
}

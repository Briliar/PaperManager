import request from '../utils/request'

export function submitProposal(data) {
  return request({
    url: '/proposal/submit',
    method: 'post',
    data
  })
}

export function getMyProposal(studentId) {
  return request({
    url: '/proposal/my',
    method: 'get',
    params: { studentId }
  })
}

export function getTeacherProposalList(topicId, params) {
  return request({
    url: '/proposal/teacher-list',
    method: 'get',
    params: { topicId, ...params }
  })
}

export function auditProposal(id, status, comment) {
  return request({
    url: `/proposal/audit/${id}`,
    method: 'post',
    params: { status, comment }
  })
}

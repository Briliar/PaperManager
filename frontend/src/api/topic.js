import request from '../utils/request'

export function getTopicPage(params) {
  return request({
    url: '/topic/page',
    method: 'get',
    params
  })
}

export function saveTopic(data) {
  return request({
    url: '/topic/save',
    method: 'post',
    data
  })
}

export function deleteTopic(id) {
  return request({
    url: `/topic/${id}`,
    method: 'delete'
  })
}

export function auditTopic(id, status) {
  return request({
    url: `/topic/audit/${id}`,
    method: 'post',
    params: { status }
  })
}

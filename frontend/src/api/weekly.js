import request from '../utils/request'
export const saveWeekly = data => request.post('/weekly/save', data)
export const getWeeklyPage = params => request.get('/weekly/page', { params })
export const auditWeekly = (id, params) => request.post(`/weekly/audit/${id}`, null, { params })

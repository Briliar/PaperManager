import request from '../utils/request'
export const submitThesis = data => request.post('/thesis/submit', data)
export const getThesisPage = params => request.get('/thesis/page', { params })
export const auditThesis = (id, params) => request.post(`/thesis/audit/${id}`, null, { params })

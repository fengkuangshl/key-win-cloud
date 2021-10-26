import request from '../../fetch'

export const LoginApi = (data: string): Promise<Ajax.AjaxResult> => request.post('/api-auth/oauth/token', data)

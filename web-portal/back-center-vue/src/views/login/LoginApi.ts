import request from '../../fetch'
import { LoginResponse } from './interface/Response'

export const LoginApi = (data: string): Promise<Ajax.AjaxResult<LoginResponse>> => request.post('/api-auth/oauth/token', data)

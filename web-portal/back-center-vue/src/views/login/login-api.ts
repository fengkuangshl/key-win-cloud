import request from '@/fetch'
import { LoginResponse } from './interface/response'

export const LoginApi = (data: string): Promise<KWResponse.Result<LoginResponse>> => request.post('/api-auth/oauth/token', data)

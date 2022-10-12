import request from '@/fetch'
import settings from '@/settings'
import { LoginResponse } from './interface'

export const LoginApi = (data: string): Promise<KWResponse.Result<LoginResponse>> => request.post(settings.apiAuth + 'oauth/token', data)

import request from '../../../../fetch'
import { CurrentUserResponse } from './interface/UserResponse'

export const UserInfoApi = (): Promise<Ajax.AjaxResult<CurrentUserResponse>> => request.get('api-auth/oauth/current/userinfo', {})

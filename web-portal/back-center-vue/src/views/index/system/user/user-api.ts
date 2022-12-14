import request from '@/fetch'
import { LoginSuccessUserInfo, UserInfo, UserSearchRequest, UserStatuChangeRequest, UserForm, ModifyPassword } from './interface/user'

export const UserInfoApi = (): Promise<KWResponse.Result<LoginSuccessUserInfo>> => request.get('api-auth/oauth/current/userinfo')
export const UserPagedApi = (pageRequest: KWRequest.PageRequest<UserSearchRequest>): Promise<KWResponse.PageResult<UserInfo>> => request.post('api-user/getSysUserByPaged', pageRequest)
export const UserStatuChangeRequestApi = (param: UserStatuChangeRequest): Promise<KWResponse.Result> => request.get('api-user/users/updateEnabled', param)
export const UserGetApi = (id: string): Promise<KWResponse.Result<UserInfo>> => request.get('api-user/users/' + id)
export const UserSaveOrUpdateApi = (userInfo: UserForm): Promise<KWResponse.Result> => request.post('api-user/users/saveOrUpdate', userInfo)
export const ResetPasswordApi = (id: string): Promise<KWResponse.Result<UserInfo>> => request.post('api-user/users/' + id + '/resetPassword')
export const UpdateMeApi = (userInfo: UserForm): Promise<KWResponse.Result<UserInfo>> => request.put('api-user/users/me', userInfo)
export const UpdatePasswordApi = (modifyPassword: ModifyPassword): Promise<KWResponse.Result<UserInfo>> => request.put('api-user/users/password', modifyPassword)

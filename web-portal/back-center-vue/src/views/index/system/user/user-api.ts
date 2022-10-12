import request from '@/fetch'
import settings from '@/settings'
import { LoginSuccessUserInfo, UserInfo, UserSearchRequest, UserStatusChange, UserForm, ModifyPassword } from './interface/sys-user'

export const UserInfoApi = (): Promise<KWResponse.Result<LoginSuccessUserInfo>> => request.get(settings.apiUser + 'user/getLoginApp')
export const UserPagedApi = (pageRequest: KWRequest.PageRequest<UserSearchRequest>): Promise<KWResponse.PageResult<UserInfo>> => request.post(settings.apiUser + 'user/getSysUserByPaged', pageRequest)
export const UserStatusChangeRequestApi = (param: UserStatusChange): Promise<KWResponse.Result> => request.post(settings.apiUser + 'user/updateEnabled', param)
export const UserGetApi = (id: number): Promise<KWResponse.Result<UserInfo>> => request.get(settings.apiUser + 'user/getUserFullById/' + id)
export const UserSaveOrUpdateApi = (userInfo: UserForm): Promise<KWResponse.Result> => request.post(settings.apiUser + 'user/saveOrUpdate', userInfo)
export const ResetPasswordApi = (id: number): Promise<KWResponse.Result<UserInfo>> => request.get(settings.apiUser + 'user/resetPassword/' + id)
export const UpdateMeApi = (userInfo: UserForm): Promise<KWResponse.Result<UserInfo>> => request.put(settings.apiUser + 'user/me', userInfo)
export const UpdatePasswordApi = (modifyPassword: ModifyPassword): Promise<KWResponse.Result<UserInfo>> => request.post(settings.apiUser + 'user/modifyMyPassword', modifyPassword)
export const LogoutApi = (token: string): Promise<KWResponse.Result> => request.post(settings.apiAuth + 'oauth/remove/token?access_token=' + token)

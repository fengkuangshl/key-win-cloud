import request from '../../../../fetch'
import { LoginSuccessUserInfo, UserInfo, UserSearchRequest, UserStatuChangeRequest, UserForm } from './interface/User'

export const UserInfoApi = (): Promise<KWResponse.Result<LoginSuccessUserInfo>> => request.get('api-auth/oauth/current/userinfo')
export const UserPagedApi = (pageRequest: KWRequest.PageRequest<UserSearchRequest>): Promise<KWResponse.PageResult<UserInfo>> => request.post('api-user/getSysUserByPaged', pageRequest)
export const UserStatuChangeRequestApi = (param: UserStatuChangeRequest): Promise<KWResponse.Result> => request.get('api-user/users/updateEnabled', param)
export const UserGetApi = (id: string): Promise<KWResponse.Result<UserInfo>> => request.get('api-user/users/' + id)
export const UserSaveOrUpdateApi = (userInfo: UserForm): Promise<KWResponse.Result> => request.post('api-user/users/saveOrUpdate', userInfo)
export const ResetPasswordApi = (id: string): Promise<KWResponse.Result<UserInfo>> => request.post('api-user/users/' + id + '/resetPassword')

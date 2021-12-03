import { PermissionResponse } from '../../permission/interface/sys-permission'
import { SysRole } from '../../sys-role/interface/sys-role'

export enum Sex {
  男 = 0,
  女 = 1
}

export interface UserSearchRequest {
  nickname: string
}

export interface UserForm extends UserSearchRequest {
  roleId: Array<string> | string
  sex: Sex | string
  phone: string
  username: string
}

export interface UserPassword {
  newPassword: string | null
  oldPassword: string | null
}

export interface ModifyPassword extends Model.Id, UserPassword {
  rePassword: string
}

export interface Enabled {
  enabled: boolean
}

export interface UserInfo extends UserForm, Model.BaseFleid, UserPassword, Enabled {
  headImgUrl: string | null
  password: string
  roles: Array<SysRole>
  type: string
}

export interface UserExt extends UserInfo {
  accountNonExpired: boolean
  accountNonLocked: boolean
  credentialsNonExpired: boolean
  permissions: Array<PermissionResponse>
  sysRoles: Array<SysRole>
}

export interface LoginSuccessUserInfo {
  permissions: Array<PermissionResponse>
  user: UserExt
}

export interface UserStatuChange extends Model.Id, Enabled {}

export interface UserStatuChangeRequest {
  params: UserStatuChange
}

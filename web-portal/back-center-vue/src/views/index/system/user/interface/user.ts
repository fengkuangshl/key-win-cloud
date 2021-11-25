import { PermissionResponse } from '../../permission/interface/permission-response'
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

export interface UserInfo extends UserForm, Model.BaseFleidCU, UserPassword {
  enabled: boolean
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

export interface UserStatuChangeRequest {
  params: { id: string; enabled: boolean }
}

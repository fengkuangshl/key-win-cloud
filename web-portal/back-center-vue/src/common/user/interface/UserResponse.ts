import { PermissionResponse } from '../../permission/interface/PermissionResponse'
import { SysRolesResponse } from '../../sysRole/interface/SysRoleResponse'

export interface UserResponse {
    accountNonExpired: boolean
    accountNonLocked: boolean
    createTime: number
    credentialsNonExpired: boolean
    enabled: boolean
    headImgUrl: string | null
    id: string
    newPassword: string | null
    nickname: string
    oldPassword: string | null
    password: string
    permissions: Array<PermissionResponse>
    phone: string
    roleId: string | null
    roles: string | null
    sex: number
    sysRoles: Array<SysRolesResponse>
    type: string
    updateTime: number
    username: string
}

export interface CurrentUserResponse {
    permissions: Array<PermissionResponse>
    user: UserResponse
}

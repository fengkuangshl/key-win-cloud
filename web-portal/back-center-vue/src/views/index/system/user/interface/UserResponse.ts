import { PermissionResponse } from '../../permission/interface/PermissionResponse'
import { SysRolesResponse } from '../../sysRole/interface/SysRoleResponse'
import { BaseFleidCU } from '../../../../../common/interface/BaseInterface'
export interface UserResponse extends BaseFleidCU{
    accountNonExpired: boolean
    accountNonLocked: boolean
    credentialsNonExpired: boolean
    enabled: boolean
    headImgUrl: string | null
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
    username: string
}

export interface CurrentUserResponse {
    permissions: Array<PermissionResponse>
    user: UserResponse
}

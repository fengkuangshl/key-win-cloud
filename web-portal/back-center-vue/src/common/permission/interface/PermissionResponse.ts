import { BaseFleidCU } from '../../base/BaseInterface'

export interface PermissionResponse extends BaseFleidCU{
    authIds: string | null
    name: string | null
    permission: string
    roleId: string | null
}

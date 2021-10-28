import { BaseFleidCU } from '../../base/BaseInterface'
export interface SysRolesResponse extends BaseFleidCU{
    code: string
    name: string
    userId: string | null
}

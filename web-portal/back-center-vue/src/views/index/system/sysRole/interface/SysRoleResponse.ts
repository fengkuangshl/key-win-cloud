import { BaseFleidCU } from '../../../../../common/interface/BaseInterface'
export interface SysRolesResponse extends BaseFleidCU{
    code: string
    name: string
    userId: string | null
}

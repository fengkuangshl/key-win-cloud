export interface PermissionResponse extends Model.BaseFleidCU{
    authIds: string | null
    name: string | null
    permission: string
    roleId: string | null
}

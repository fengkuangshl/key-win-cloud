export interface SysRoleSearchRequest {
  name: string
}
export interface SysRoleForm extends SysRoleSearchRequest {
  code: string
  name: string
}
export interface SysRole extends Model.BaseFleidCU, SysRoleForm {
  userId: string | null
}

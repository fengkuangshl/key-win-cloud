export interface AuthIdsAndRoleId {
  authIds: Array<string>
  roleId: string | null
}

export interface Name {
  name: string | null
}

export interface PermissionResponse extends Model.BaseFleidCU, AuthIdsAndRoleId, Name {
  permission: string
}
export interface PermissionRole extends Name, Model.Id {
  checked: true
  open: true
}

export interface AuthIdsAndRoleId {
  authIds: Array<string>
  roleId: string | null
}

export interface Name {
  name: string | null
}
export interface PermissionForm extends Name {
  permission: string
}

export interface PermissionResponse extends Model.BaseFleid, AuthIdsAndRoleId, PermissionForm {}
export interface PermissionRole extends Name, Model.Id {
  checked: true
  open: true
}

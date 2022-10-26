export interface MenuPermissionForm extends Model.Id {
  menuId: string
  permissionId: string
  checked: boolean
}

export interface MenuPermissionDetail extends Model.BaseField, MenuPermissionForm {
  permissionCode: string
  menuName: string
  permissionName: string
  propertyName: string
}

export interface SysMenuPermissionTableDataType {
  [x: string]: MenuPermissionDetail | Array<SysMenuPermissionTableDataType> | string // 动态添加属性
}

export interface SysMenuPermissionResponse {
  data: Array<SysMenuPermissionTableDataType>
  title: Array<MenuPermissionDetail>
}

import { TreeData } from 'element-ui/types/tree'

export interface Name {
  name: string
}
export interface RoleIdAndMenuIds {
  menuIds: Array<number> | null
  roleId: string | null
}
export interface MenuForm extends Name {
  css: string
  hidden: false
  isMenu: number
  parentId: string
  path: string
  sort: number
  url: string
}
export interface MenuResponse extends Model.BaseFleidCU, Name, RoleIdAndMenuIds, MenuForm {
  subMenus: Array<MenuResponse> | null
}
export interface Id extends Name {
  id: number
}

export interface IMenuTree extends Name, TreeData {
  children: Array<IMenuTree>
}

export interface MenuRole extends Id {
  checked: boolean
  pId: number
  open: boolean
}

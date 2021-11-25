import { TreeData } from 'element-ui/types/tree'

export interface Name {
  name: string
}
export interface RoleIdAndMenuIds {
  menuIds: Array<number> | null
  roleId: string | null
}
export interface MenuResponse extends Model.BaseFleidCU, Name, RoleIdAndMenuIds {
  css: string
  hidden: false
  isMenu: number
  name: string
  parentId: string
  path: string
  sort: number
  subMenus: Array<MenuResponse> | null
  url: string
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

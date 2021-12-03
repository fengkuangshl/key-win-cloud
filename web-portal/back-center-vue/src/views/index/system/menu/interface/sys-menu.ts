import { TreeData } from 'element-ui/types/tree'

export enum IsHiddenMenu {
  是 = 1,
  否 = 0
}
export enum IsMenu {
  是 = 1,
  否 = 2
}

export interface Name {
  name: string
}
export interface RoleIdAndMenuIds {
  menuIds: Array<string> | null
  roleId: string | null
}
export interface MenuForm extends Name {
  css: string
  hidden: IsHiddenMenu | boolean | string
  isMenu: IsMenu | number | string
  parentId: string
  path: string
  sort: number
  url: string
}
export interface MenuResponse extends Model.BaseFleid, Name, RoleIdAndMenuIds, MenuForm {
  subMenus: Array<MenuResponse> | null
}
export interface Id extends Name {
  id: string
}

export interface IMenuTree extends Name, TreeData {
  children: Array<IMenuTree>
}

export interface MenuRole extends Id {
  checked: boolean
  pId: string
  open: boolean
}

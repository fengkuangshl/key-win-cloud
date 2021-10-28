import { BaseFleidCU } from '../../base/BaseInterface'

export interface MenuResponse extends BaseFleidCU {
    css: string
    hidden: false
    isMenu: number
    menuIds: string | null
    name: string
    parentId: string
    path: string
    roleId: string | null
    sort: number
    subMenus: Array<MenuResponse> | null
    url: string
}

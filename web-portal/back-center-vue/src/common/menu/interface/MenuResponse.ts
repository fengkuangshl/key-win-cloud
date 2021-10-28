export interface MenuResponse {
    createTime: number
    css: string
    hidden: false
    id: string
    isMenu: number
    menuIds: string | null
    name: string
    parentId: string
    path: string
    roleId: string | null
    sort: number
    subMenus: Array<MenuResponse> | null
    updateTime: number
    url: string
}

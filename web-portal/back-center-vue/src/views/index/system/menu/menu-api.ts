import request from '@/fetch'
import { MenuResponse, MenuRole, RoleIdAndMenuIds } from './interface/menu-response'

export const CurrentMenuApi = (): Promise<KWResponse.Result<Array<MenuResponse>>> => request.get('api-user/menus/current/menus', {})
export const GetMenuByRoleIdApi = (roleId: string): Promise<KWResponse.Result<Array<MenuRole>>> => request.get('api-user/menus/' + roleId)
export const SaveMenuRoleApi = (roleIdAndMenuIds: RoleIdAndMenuIds): Promise<KWResponse.Result> => request.post('api-user/menus/granted', roleIdAndMenuIds)

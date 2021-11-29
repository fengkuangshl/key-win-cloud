import request from '@/fetch'
import { MenuForm, MenuResponse, MenuRole, Name, RoleIdAndMenuIds } from './interface/menu-response'

export const CurrentMenuApi = (): Promise<KWResponse.Result<Array<MenuResponse>>> => request.get('api-user/menus/current/menus', {})
export const GetMenuByRoleIdApi = (roleId: string): Promise<KWResponse.Result<Array<MenuRole>>> => request.get('api-user/menus/' + roleId)
export const SaveMenuRoleApi = (roleIdAndMenuIds: RoleIdAndMenuIds): Promise<KWResponse.Result> => request.post('api-user/menus/granted', roleIdAndMenuIds)
export const SysMenuPagedApi = (pageRequest: KWRequest.PageRequest<Name>): Promise<KWResponse.PageResult<MenuResponse>> => request.post('api-user/getSysMeunByPaged', pageRequest)
export const DeleteSysMenuApi = (id: string): Promise<KWResponse.Result> => request.delete('api-user/menus/' + id)
export const SysMuenSaveOrUpdateApi = (menuForm: MenuForm): Promise<KWResponse.Result> => request.post('api-user/menus/saveOrUpdate', menuForm)
export const GetMenuByIdApi = (id: string): Promise<KWResponse.Result<MenuResponse>> => request.get('api-user/menus/' + id)

import request from '@/fetch'
import settings from '@/settings'
import { MenuForm, MenuResponse, MenuRole, Name, RoleIdAndMenuIds } from './interface/sys-menu'

export const CurrentMenuApi = (): Promise<KWResponse.Result<Array<MenuResponse>>> => request.get(settings.apiUser + 'menu/current', {})
export const GetMenuByRoleIdApi = (roleId: string): Promise<KWResponse.Result<Array<MenuRole>>> => request.get(settings.apiUser + 'menu/' + roleId)
export const SaveMenuRoleApi = (roleIdAndMenuIds: RoleIdAndMenuIds): Promise<KWResponse.Result> => request.post(settings.apiUser + 'menu/granted', roleIdAndMenuIds)
export const SysMenuPagedApi = (pageRequest: KWRequest.PageRequest<Name>): Promise<KWResponse.PageResult<MenuResponse>> => request.post(settings.apiUser + 'menu/getSysMeunByPaged', pageRequest)
export const DeleteSysMenuApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiUser + 'menu/delete/' + id)
export const SysMuenSaveOrUpdateApi = (menuForm: MenuForm): Promise<KWResponse.Result> => request.post(settings.apiUser + 'menu/saveOrUpdate', menuForm)
export const GetMenuByIdApi = (id: string): Promise<KWResponse.Result<MenuResponse>> => request.get(settings.apiUser + 'menu/get/' + id)
export const GetOnesApi = (): Promise<KWResponse.Result<Array<MenuResponse>>> => request.get(settings.apiUser + 'menu/getOnes', {})

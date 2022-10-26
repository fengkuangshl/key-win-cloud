import request from '@/fetch'
import settings from '@/settings'
import { Name, PermissionEnum, PermissionForm, PermissionResponse } from './interface/sys-permission'
export const SysPermissionSaveOrUpdateApi = (permission: PermissionForm): Promise<KWResponse.Result> => request.post(settings.apiUser + 'permission/saveOrUpdate', permission)
export const DeleteSysPermissionApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiUser + 'permission/delete/' + id)
export const SysPermissionPagedApi = (pageRequest: KWRequest.PageRequest<Name>): Promise<KWResponse.PageResult<PermissionResponse>> => request.post(settings.apiUser + 'permission/findSysPermissionByPaged', pageRequest)
export const GetPermissionEnumApi = (): Promise<KWResponse.Result<Array<PermissionEnum>>> => request.get(settings.apiUser + 'permission/getPermissionEnum')

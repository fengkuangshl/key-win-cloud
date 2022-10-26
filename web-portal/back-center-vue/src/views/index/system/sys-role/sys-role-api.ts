import request from '@/fetch'
import settings from '@/settings'
import { SysRoleSearchRequest, SysRole, SysRoleForm } from './interface/sys-role'

export const SysRolePagedApi = (pageRequest: KWRequest.PageRequest<SysRoleSearchRequest>): Promise<KWResponse.PageResult<SysRole>> => request.post(settings.apiUser + 'role/getSysRolesByPaged', pageRequest)
export const SysRoleSaveOrUpdateApi = (role: SysRoleForm): Promise<KWResponse.Result> => request.post(settings.apiUser + 'role/saveOrUpdate', role)
export const DeleteSysRoleApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiUser + 'role/delete/' + id)
export const FindAllSysRoleApi = (): Promise<KWResponse.Result<Array<SysRole>>> => request.get(settings.apiUser + 'role/getRoleAll')

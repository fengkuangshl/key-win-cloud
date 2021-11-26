import request from '@/fetch'
import { SysRoleSearchRequest, SysRole, SysRoleForm } from './interface/sys-role'

export const SysRolePagedApi = (pageRequest: KWRequest.PageRequest<SysRoleSearchRequest>): Promise<KWResponse.PageResult<SysRole>> => request.post('api-user/getSysRolesByPaged', pageRequest)
export const SysRoleSaveOrUpdateApi = (role: SysRoleForm): Promise<KWResponse.Result> => request.post('api-user/roles/saveOrUpdate', role)
export const DeleteSysRoleApi = (id: string): Promise<KWResponse.Result> => request.delete('api-user/roles/' + id)
export const FindAllSysRoleApi = (): Promise<KWResponse.Result<Array<SysRole>>> => request.post('api-user/roles/findAllSysRole')

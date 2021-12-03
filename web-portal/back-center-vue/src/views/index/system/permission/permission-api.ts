import request from '@/fetch'
import { AuthIdsAndRoleId, Name, PermissionForm, PermissionResponse, PermissionRole } from './interface/sys-permission'

export const GetByPermissionRoleIdApi = (roleId: string): Promise<KWResponse.Result<Array<PermissionRole>>> => request.get('api-user/permissions/' + roleId)
export const SaveMenuPermissionApi = (authIdsAndRoleId: AuthIdsAndRoleId): Promise<KWResponse.Result> => request.post('api-user/permissions/granted', authIdsAndRoleId)
export const SysPermissionSaveOrUpdateApi = (permission: PermissionForm): Promise<KWResponse.Result> => request.post('api-user/permissions/saveOrUpdate', permission)
export const DeleteSysPermissionApi = (id: string): Promise<KWResponse.Result> => request.delete('api-user/permissions/' + id)
export const SysPermissionPagedApi = (pageRequest: KWRequest.PageRequest<Name>): Promise<KWResponse.PageResult<PermissionResponse>> => request.post('api-user/permissions/getSysPermissionByPaged', pageRequest)

import request from '@/fetch'
import { AuthIdsAndRoleId, PermissionRole } from './interface/permission-response'

export const GetByPermissionRoleIdApi = (roleId: string): Promise<KWResponse.Result<Array<PermissionRole>>> => request.get('api-user/permissions/' + roleId)
export const SaveMenuPermissionApi = (authIdsAndRoleId: AuthIdsAndRoleId): Promise<KWResponse.Result> => request.post('api-user/permissions/granted', authIdsAndRoleId)

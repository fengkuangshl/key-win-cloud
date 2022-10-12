import request from '@/fetch'
import settings from '@/settings'
import { RoleMenuPermissionForm, SysRoleMenuPermissionResponse } from './interface/sys-role-menu-permission'

export const SysRoleMenuPermissionSaveOrUpdateApi = (roleMenuPermissions: Array<RoleMenuPermissionForm>): Promise<KWResponse.Result> => request.post(settings.apiUser + 'sysrmpc/saveOrUpdateBatch', roleMenuPermissions)
export const GetPagePermissionApi = (roleId: number): Promise<KWResponse.Result<SysRoleMenuPermissionResponse>> => request.get(settings.apiUser + 'sysrmpc/get/grant/' + roleId)

import request from '@/fetch'
import settings from '@/settings'
import { MenuPermissionForm, SysMenuPermissionResponse } from './interface/sys-menu-permission'

export const SysMenuPermissionSaveOrUpdateApi = (menuPermissions: Array<MenuPermissionForm>): Promise<KWResponse.Result> => request.post(settings.apiUser + 'pagePermission/saveOrUpdateBatch', menuPermissions)
export const GetPagePermissionApi = (): Promise<KWResponse.Result<SysMenuPermissionResponse>> => request.get(settings.apiUser + 'pagePermission/get/page')

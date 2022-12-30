import request from '@/fetch'
import settings from '@/settings'
import { MenuPermissionForm, SysMenuPermissionResponse } from './interface/sys-menu-permission'

export const SysMenuPermissionSaveOrUpdateApi = (menuPermissions: Array<MenuPermissionForm>): Promise<KWResponse.Result> => {
  const config = {
    timeout: 30000
  }
  return request.post(settings.apiUser + 'pagePermission/saveOrUpdateBatch', menuPermissions, config)
}

export const GetPagePermissionApi = (): Promise<KWResponse.Result<SysMenuPermissionResponse>> => request.get(settings.apiUser + 'pagePermission/get/page')

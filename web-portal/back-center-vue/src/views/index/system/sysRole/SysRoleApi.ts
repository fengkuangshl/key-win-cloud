import request from '../../../../fetch'
import { SysRole } from './interface/SysRole'

export const SysRolePagedApi = (pageRequest: KWRequest.PageRequest<SysRole|undefined>): Promise<KWResponse.PageResult<SysRole>> => request.post('api-user/getSysRolesByPaged', pageRequest)

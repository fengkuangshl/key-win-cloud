import request from '@/fetch'

import settings from '@/settings'
import { SysGroupSearchRequest, SysGroup, SysGroupForm } from './interface/sys-group'

export const SysGroupPagedApi = (pageRequest: KWRequest.PageRequest<SysGroupSearchRequest>): Promise<KWResponse.PageResult<SysGroup>> => request.post(settings.apiUser + 'group/getSysGroupsByPaged', pageRequest)
export const SysGroupSaveOrUpdateApi = (group: SysGroupForm): Promise<KWResponse.Result> => request.post(settings.apiUser + 'group/saveOrUpdate', group)
export const DeleteSysGroupApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiUser + 'group/delete/' + id)
export const FindAllSysGroupApi = (): Promise<KWResponse.Result<Array<SysGroup>>> => request.get(settings.apiUser + 'group/getGroupAll')

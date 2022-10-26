import request from '@/fetch'
import settings from '@/settings'
import { SysDictTypeForm, SysDictType, SysDictTypeStatusChange } from './interface/dict-type'

export const SysDictTypePagedApi = (pageRequest: KWRequest.PageRequest<SysDictTypeForm>): Promise<KWResponse.PageResult<SysDictType>> => request.post(settings.apiParam + 'sysDictType/getSysDictTypeByPaged', pageRequest)
export const SysDictTypeSaveOrUpdateApi = (dictType: SysDictTypeForm): Promise<KWResponse.Result> => request.post(settings.apiParam + 'sysDictType/saveOrUpdate', dictType)
export const DeleteSysDictTypeApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiParam + 'sysDictType/delete/' + id)
export const SysDictTypeStatusChangeRequestApi = (param: SysDictTypeStatusChange): Promise<KWResponse.Result> => request.get(settings.apiParam + 'sysDictType/updateEnabled/' + param.id + '/' + param.status)
export const SysDictTypeGetApi = (id: string): Promise<KWResponse.Result<SysDictType>> => request.get(settings.apiParam + 'sysDictType/get/' + id)

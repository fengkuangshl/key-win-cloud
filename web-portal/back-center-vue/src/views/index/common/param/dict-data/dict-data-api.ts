import request from '@/fetch'
import settings from '@/settings'
import { SysDictDataForm, SysDictData, SysDictDataStatusChange } from './interface/dict-data'

export const SysDictDataPagedApi = (pageRequest: KWRequest.PageRequest<SysDictDataForm>): Promise<KWResponse.PageResult<SysDictData>> => request.post(settings.apiParam + 'sysDictData/getSysDictDataByPaged', pageRequest)
export const SysDictDataSaveOrUpdateApi = (dictData: SysDictDataForm): Promise<KWResponse.Result> => request.post(settings.apiParam + 'sysDictData/saveOrUpdate', dictData)
export const DeleteSysDictDataApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiParam + 'sysDictData/delete/' + id)
export const SysDictDataStatusChangeRequestApi = (param: SysDictDataStatusChange): Promise<KWResponse.Result> => request.get(settings.apiParam + 'sysDictData/updateEnabled/' + param.id + '/' + param.status)
export const SysDictDataGetApi = (id: string): Promise<KWResponse.Result<SysDictData>> => request.get(settings.apiParam + 'sysDictData/get/' + id)

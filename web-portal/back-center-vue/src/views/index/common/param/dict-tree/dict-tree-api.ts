import request from '@/fetch'
import settings from '@/settings'
import { SysDictTreeForm, SysDictTree, SysDictTreeStatusChange } from './interface/dict-tree'

export const SysDictTreePagedApi = (pageRequest: KWRequest.PageRequest<SysDictTreeForm>): Promise<KWResponse.PageResult<SysDictTree>> => request.post(settings.apiParam + 'sysDictTree/findSysDictTreeByPaged', pageRequest)
export const SysDictTreeSaveOrUpdateApi = (dictTree: SysDictTreeForm): Promise<KWResponse.Result> => request.post(settings.apiParam + 'sysDictTree/saveOrUpdate', dictTree)
export const DeleteSysDictTreeApi = (id: number): Promise<KWResponse.Result> => request.delete(settings.apiParam + 'sysDictTree/delete/' + id)
export const SysDictTreeStatusChangeRequestApi = (param: SysDictTreeStatusChange): Promise<KWResponse.Result> => request.get(settings.apiParam + 'sysDictTree/updateEnabled/' + param.id + '/' + param.status)
export const SysDictTreeGetApi = (id: number): Promise<KWResponse.Result<SysDictTree>> => request.get(settings.apiParam + 'sysDictTree/get/' + id)
export const GetSysDictTreeByTypeApi = (type: number): Promise<KWResponse.Result<Array<SysDictTree>>> => request.get(settings.apiParam + 'sysDictTree/getDictTree/' + type)

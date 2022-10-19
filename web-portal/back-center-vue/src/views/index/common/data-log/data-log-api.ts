import request from '@/fetch'
import settings from '@/settings'
import { DataLogDetail, DataLogSearchRequest } from './interface/data-log'

export const DataLogPagedApi = (pageRequest: KWRequest.PageRequest<DataLogSearchRequest>): Promise<KWResponse.PageResult<DataLogDetail>> => request.post(settings.apiLog + 'data/log/getDataLogByPaged', pageRequest)
export const DeleteDataLogApi = (id: number): Promise<KWResponse.Result> => request.delete(settings.apiLog + 'data/log/delete/' + id)
export const DataLogGetApi = (id: number): Promise<KWResponse.Result<DataLogDetail>> => request.get(settings.apiLog + 'data/log/get/' + id)

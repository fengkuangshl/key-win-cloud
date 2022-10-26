import request from '@/fetch'
import settings from '@/settings'
import { FileInfo, Name } from './interface/file'

export const FilePagedApi = (pageRequest: KWRequest.PageRequest<Name>): Promise<KWResponse.PageResult<FileInfo>> => request.post(settings.apiFile + 'file/getFileInfoByPaged', pageRequest)
export const FileUploadApi = (formData: FormData, bizType: string): Promise<KWResponse.Result<FileInfo>> => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }
  return request.post(settings.apiFile + 'upload/' + bizType + '/file', formData, config)
}
export const FileDeleteApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiFile + 'files/' + id)
export const FileGetApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiFile + 'files/' + id)

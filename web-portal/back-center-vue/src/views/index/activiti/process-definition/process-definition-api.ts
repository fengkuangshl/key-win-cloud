import request from '@/fetch'
import settings from '@/settings'
import { ProcessDefinitionDetail } from './interface/process-definition'

export const ProcessDefinitionPagedApi = (pageRequest: KWRequest.PageRequest<ProcessDefinitionDetail>): Promise<KWResponse.PageResult<ProcessDefinitionDetail>> => request.post(settings.apiActiviti + 'processDefinitionCtrl/getDefinitions', pageRequest)
export const ProcessDefinitionUploadApi = (formData: FormData): Promise<KWResponse.Result> => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }
  return request.post(settings.apiActiviti + 'processDefinitionCtrl/uploadStreamAndDeployment', formData, config)
}
export const DeleteProcessDefinitionApi = (id: string, deploymentId: string): Promise<KWResponse.Result> => request.delete(settings.apiActiviti + 'processDefinitionCtrl/delDefinition/' + id + '/' + deploymentId)
export const GetProcessDefinitionApi = (id: string): Promise<KWResponse.Result<ProcessDefinitionDetail>> => request.get(settings.apiActiviti + 'processDefinitionCtrl/get/' + id)

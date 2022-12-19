import request from '@/fetch'
import settings from '@/settings'
import { ProcessTaskForm, FromData, DynamicFromData } from './interface/todo-task'
export const CompleteProcessTaskGetApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processTaskCtrl/completeTask/' + id)
export const CompleteProcessTaskPostApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/completeTask', processTaskForm)
export const GetShowFormData = (id: string): Promise<KWResponse.Result<Array<FromData> | string>> => request.get(settings.apiActiviti + 'processTaskCtrl/formDataShow/' + id)
export const SaveFormData = (fromData: DynamicFromData): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/formDataSave', fromData)
export const TrunTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/trunTask', processTaskForm)
export const GiveBackTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/giveBackTask', processTaskForm)
export const GetPreOneIncomeNodeApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/getPreOneIncomeNode', processTaskForm)
export const GetRevocationApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/revocation', processTaskForm)
export const GetHandleCancellationApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/handleCancellation', processTaskForm)

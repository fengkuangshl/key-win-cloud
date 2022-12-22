import request from '@/fetch'
import settings from '@/settings'
import { ProcessTaskForm, FromData, DynamicFromData, FromDataDetail } from './interface/todo-task'
export const CompleteProcessTaskGetApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processTaskCtrl/completeTask/' + id)
export const CompleteProcessTaskPostApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/completeTask', processTaskForm)
export const TrunTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/trunTask', processTaskForm)
export const GiveBackTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/giveBackTask', processTaskForm)
export const GetPreOneIncomeNodeApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/getPreOneIncomeNode', processTaskForm)
export const GetRevocationApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/revocation', processTaskForm)
export const GetHandleCancellationApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/handleCancellation', processTaskForm)
export const GetShowFormData = (id: string): Promise<KWResponse.Result<Array<FromData> | string>> => request.get(settings.apiActiviti + 'dynamicFormCtrl/formDataShow/' + id)
export const SaveFormData = (fromData: DynamicFromData): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'dynamicFormCtrl/formDataSave', fromData)
export const GetApprovalHistoryList = (instanceId: string): Promise<KWResponse.Result<Array<FromDataDetail>>> => request.get(settings.apiActiviti + 'dynamicFormCtrl/getFormData/' + instanceId)

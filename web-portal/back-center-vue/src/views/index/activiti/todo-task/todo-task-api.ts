import request from '@/fetch'
import settings from '@/settings'
import { ProcessTaskForm, FromData, SubmitFormData } from './interface/todo-task'
export const CompleteProcessTaskGetApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processTaskCtrl/completeTask/' + id)
export const CompleteProcessTaskPostApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/completeTask', processTaskForm)
export const GetShowFormData = (id: string): Promise<KWResponse.Result<Array<FromData> | string>> => request.get(settings.apiActiviti + 'processTaskCtrl/formDataShow/' + id)
export const SaveFormData = (fromData: SubmitFormData): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/formDataSave/', fromData)
export const TrunTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/trunTask', processTaskForm)
export const GiveBackTaskApi = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/giveBackTask', processTaskForm)
export const GetPreOneIncomeNode = (processTaskForm: ProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskActionController/getPreOneIncomeNode', processTaskForm)

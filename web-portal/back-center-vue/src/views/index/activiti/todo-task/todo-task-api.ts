import request from '@/fetch'
import settings from '@/settings'
import { CompleteProcessTaskForm } from './interface/todo-task'
export const CompleteProcessTaskGetApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processTaskCtrl/completeTask/' + id)
export const CompleteProcessTaskPostApi = (completeProcessTaskForm: CompleteProcessTaskForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processTaskCtrl/completeTask', completeProcessTaskForm)
export const GetFormDataShow = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processTaskCtrl/formDataShow/' + id)

import request from '@/fetch'
import settings from '@/settings'
import { ProcessInstanceDetail, ProcessInstanceForm } from './interface/process-instance'
export const StartProcessInstanceApi = (processInstanceForm: ProcessInstanceForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processInstanceCtrl/startProcess', processInstanceForm)
export const DeleteProcessInstanceApi = (id: string): Promise<KWResponse.Result> => request.delete(settings.apiActiviti + 'processInstanceCtrl/deleteInstance/' + id)
export const SuspendProcessInstanceApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processInstanceCtrl/suspendInstance/' + id)
export const ResumeProcessInstanceApi = (id: string): Promise<KWResponse.Result> => request.get(settings.apiActiviti + 'processInstanceCtrl/resumeInstance/' + id)
export const GetProcessInstanceApi = (id: string): Promise<KWResponse.Result<ProcessInstanceDetail>> => request.get(settings.apiActiviti + 'processInstanceCtrl/get/' + id)

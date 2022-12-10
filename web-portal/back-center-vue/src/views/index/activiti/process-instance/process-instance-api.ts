import request from '@/fetch'
import settings from '@/settings'
import { ProcessInstanceForm } from './interface/process-instance'
export const StartProcessInstanceApi = (processInstanceForm: ProcessInstanceForm): Promise<KWResponse.Result> => request.post(settings.apiActiviti + 'processInstanceCtrl/startProcess', processInstanceForm)

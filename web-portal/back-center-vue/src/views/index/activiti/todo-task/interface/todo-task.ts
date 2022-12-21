export type Name = Model.Name

export interface ProcessTaskForm {
  taskId: string
  audit?: string
  assignee?: string
  processInstanceId?: string
}

export interface ProcessTaskDetail extends Model.Id, Model.Name {
  status: string
  assignee: string
  instanceName: string
  instanceId: string
  createTime: Date
  claimTime: Date
}

export interface FromData {
  procDefId: string
  procInstId: string
  procTaskId: string
  controlType: string
  formKey: string
  controlId: string
  controlLabel: string
  controlValue: string
  controlValueParamType: string
  controlIsReadOnly?: boolean
}

export interface FromDataDetail extends Model.BaseField, FromData {}

export interface DynamicFromData {
  taskId: string
  formData: Array<FromData>
}

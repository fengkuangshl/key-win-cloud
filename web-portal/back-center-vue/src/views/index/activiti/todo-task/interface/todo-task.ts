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

export interface FromData extends Model.Id {
  controlType: string
  controlLabel: string
  controlDefValue: string
  controlIsParam: string
}

export interface DynamicFromData {
  taskId: string
  formData: Array<string>
}

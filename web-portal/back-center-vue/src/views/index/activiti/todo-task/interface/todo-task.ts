export type Name = Model.Name

export interface CompleteProcessTaskForm extends Model.Id {
  audit: string
}

export interface ProcessTaskDetail extends Model.Id, Model.Name {
  status: string
  assignee: string
  instanceName: string
  createTime: Date
}

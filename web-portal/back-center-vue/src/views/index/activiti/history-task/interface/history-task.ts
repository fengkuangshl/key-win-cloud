export type Name = Model.Name
export interface HistoryTaskDetail extends Model.Id, Model.Name {
  assignee: string
  resourceName: string
  taskDefinitionKey: string
  processInstanceId: string
  processInstanceName: string
  processDefinitionId: string
  createTime: Date
  startTime: Date
  isRecover: boolean
  isAbandon: boolean
}

export type Name = Model.Name
export interface HistoryTaskDetail extends Model.Id, Model.Name {
  assignee: string
  resourceName: string
  taskDefinitionKey: string
  processInstanceId: string
  createTime: Date
  startTime: Date
}

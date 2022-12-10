export interface ProcessInstanceBase extends Model.Name {
  processDefinitionId: string
  processDefinitionKey: string
}

export interface ProcessInstanceForm extends ProcessInstanceBase {
  variables: Map<string, string>
  variable: string
  businessKey: string
}

export interface ProcessInstanceDetail extends Model.Id, Model.Version {
  resourceName: string
  deploymentId: string
  processDefinitionVersion: number
  startTime: Date
  status: string
}

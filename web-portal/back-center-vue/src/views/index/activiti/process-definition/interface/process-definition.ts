export interface ProcessDefinitionDetail extends Model.Id, Model.Name, Model.Version {
  deploymentId: string
  resourceName: string
  key: string
}

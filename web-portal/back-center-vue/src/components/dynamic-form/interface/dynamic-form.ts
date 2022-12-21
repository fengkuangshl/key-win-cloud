export interface DynamicPickerDate {
  type: string
  formatValue: string
}

export interface DynamicOptions {
  label: string
  value: string
  name?: string
}

export interface DynamicFormItem {
  label: string
  type: string
  model: string
  isParam: string
  isReadOnly?: boolean
  pickerDate?: DynamicPickerDate
  isFun?: boolean
  opts?: Array<DynamicOptions>
}

export interface DynamicFormRule {
  [x: string]: Array<KWRule.Rule>
}

export interface DynamicInputFormData {
  [x: string]: string | number | Date | boolean | null | undefined
}

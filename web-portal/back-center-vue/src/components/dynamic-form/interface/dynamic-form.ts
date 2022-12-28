export type EvnetFn = (val: string) => void

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
  label: string // 控件名称
  type: string // 控件类型
  model: string // 控件id
  isParam: string // 是否作为其它控件的参数
  isShowControl: boolean // 是否控件展示控件
  eventType?: string
  eventFn?: EvnetFn
  isReadOnly?: boolean // 控件是否只读
  pickerDate?: DynamicPickerDate // 日期控件
  isFun?: boolean
  opts?: Array<DynamicOptions> // checkbox radio select 的选项
}

export interface DynamicFormRule {
  [x: string]: Array<KWRule.Rule>
}

export interface DynamicInputFormData {
  [x: string]: string | number | Date | boolean | null | undefined
}

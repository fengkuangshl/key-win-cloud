
<template>
  <div>
    <el-form id="dynamicForm" :model="dynamicFormData" ref="dynamicFormRef" :inline='false' label-width="auto" class="dynamicForm">
      <template v-for="(item,index) in formItems">
        <el-form-item :key="index" v-show="item.isShowControl" :label="item.label+'：'" :prop="item.model" :rules="item.rule ? item.rule : {}">
          <el-input v-if="item.type == null || item.type == undefined || item.type == 'text' " :disabled="item.isReadOnly" type="text" :placeholder="'请填写'+item.label" @change="onAction(dynamicFormData[item.model])" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-input>

          <el-input v-else-if="item.type == 'number'" :placeholder="'请填写'+item.label" type="number" :disabled="item.isReadOnly" v-model="dynamicFormData[item.model]" @change="onAction(dynamicFormData[item.model])" style="max-width: 220px;"></el-input>

          <el-input v-else-if="item.type == 'textarea'" :placeholder="'请填写'+item.label" type="textarea" :disabled="item.isReadOnly" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-input>

          <el-select v-else-if="item.type == 'select'" filterable :placeholder="'请选择'+item.label" :disabled="item.isReadOnly" v-model="dynamicFormData[item.model]" @change="item.isFun ? onAction(dynamicFormData[item.model]) : ''" style="max-width: 220px;">
            <el-option v-for="(it,index) in item.opts" :key="index" :label="it.label" :value="it.value"></el-option>
          </el-select>

          <el-switch v-else-if="item.type == 'switch'" v-model="dynamicFormData[item.model]" :disabled="item.isReadOnly" style="max-width: 220px;"></el-switch>

          <el-radio-group v-else-if="item.type == 'radio'" @[item.eventType]="item.eventFn" v-model="dynamicFormData[item.model]" :disabled="item.isReadOnly">
            <el-radio v-for="(it,index) in item.opts" :key="index" :label="it.value">{{it.label}}</el-radio>
          </el-radio-group>

          <el-checkbox-group v-else-if="item.type == 'checkbox'" v-model="dynamicFormData[item.model]" :disabled="item.isReadOnly">
            <el-checkbox v-for="(it,index) in item.opts" :key="index" :label="it.value" :name='it.name'>{{it.label}}</el-checkbox>
          </el-checkbox-group>

          <el-date-picker v-else-if="item.type == 'date'" :disabled="item.isReadOnly" :start-placeholder="item.pickerDate.type == 'daterange'?'开始日期':''" :end-placeholder="item.pickerDate.type == 'daterange'?'结束日期':''" style="max-width: 220px;" :value-format='item.pickerDate.formatValue' v-model="dynamicFormData[item.model]" :type="item.pickerDate.type" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Prop, Vue, Ref, Watch } from 'vue-property-decorator'
import { DynamicFormItem, DynamicInputFormData } from './interface/dynamic-form'
@Component({
  components: {}
})
export default class KWDynamicForm extends Vue {
  @Prop({ default: [], type: Array })
  formItems!: Array<DynamicFormItem>

  @Prop({ default: {}, type: Object })
  inputFormData!: DynamicInputFormData

  dynamicFormData: DynamicInputFormData = {}

  onAction(val: string): void {
    console.log(val)
  }

  @Ref('dynamicFormRef')
  readonly dynamicFormRef!: ElForm

  @Watch('inputFormData')
  onInputFormData(): void {
    this.dynamicFormData = this.inputFormData
  }

  created(): void {
    this.dynamicFormData = this.inputFormData
  }
}
</script>

<style lang="less" scoped >
</style>

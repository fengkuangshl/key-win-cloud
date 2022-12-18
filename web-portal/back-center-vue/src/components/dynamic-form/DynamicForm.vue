
<template>
  <div>
    <el-form :model="dynamicFormData" ref="dynamicFormRef" :inline='false' :rules="dynamicFormRules" label-width="auto" class="dynamicForm">
      <template v-for="(item,index) in formItems">
        <el-form-item :key="index" v-show="item.type !== 'hidden'" :label="item.label+'：'" :prop="item.model">
          <el-input v-if="item.type == null || item.type == undefined || item.type == 'text' " type="text" :placeholder="'请填写'+item.label" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-input>

          <el-input v-else-if="item.type == 'number'" :placeholder="'请填写'+item.label" type="number" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-input>

          <el-input v-else-if="item.type == 'textarea'" :placeholder="'请填写'+item.label" type="textarea" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-input>

          <el-select v-else-if="item.type == 'select'" filterable :placeholder="'请选择'+item.label" v-model="dynamicFormData[item.model]" @change="item.isFun ? onAction(dynamicFormData[item.model]) : ''" style="max-width: 220px;">
            <el-option v-for="(it,index) in item.opts" :key="index" :label="it.label" :value="it.value"></el-option>
          </el-select>

          <el-switch v-else-if="item.type == 'switch'" v-model="dynamicFormData[item.model]" style="max-width: 220px;"></el-switch>

          <el-radio-group v-else-if="item.type == 'radio'" v-model="dynamicFormData[item.model]">
            <el-radio v-for="(it,index) in item.opts" :key="index" :label="it.value">{{it.label}}</el-radio>
          </el-radio-group>

          <el-checkbox-group v-else-if="item.type == 'checkbox'" v-model="dynamicFormData[item.model]">
            {{dynamicFormData[item.model]}}
            <el-checkbox v-for="(it,index) in item.opts" :key="index" :label="it.value" :name='it.name'>{{it.label}}</el-checkbox>
          </el-checkbox-group>

          <el-date-picker v-else-if="item.type == 'date'" :start-placeholder="item.pickerDate.type == 'daterange'?'开始日期':''" :end-placeholder="item.pickerDate.type == 'daterange'?'结束日期':''" style="max-width: 220px;" :value-format='item.pickerDate.formatValue' v-model="dynamicFormData[item.model]" :type="item.pickerDate.type" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Prop, Vue, Ref } from 'vue-property-decorator'
import { DynamicFormItem, DynamicFormRule, DynamicInputFormData } from './interface/dynamic-form'
@Component({
  components: {}
})
export default class KWDynamicForm extends Vue {
  @Prop({ default: [], type: Array })
  formItems!: Array<DynamicFormItem>

  @Prop({ default: {}, type: Object })
  inputFormData!: DynamicInputFormData

  @Prop({ default: {}, type: Object })
  dynamicFormRules!: DynamicFormRule

  dynamicFormData: DynamicInputFormData = {}

  onAction(val: string): void {
    console.log(val)
  }

  @Ref('dynamicFormRef')
  readonly dynamicFormRef!: ElForm

  created(): void {
    this.dynamicFormData = this.inputFormData
  }
}
</script>

<style lang="less" scoped >
</style>

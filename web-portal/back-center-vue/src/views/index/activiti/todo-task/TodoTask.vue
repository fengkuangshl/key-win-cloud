<template>
  <div id="todoTaskMain" style="height: 100%">
    <div class="navigation-breadcrumb">
      <div>待办任务</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>待办任务</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入实例名称" v-model="t.name" v-hasPermissionQueryPage="processTaskPermissionPrefix">
            <el-button slot="append" class="search-primary" icon="el-icon-search" @click="searchProcessTask"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <KWTable url="/api-activiti/processTaskCtrl/getTasks" v-hasPermissionQueryPage="processTaskPermissionPrefix" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="instanceName" sortable="custom" label="流程名称"> </el-table-column>
        <el-table-column prop="name" label="任务节点名称" sortable="custom"></el-table-column>
        <el-table-column prop="status" label="状态" sortable="custom" :formatter="
            row => {
              if (row.status && row.status == 'ASSIGNED') {
                    return '执行中';
                } else if (row.status && row.status == 'CREATED') {
                    return '创建';
                } else if (row.status && row.status == 'SUSPENDED') {
                    return '挂起';
                } else if (row.status && row.status == 'COMPLETED') {
                    return '完成';
                } else if (row.status && row.status == 'CANCELLED') {
                    return '取消';
                } else if (row.status && row.status == 'DELETED') {
                    return '删除';
                }else if (row.status && row.status == 'RUNNING') {
                    return '运行';
                } else {
                    return d.status
                }
            }
          "></el-table-column>
        <el-table-column prop="assignee" label="办理人" sortable="custom" :formatter="
            row => {
              if (row.assignee) {
                    return row.assignee;
                } else {
                    return '待拾取任务';
                }
            }
          "></el-table-column>
        <el-table-column prop="createTime" label="创建时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.createTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column width="230" label="操作">
          <template v-slot="scope">
            <el-tooltip effect="dark" content="办理" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED'" icon="el-icon-finished" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="processTask(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="当前流程已被挂起，不能办理" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status == 'SUSPENDED'" icon="el-icon-video-play" :disabled=true v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :visible.sync="dialogDynamicFormVisible" title="审批表单" class="dynamicForm" style="height:auto;" :before-close="()=>{dialogDynamicFormVisible = false}" width="25%">
      <el-tabs v-model="activeTabName">
        <el-tab-pane name="approvalPage">
          <span slot="label"><i class="el-icon-s-check"></i>审批</span>
          <div style="height:auto;">
            <KWDynamicForm :formItems='dynamicFormItems' :dynamicFormRules='dynamicRules' ref='dynamicForm' :inputFormData='dynamicInputFormData'></KWDynamicForm>
            <div style="padding:0px 10px 20px 0px;float:right">
              <el-button @click="dialogDynamicFormVisible = false">取消</el-button>
              <el-button v-if="approvalTaskBtn" type="primary" @click="approvalTask()">审批</el-button>
              <el-button v-if="trunTaskBtn" type="primary" @click="transferTask()">转签</el-button>
              <el-button v-if="delegateTaskBtn" type="primary" @click="delegateTask()">委派</el-button>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane name="approvalHistoryListPage">
          <span slot="label"><i class="el-icon-s-operation"></i>审批历史</span>
          <el-scrollbar>
            <div class="block" style="margin: 0px 1px;max-height:400px;">
              <el-timeline>
                <el-timeline-item v-for="(value,key) in approvalHistoryMap" :key="key" :timestamp="value[0]" placement="top">
                  <el-card class="box-card" style="box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%) !important;border: 1px solid #EBEEF5 !important;">
                    <template v-for="(item,index) in value[1]">
                      <div :key="index">
                        <h4 v-if="index===0" style="padding:5px">{{item.createUserName+' ' + (key ===0 ? '发起':'审批')}}&nbsp;&nbsp;&nbsp;&nbsp;<a style="color:#409EFF;cursor: pointer;" title="查看流程图" @click="showProcessInstance(item)"><i class="el-icon-view"></i></a></h4>
                        <p class="el-form-item-div" v-if="index!==0 || key !==0" style="padding:5px">{{item.controlLabel+'：'+item.controlValue}}</p>
                      </div>
                    </template>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-scrollbar>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <KWBpmnJsIframe :showBpmn="showBpmn" :instanceId="instanceId" :deploymentFileUUID="deploymentFileUUID" :type="'lookBpmn'" :deploymentName="deploymentName"></KWBpmnJsIframe>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import KWTable from '@/components/table/Table.vue'
import KWDynamicForm from '@/components/dynamic-form/DynamicForm.vue'
import KWBpmnJsIframe from '@/components/bpmn-js/BpmnJsIframe.vue'
import PermissionUtil from '@/common/utils/permission/permission-util'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { Name, ProcessTaskDetail, FromDataDetail, DynamicFromData, ProcessTaskForm, FromData } from './interface/todo-task'
import { GetShowFormData, TrunTaskApi, DelegateTaskApi, SaveFormData, CompleteProcessTaskPostApi, GetApprovalHistoryList } from './todo-task-api'
import { DynamicFormItem, DynamicFormRule, DynamicInputFormData, DynamicOptions, EvnetFn } from '@/components/dynamic-form/interface/dynamic-form'
import { GetUserAllApi } from '../../system/user/user-api'
import { MessageBoxData, MessageBoxInputData } from 'node_modules/_element-ui@2.15.10@element-ui/types/message-box'
import dateFormat from '@/common/utils/date-util/date-format'
import { GetProcessDefinitionApi } from '../process-definition/process-definition-api'
@Component({
  components: {
    KWTable,
    KWDynamicForm,
    KWBpmnJsIframe
  }
})
export default class TodoTask extends Vue {
  t: Name = {
    name: ''
  }

  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''
  instanceId = ''
  dialogDynamicFormVisible = false
  processTaskPermissionPrefix = PermissionPrefixUtils.processTask
  activeTabName = 'approvalPage'
  approvalTaskBtn = true
  trunTaskBtn = false
  delegateTaskBtn = false
  controlEventType = ''

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, ProcessTaskDetail>

  @Ref('dynamicForm')
  readonly dynamicForm!: KWDynamicForm

  dynamicFormItems: Array<DynamicFormItem> = new Array<DynamicFormItem>()

  dynamicInputFormData: DynamicInputFormData = {}

  users: Array<{ label: string; value: string }> = []
  approvalHistoryMap: Map<string, Array<FromDataDetail>> = new Map()

  hasPermissionEnabled(): boolean {
    return PermissionUtil.hasPermissionForEnabled(this.processTaskPermissionPrefix)
  }

  hasPermission(data: ProcessTaskDetail): boolean {
    return true
  }

  async processTask(processTaskDetail: ProcessTaskDetail): Promise<void> {
    if (this.users.length === 0) {
      this.getUserList()
    } else {
      console.log('获取用户列表已加载过，不需要再次加载!')
    }
    const { code, msg, data } = await GetShowFormData(processTaskDetail.id)
    if (code === 200) {
      if (data === 'non-from') {
        this.doTask(processTaskDetail.id)
      } else {
        this.renderDynamicForm(data as Array<FromDataDetail>, processTaskDetail)
        this.dialogDynamicFormVisible = true
      }
    } else {
      this.$message.error(msg || '获取代办数据失败!')
    }
    if (this.approvalHistoryMap.size === 0) {
      this.getApprovalHistoryList(processTaskDetail.instanceId)
    } else {
      console.log('获取审批列表已加载过，不需要再次加载!')
    }
  }

  async getApprovalHistoryList(instanceId: string): Promise<void> {
    const { code, msg, data } = await GetApprovalHistoryList(instanceId)
    if (code === 200) {
      data.forEach(item => {
        const key = dateFormat(item.createDate) // + '由用户[' + item.createUserName + ']' // + '::' + (this.approvalHistoryMap.size === 0 ? '发起' : '审批')
        let fromDataDetails: Array<FromDataDetail> = this.approvalHistoryMap.get(key) as Array<FromDataDetail>
        if (fromDataDetails === undefined) {
          fromDataDetails = []
          this.approvalHistoryMap.set(key, fromDataDetails)
          // fromDataDetails = this.approvalHistoryMap.get(key) as Array<FromDataDetail>
        }
        if (item.controlValueOptions !== undefined && item.controlValueOptions !== '') {
          const opts: Array<DynamicOptions> = JSON.parse(item.controlValueOptions as string)
          for (const key in opts) {
            if (Object.prototype.hasOwnProperty.call(opts, key)) {
              const element = opts[key]
              if (element.value === item.controlValue) {
                item.controlValue = element.label
                break
              }
            }
          }
          // const kv: Map<string, string> = new Map()
          // opts.forEach(opt => {
          //   kv.set(opt.value, opt.label)
          // })
          // item.controlValue = kv.get(item.controlValue) as string
        }
        fromDataDetails.push(item)
      })
      console.log(this.approvalHistoryMap)
    } else {
      this.$message.error(msg || '获取审批列表失败!')
    }
  }

  renderDynamicForm(datas: Array<FromDataDetail>, processTaskDetail: ProcessTaskDetail): void {
    this.dynamicFormItems = []
    this.dynamicInputFormData = {}
    if (datas && datas.length > 0) {
      datas.forEach(item => {
        let type = item.controlType
        let triggerType = 'blur'
        switch (item.controlType) {
          case 'long':
            type = 'number'
            break
          case 'user_list':
            type = 'select'
            triggerType = 'change'
            item.controlValueOptions = JSON.stringify(this.users)
            break
          case 'string':
            type = 'text'
            break
          case 'date':
            break
          case 'checkbox':
            triggerType = 'change'
            break
          case 'radio':
            triggerType = 'change'
            break
        }
        let eventType = 'change'
        let fun: EvnetFn = val => console.log(val)
        if (item.controlEvent) {
          const et: { eventType: string; eventFn: string } = JSON.parse(item.controlEvent as string)
          eventType = et.eventType
          var Fun = Function // 一个变量指向Function，防止有些前端编译工具报错
          /**
           * '(val)=>{ var vueDiv = document.querySelector("#dynamicForm").parentElement.__vue__; debugger; console.log(vueDiv.formItems);}'
           * 此段代码是为在普通的js获取当前页面获取dynamicForm的Vue对象，以便获取dynamicForm的属性，对其属性操作
           * 这个段动态表单的事件里的代码
           */
          fun = new Fun('return ' + et.eventFn)()
        }
        if (item.controlValue !== '无') {
          this.$set(this.dynamicInputFormData, item.controlId, item.controlValue)
        } else {
          this.$set(this.dynamicInputFormData, item.controlId, '')
        }
        let rule = { required: false, message: '', trigger: triggerType }
        if (item.controlValueValidate) {
          if (item.controlValueValidate === 'false' || item.controlValueValidate === '无') {
            console.log('什么也不做')
          } else if (item.controlValueValidate === 'true') {
            rule = { required: true, message: '不能为空!', trigger: triggerType }
          } else {
            var FunRule = Function // 一个变量指向Function，防止有些前端编译工具报错
            // rule = eval('(' + item.controlValueValidate + ')')
            rule = new FunRule('return ' + item.controlValueValidate)()
          }
        }

        this.dynamicFormItems.push({
          label: item.controlLabel,
          type: type,
          originType: item.controlType,
          model: item.controlId,
          isShowControl: item.isShowControl,
          isReadOnly: item.controlIsReadOnly,
          isParam: item.controlValueParamType,
          eventType: eventType,
          eventFn: fun,
          rule: rule,
          pickerDate: type === 'date' && item.controlValueOptions !== undefined ? JSON.parse(item.controlValueOptions as string) : undefined,
          opts: type !== 'date' && item.controlValueOptions !== undefined ? JSON.parse(item.controlValueOptions as string) : undefined
        })
      })
      this.dynamicFormItems.push({
        label: '',
        type: 'hidden',
        model: 'taskId',
        isParam: '',
        originType: '',
        isShowControl: false
      })
      this.$set(this.dynamicInputFormData, 'taskId', processTaskDetail.id)
    }
  }

  async getUserList(): Promise<void> {
    const { code, msg, data } = await GetUserAllApi()
    if (code !== 200) {
      this.$message.error(msg || '获取用户列表失败!')
    } else {
      data.forEach(user => {
        this.users.push({ label: user.nickname, value: user.username })
      })
    }
  }

  doTask(id: string): void {
    this.$prompt('审批内容', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'textarea'
    })
      .then(async (messageBoxInputData: MessageBoxData) => {
        messageBoxInputData = messageBoxInputData as MessageBoxInputData
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: messageBoxInputData.value,
          taskId: id
        }
        const { code, msg } = await CompleteProcessTaskPostApi(completeProcessTaskForm)
        if (code !== 200) {
          this.$message.error(msg || '审批失败!')
        } else {
          this.searchProcessTask()
          this.$message.success('审批成功!')
        }
      })
      .catch(e => {
        console.log(e)
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
  }

  searchProcessTask(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  getFormItemsToMap(): Map<string, DynamicFormItem> {
    const map = new Map<string, DynamicFormItem>()
    this.dynamicFormItems.forEach(item => {
      map.set(item.model, item)
    })
    return map
  }

  approvalTask(): void {
    console.log('getDynamicFormDatas...')
    this.dynamicForm.dynamicFormRef.validate(async valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      const map = this.getFormItemsToMap()
      const formDatas = this.dynamicForm.dynamicFormData
      const param: Array<FromData> = []
      for (const key in formDatas) {
        if (key === 'taskId') {
          continue
        }
        const formItem = map.get(key) as DynamicFormItem
        const val = formDatas[key]
        let eventFn = ''
        if (formItem.eventFn !== undefined) {
          eventFn = formItem.eventFn?.toString()
        }
        const formData: FromData = {
          procDefId: '',
          procInstId: '',
          procTaskId: '',
          controlType: formItem.originType,
          formKey: '',
          controlId: key,
          controlLabel: formItem.label,
          controlValue: val as string,
          controlValueValidate: formItem.rule !== undefined ? JSON.stringify(formItem.rule) : '',
          controlValueParamType: formItem.isParam,
          controlValueOptions: formItem.opts !== undefined ? JSON.stringify(formItem.opts) : '',
          controlIsReadOnly: formItem.isReadOnly,
          isShowControl: formItem.isShowControl,
          controlEventType: this.controlEventType,
          controlEvent: eventFn
        }
        param.push(formData)
      }
      console.log('param:{}', param)
      const formData: DynamicFromData = {
        taskId: formDatas.taskId as string,
        formData: param
      }
      const { code, msg } = await SaveFormData(formData)
      if (code !== 200) {
        this.$message.error(msg || '审批失败!')
      } else {
        this.searchProcessTask()
        this.dialogDynamicFormVisible = false
        this.$message.success('审批成功!')
      }
    })
  }

  async showProcessInstance(fromDataDetail: FromDataDetail): Promise<void> {
    const { code, msg, data } = await GetProcessDefinitionApi(fromDataDetail.procDefId)
    if (code !== 200) {
      this.$message.error(msg || '获取流程实例信息失败!')
    } else {
      this.deploymentFileUUID = data.deploymentId
      this.deploymentName = data.resourceName
      this.instanceId = fromDataDetail.procInstId
      this.showBpmn = true
      setTimeout(() => {
        this.showBpmn = false
      }, 1000)
    }
  }

  transferTask(): void {
    this.tdTask('转签')
  }

  delegateTask(): void {
    this.tdTask('委派')
  }

  tdTask(tipInfo: string): void {
    this.dynamicForm.dynamicFormRef.validate(async valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      const map = this.getFormItemsToMap()
      const formDatas = this.dynamicForm.dynamicFormData
      const param: Array<FromData> = []
      for (const key in formDatas) {
        if (key === 'taskId') {
          continue
        }
        const formItem = map.get(key) as DynamicFormItem
        const val = formDatas[key]
        if (formItem.originType === 'user_list' && val === '') {
          this.$message.error('请选择用户!')
          return false
        }
        let eventFn = ''
        if (formItem.eventFn !== undefined) {
          eventFn = formItem.eventFn?.toString()
        }
        const formData: FromData = {
          procDefId: '',
          procInstId: '',
          procTaskId: '',
          controlType: formItem.originType,
          formKey: '',
          controlId: key,
          controlLabel: formItem.label,
          controlValue: val as string,
          controlValueValidate: formItem.rule !== undefined ? JSON.stringify(formItem.rule) : '',
          controlValueParamType: formItem.isParam,
          controlValueOptions: formItem.opts !== undefined ? JSON.stringify(formItem.opts) : '',
          controlIsReadOnly: formItem.isReadOnly,
          isShowControl: formItem.isShowControl,
          controlEventType: this.controlEventType,
          controlEvent: eventFn
        }
        param.push(formData)
      }
      console.log('param:{}', param)
      const formData: DynamicFromData = {
        taskId: formDatas.taskId as string,
        formData: param
      }
      const { code, msg } = tipInfo === '转签' ? await TrunTaskApi(formData) : await DelegateTaskApi(formData)
      if (code !== 200) {
        this.$message.error(msg || '任务' + tipInfo + '失败!')
      } else {
        this.searchProcessTask()
        this.dialogDynamicFormVisible = false
        this.$message.success('任务' + tipInfo + '成功!')
      }
    })
  }
}
</script>

<style lang="less"   >
.search-primary {
  background: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
}
.search-primary:focus,
.search-primary:hover {
  background: #66b1ff !important;
  border-color: #66b1ff !important;
  color: #fff !important;
}
.transform {
  transform: rotate(180deg);
}
.dynamicForm {
  // .el-timeline-item__timestamp.is-top {
  //   margin-bottom: 0px;
  //   padding-top: 0px;
  // }
  // .el-timeline-item {
  //   position: relative;
  //   padding-bottom: 0px;
  // }

  .el-dialog__body {
    padding: 0px 30px;
    color: #606266;
    font-size: 14px;
    word-break: break-all;
  }
  .el-card__body {
    padding: 10px 20px;
  }
  .box-card {
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%) !important;
  }
}
</style>

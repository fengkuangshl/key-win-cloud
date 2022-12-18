<template>
  <div style="height: 100%">
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
            <el-tooltip effect="dark" content="转办" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED'" icon="el-icon-share" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="transferTask(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="退还" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED' && scope.row.claimTime !== undefined  && scope.row.claimTime !== null " icon="el-icon-s-operation" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="giveBackTask(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="驳回" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED'" icon="el-icon-back" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="getPreOneIncomeNode(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="申请作废" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED'" icon="el-icon-s-release" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="handleCancellation(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :visible.sync="dialogDynamicFormVisible" title="动态表单" style="height:auto;" :before-close="()=>{dialogDynamicFormVisible = false}" width="20%">
      <div style="height:auto;">
        <KWDynamicForm :formItems='formItems' :dynamicFormRules='rules' ref='dynamicForm' :inputFormData='inputFormData'></KWDynamicForm>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogDynamicFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="getDynamicFormDatas()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogTransferFormVisible" title="转办表单" style="height:auto;" :before-close="()=>{dialogTransferFormVisible = false}" width="20%">
      <div style="height:auto;">
        <KWDynamicForm :formItems='transferFormItems' :dynamicFormRules='transferRules' ref='transferForm' :inputFormData='transferInputFormData'></KWDynamicForm>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTransferFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="getTransferFormDatas()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogGiveBackFormVisible" title="退还表单" style="height:auto;" :before-close="()=>{dialogGiveBackFormVisible = false}" width="20%">
      <div style="height:auto;">
        <KWDynamicForm :formItems='giveBackFormItems' :dynamicFormRules='giveBackRules' ref='giveBackForm' :inputFormData='giveBackInputFormData'></KWDynamicForm>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogGiveBackFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="getGiveBackFormDatas()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import KWTable from '@/components/table/Table.vue'
import KWDynamicForm from '@/components/dynamic-form/DynamicForm.vue'
import PermissionUtil from '@/common/utils/permission/permission-util'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { Name, ProcessTaskDetail, FromData, SubmitFormData, ProcessTaskForm } from './interface/todo-task'
import { GetShowFormData, TrunTaskApi, GiveBackTaskApi, SaveFormData, CompleteProcessTaskPostApi, GetPreOneIncomeNode } from './todo-task-api'
import { DynamicFormItem, DynamicInputFormData } from '@/components/dynamic-form/interface/dynamic-form'
import { GetUserAllApi } from '../../system/user/user-api'
import { MessageBoxData, MessageBoxInputData } from 'node_modules/_element-ui@2.15.10@element-ui/types/message-box'
@Component({
  components: {
    KWTable,
    KWDynamicForm
  }
})
export default class TodoTask extends Vue {
  t: Name = {
    name: ''
  }

  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''
  dialogDynamicFormVisible = false
  dialogTransferFormVisible = false
  dialogGiveBackFormVisible = false
  processTaskPermissionPrefix = PermissionPrefixUtils.processTask

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, ProcessTaskDetail>

  @Ref('dynamicForm')
  readonly dynamicForm!: KWDynamicForm

  @Ref('transferForm')
  readonly transferForm!: KWDynamicForm

  @Ref('giveBackForm')
  readonly giveBackForm!: KWDynamicForm

  formItems: Array<DynamicFormItem> = new Array<DynamicFormItem>()

  transferFormItems: Array<DynamicFormItem> = new Array<DynamicFormItem>()

  giveBackFormItems: Array<DynamicFormItem> = new Array<DynamicFormItem>()

  inputFormData: DynamicInputFormData = {}
  transferInputFormData: DynamicInputFormData = {}
  giveBackInputFormData: DynamicInputFormData = {}

  rules = {}
  transferRules = {}

  hasPermissionEnabled(): boolean {
    return PermissionUtil.hasPermissionForEnabled(this.processTaskPermissionPrefix)
  }

  hasPermission(data: ProcessTaskDetail): boolean {
    return true
  }

  async processTask(processTaskDetail: ProcessTaskDetail): Promise<void> {
    const { code, msg, data } = await GetShowFormData(processTaskDetail.id)
    if (code === 200) {
      if (data === 'non-from') {
        this.doTask(processTaskDetail.id)
      } else {
        this.renderDynamicForm(data as Array<FromData>)
      }
    } else {
      this.$message.error(msg || '获取代办数据失败!')
    }
  }

  renderDynamicForm(datas: Array<FromData>): void {
    this.formItems = []
    this.inputFormData = {}
    if (datas && datas.length > 0) {
      datas.forEach(item => {
        let type = item.controlType
        switch (item.controlType) {
          case 'long':
            type = 'number'
            break
          case 'cUser':
            type = 'select'
            break
          case 'string':
            type = 'text'
            break
          case 'date':
            break
          case 'checkbox':
            break
        }
        if (item.controlDefValue !== '无') {
          this.inputFormData[item.id] = item.controlDefValue
        }
        this.formItems.push({
          label: item.controlLabel,
          type: type,
          model: item.id,
          isParam: item.controlIsParam
        })
      })
    }
  }

  async transferTask(processTaskDetail: ProcessTaskDetail): Promise<void> {
    this.transferFormItems = []
    this.transferInputFormData = {}
    this.transferRules = {}
    const { code, msg, data } = await GetUserAllApi()
    if (code !== 200) {
      this.$message.error(msg || '获取用户列表失败!')
    } else {
      const users: Array<{ label: string; value: string }> = []
      data.forEach(user => {
        users.push({ label: user.nickname, value: user.username })
      })

      this.transferFormItems.push({
        label: '转办用户',
        type: 'select',
        model: 'userName',
        isParam: '',
        opts: users
      })
      this.transferFormItems.push({
        label: '备注',
        type: 'textarea',
        model: 'audit',
        isParam: ''
      })
      this.transferFormItems.push({
        label: '',
        type: 'hidden',
        model: 'taskId',
        isParam: ''
      })
      this.transferFormItems.push({
        label: '',
        type: 'hidden',
        model: 'processInstanceId',
        isParam: ''
      })
      this.transferInputFormData = { userName: '', audit: '', taskId: processTaskDetail.id, processInstanceId: processTaskDetail.instanceId }
      this.transferRules = {
        userName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }]
      }
    }
    this.dialogTransferFormVisible = true
  }

  getTransferFormDatas(): void {
    this.transferForm.dynamicFormRef.validate(async valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      const formDatas = this.transferForm.dynamicFormData
      const processTaskForm: ProcessTaskForm = {
        taskId: formDatas.taskId as string,
        assignee: formDatas.userName as string,
        processInstanceId: formDatas.processInstanceId as string,
        audit: formDatas.audit as string
      }
      const { code, msg } = await TrunTaskApi(processTaskForm)
      if (code !== 200) {
        this.$message.error(msg || '任务转签失败!')
      } else {
        this.searchProcessTask()
        this.dialogTransferFormVisible = false
        this.$message.success('任务转签成功!')
      }
    })
  }

  giveBackTask(processTaskDetail: ProcessTaskDetail): void {
    this.giveBackFormItems = []
    this.giveBackInputFormData = {}
    this.giveBackFormItems.push({
      label: '',
      type: 'hidden',
      model: 'taskId',
      isParam: ''
    })
    this.giveBackFormItems.push({
      label: '备注',
      type: 'textarea',
      model: 'audit',
      isParam: ''
    })
    this.giveBackInputFormData = { audit: '', taskId: processTaskDetail.id }
    this.dialogGiveBackFormVisible = true
  }

  getGiveBackFormDatas(): void {
    this.giveBackForm.dynamicFormRef.validate(async valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      const formDatas = this.giveBackForm.dynamicFormData
      const processTaskForm: ProcessTaskForm = {
        taskId: formDatas.taskId as string,
        audit: formDatas.audit as string
      }
      const { code, msg } = await GiveBackTaskApi(processTaskForm)
      if (code !== 200) {
        this.$message.error(msg || '任务退还失败!')
      } else {
        this.searchProcessTask()
        this.dialogGiveBackFormVisible = false
        this.$message.success('任务退还成功!')
      }
    })
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

  getPreOneIncomeNode(data: ProcessTaskDetail): void {
    this.$prompt('驳回内容', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'textarea'
    })
      .then(async (messageBoxInputData: MessageBoxData) => {
        messageBoxInputData = messageBoxInputData as MessageBoxInputData
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: messageBoxInputData.value,
          taskId: data.id,
          processInstanceId: data.instanceId
        }
        const { code, msg } = await GetPreOneIncomeNode(completeProcessTaskForm)
        if (code !== 200) {
          this.$message.error(msg || '驳回失败!')
        } else {
          this.searchProcessTask()
          this.$message.success('驳回成功!')
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

  handleCancellation(data: ProcessTaskDetail): void {
    this.$prompt('确认要作废', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'textarea'
    })
      .then(async (messageBoxInputData: MessageBoxData) => {
        messageBoxInputData = messageBoxInputData as MessageBoxInputData
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: messageBoxInputData.value,
          taskId: data.id,
          processInstanceId: data.instanceId
        }
        const { code, msg } = await GetPreOneIncomeNode(completeProcessTaskForm)
        if (code !== 200) {
          this.$message.error(msg || '作废失败!')
        } else {
          this.searchProcessTask()
          this.$message.success('作废成功!')
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
    this.formItems.forEach(item => {
      map.set(item.model, item)
    })
    return map
  }

  getDynamicFormDatas(): void {
    console.log('getDynamicFormDatas...')
    this.dynamicForm.dynamicFormRef.validate(async valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      const map = this.getFormItemsToMap()
      const formDatas = this.dynamicForm.dynamicFormData
      let param = ''
      for (const key in formDatas) {
        const formItem = map.get(key) as DynamicFormItem
        const val = formDatas[key]
        const str = key + '-_!' + val + '-_!' + formItem.isParam
        param += str + '!_!'
      }
      console.log('param:{}', param)
      const params = param.slice(0, param.length - 3)
      console.log('params:{}', params)
      const formData: SubmitFormData = {
        taskId: '',
        formData: params
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
}
</script>

<style lang="less" scoped >
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
</style>

<template>
  <div style="height: 100%">
    <div class="navigation-breadcrumb">
      <div>历史任务</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>历史任务</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入流程名称" v-model="t.name" v-hasPermissionQueryPage="historyTaskPermissionPrefix">
            <el-button slot="append" class="search-primary" icon="el-icon-search" @click="searchHistoryTask"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <KWTable url="/api-activiti/activitiHistoryCtrl/getInstancesByUserName" v-hasPermissionQueryPage="historyTaskPermissionPrefix" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="id" sortable="custom" label="实例ID"> </el-table-column>
        <el-table-column prop="taskDefinitionKey" sortable="custom" label="KEY"> </el-table-column>
        <el-table-column prop="name" label="审批节点名称" sortable="custom"></el-table-column>
        <el-table-column prop="processInstanceName" label="实例名称"></el-table-column>
        <!-- <el-table-column prop="processInstanceId" label="实例ID" sortable="custom"></el-table-column> -->
        <el-table-column prop="assignee" label="办理人" sortable="custom"></el-table-column>
        <!-- <el-table-column prop="createTime" label="创建时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.createTime | dateTimeFormat }}</template>
        </el-table-column> -->
        <el-table-column prop="startTime" label="开始时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.startTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <!-- <el-tooltip effect="dark" content="追回" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.isRecover === true" icon="el-icon-bottom" @click="getRecover(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="申请作废" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.isAbandon === true" icon="el-icon-s-release" @click="getAbandon(scope.row)"></el-button>
            </el-tooltip> -->
            <el-tooltip effect="dark" content="查看历史" placement="top" :enterable="false">
              <el-button type="warning" icon="el-icon-view" size="mini" @click="showApprovalHistory(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
      <KWBpmnJsIframe :showBpmn="showBpmn" :instanceId="instanceId" :deploymentFileUUID="deploymentFileUUID" :type="'lookBpmn'" :deploymentName="deploymentName"></KWBpmnJsIframe>
      <el-dialog :visible.sync="dialogApprovalHistoryVisible" title="审批表单历史" style="height:auto;" :before-close="()=>{dialogApprovalHistoryVisible = false}" width="30%">
        <el-scrollbar>
          <div class="block" style="margin: 0px 1px;max-height:400px;">
            <el-timeline>
              <el-timeline-item v-for="(value,key) in approvalHistoryMap" :key="key" :timestamp="value[0]" placement="top">
                <el-card class="box-card" style="box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%) !important;border: 1px solid #EBEEF5 !important;">
                  <template v-for="(item,index) in value[1]">
                    <div :key="index">
                      <h4 v-if="index===0" style="padding:5px">{{item.createUserName+' ' + item.controlEventType}}&nbsp;&nbsp;&nbsp;&nbsp;<a style="color:#409EFF;cursor: pointer;" title="查看流程图" @click="showBpmnDialog(item)"><i class="el-icon-view"></i></a></h4>
                      <p class="el-form-item-div" v-if="index!==0 || key !==0" style="padding:5px">{{item.controlLabel+'：'+item.controlValue}}</p>
                    </div>
                  </template>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-scrollbar>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogApprovalHistoryVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogApprovalHistoryVisible = false">关 闭</el-button>
        </span>
      </el-dialog>
    </el-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import KWTable from '@/components/table/Table.vue'
import KWBpmnJsIframe from '@/components/bpmn-js/BpmnJsIframe.vue'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { HistoryTaskDetail, Name } from './interface/history-task'
import { FromDataDetail, ProcessTaskForm } from '../todo-task/interface/todo-task'
import { GetRevocationApi, GetHandleCancellationApi, GetApprovalHistoryList } from '../todo-task/todo-task-api'
import { GetProcessDefinitionApi } from '../process-definition/process-definition-api'
import dateFormat from '@/common/utils/date-util/date-format'
import { DynamicOptions } from '@/components/dynamic-form/interface/dynamic-form'
@Component({
  components: {
    KWTable,
    KWBpmnJsIframe
  }
})
export default class HistoryTask extends Vue {
  t: Name = {
    name: ''
  }

  historyTaskPermissionPrefix = PermissionPrefixUtils.historyTask
  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''
  instanceId = ''
  dialogApprovalHistoryVisible = false

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, HistoryTaskDetail>

  approvalHistoryMap: Map<string, Array<FromDataDetail>> = new Map()

  searchHistoryTask(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  getRecover(data: HistoryTaskDetail): void {
    this.$confirm('确定要追回, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: '',
          taskId: data.id,
          processInstanceId: data.processInstanceId
        }
        const { code, msg } = await GetRevocationApi(completeProcessTaskForm)
        if (code !== 200) {
          this.$message.error(msg || '追回失败!')
        } else {
          this.searchHistoryTask()
          this.$message.success('追回成功!')
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

  getAbandon(data: HistoryTaskDetail): void {
    this.$confirm('确定要作废, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: '',
          taskId: data.id,
          processInstanceId: data.processInstanceId
        }
        const { code, msg } = await GetHandleCancellationApi(completeProcessTaskForm)
        if (code !== 200) {
          this.$message.error(msg || '申请作废失败!')
        } else {
          this.searchHistoryTask()
          this.$message.success('申请作废成功!')
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

  async showBpmnDialog(fromDataDetail: FromDataDetail): Promise<void> {
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

  async showApprovalHistory(historyTaskDetail: HistoryTaskDetail): Promise<void> {
    const { code, msg, data } = await GetApprovalHistoryList(historyTaskDetail.processInstanceId)
    if (code === 200) {
      this.approvalHistoryMap = new Map()
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
      this.dialogApprovalHistoryVisible = true
    } else {
      this.$message.error(msg || '获取审批列表失败!')
    }
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

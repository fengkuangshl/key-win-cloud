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
        <el-table-column prop="name" label="流程名称" sortable="custom"></el-table-column>
        <el-table-column prop="processInstanceId" label="实例ID" sortable="custom"></el-table-column>
        <el-table-column prop="assignee" label="办理人" sortable="custom"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.createTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.startTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-tooltip effect="dark" content="追回" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.isRecover === true" icon="el-icon-bottom" @click="getRecover(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="申请作废" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.isAbandon === true" icon="el-icon-s-release" @click="getAbandon(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import KWTable from '@/components/table/Table.vue'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { HistoryTaskDetail, Name } from './interface/history-task'
import { MessageBoxData, MessageBoxInputData } from 'node_modules/_element-ui@2.15.10@element-ui/types/message-box'
import { ProcessTaskForm } from '../todo-task/interface/todo-task'
import { GetRevocationApi, GetHandleCancellationApi } from '../todo-task/todo-task-api'
@Component({
  components: {
    KWTable
  }
})
export default class HistoryTask extends Vue {
  t: Name = {
    name: ''
  }

  historyTaskPermissionPrefix = PermissionPrefixUtils.historyTask

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, HistoryTaskDetail>

  searchHistoryTask(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  getRecover(data: HistoryTaskDetail): void {
    this.$prompt('追回原因', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'textarea'
    })
      .then(async (messageBoxInputData: MessageBoxData) => {
        messageBoxInputData = messageBoxInputData as MessageBoxInputData
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: messageBoxInputData.value,
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
    this.$prompt('申请作废内容原因', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'textarea'
    })
      .then(async (messageBoxInputData: MessageBoxData) => {
        messageBoxInputData = messageBoxInputData as MessageBoxInputData
        var completeProcessTaskForm: ProcessTaskForm = {
          audit: messageBoxInputData.value,
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

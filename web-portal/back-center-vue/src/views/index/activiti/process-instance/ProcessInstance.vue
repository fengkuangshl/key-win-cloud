<template>
  <div style="height: 100%">
    <div class="navigation-breadcrumb">
      <div>流程实例</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>流程实例</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入实例名称" v-model="t.name" v-hasPermissionQueryPage="processInstancePermissionPrefix">
            <el-button slot="append" class="search-primary" icon="el-icon-search" @click="searchProcessInstance"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <KWTable url="/api-activiti/processInstanceCtrl/getInstances" v-hasPermissionQueryPage="processInstancePermissionPrefix" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="id" sortable="custom" label="实例ID"> </el-table-column>
        <el-table-column prop="name" sortable="custom" label="实例名称"> </el-table-column>
        <el-table-column prop="processDefinitionId" label="流程定义编号" sortable="custom"></el-table-column>
        <el-table-column prop="processDefinitionKey" label="流程定义KEY" sortable="custom"></el-table-column>
        <!-- <el-table-column prop="processDefinitionVersion" label="版本" sortable="custom"></el-table-column> -->
        <el-table-column prop="status" label="状态" sortable="custom" :formatter="
            row => {
              if(row.status &&  row.status == 'active'){
                return '激活'
              }else{
                return '挂起'
              }
            }
          "></el-table-column>
        <el-table-column prop="startTime" label="创建时间" sortable="custom">
          <template slot-scope="scope">{{ scope.row.startTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-tooltip effect="dark" content="挂起" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status == 'active'" icon="el-icon-video-pause" v-hasPermissionUpdate="processInstancePermissionPrefix" size="mini" @click="suspendProcessInstance(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="激活" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'active'" icon="el-icon-video-play" v-hasPermissionUpdate="processInstancePermissionPrefix" size="mini" @click="resumeProcessInstance(scope.row.id)"></el-button>
            </el-tooltip>
            <el-button type="danger" icon="el-icon-delete" v-hasPermissionDelete="processInstancePermissionPrefix" size="mini" @click="deleteProcessInstance(scope.row.id)">
            </el-button>
            <el-tooltip effect="dark" content="查看历史" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="warning" icon="el-icon-view" size="mini" @click="showProcessInstanceDetail(scope.row)">
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
      <KWBpmnJsIframe :showBpmn="showBpmn" :deploymentFileUUID="deploymentFileUUID" :type="'lookBpmn'" :deploymentName="deploymentName"></KWBpmnJsIframe>
    </el-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import KWTable from '@/components/table/Table.vue'
import KWBpmnJsIframe from '@/components/bpmn-js/BpmnJsIframe.vue'
import PermissionUtil from '@/common/utils/permission/permission-util'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { ProcessInstanceDetail } from './interface/process-instance'
import { DeleteProcessInstanceApi, ResumeProcessInstanceApi, SuspendProcessInstanceApi } from './process-instance-api'
@Component({
  components: {
    KWTable,
    KWBpmnJsIframe
  }
})
export default class ProcessInstance extends Vue {
  t: ProcessInstanceDetail = {
    resourceName: '',
    deploymentId: '',
    processDefinitionVersion: 0,
    startTime: new Date(),
    status: '',
    id: '',
    version: 0
  }

  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''

  processInstancePermissionPrefix = PermissionPrefixUtils.processInstance

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<ProcessInstanceDetail, ProcessInstanceDetail>

  hasPermissionEnabled(): boolean {
    return PermissionUtil.hasPermissionForEnabled(this.processInstancePermissionPrefix)
  }

  hasPermission(data: ProcessInstanceDetail): boolean {
    return true
  }

  deleteProcessInstance(id: string): void {
    this.$confirm('确定要删除, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await DeleteProcessInstanceApi(id)
        if (code !== 200) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchProcessInstance()
          this.$message.success('删除成功!')
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

  searchProcessInstance(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  suspendProcessInstance(id: string): void {
    this.$confirm('确定要挂起当前流程实现吗, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await SuspendProcessInstanceApi(id)
        if (code !== 200) {
          this.$message.error(msg || '挂起失败!')
        } else {
          this.searchProcessInstance()
          this.$message.success('挂起成功!')
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

  resumeProcessInstance(id: string): void {
    this.$confirm('确定要激活当前流程实现吗, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await ResumeProcessInstanceApi(id)
        if (code !== 200) {
          this.$message.error(msg || '激活失败!')
        } else {
          this.searchProcessInstance()
          this.$message.success('激活成功!')
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

  showProcessInstanceDetail(data: ProcessInstanceDetail): void {
    this.deploymentFileUUID = data.deploymentId
    this.deploymentName = data.resourceName
    this.showBpmn = true
    setTimeout(() => {
      this.showBpmn = false
    }, 1000)
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

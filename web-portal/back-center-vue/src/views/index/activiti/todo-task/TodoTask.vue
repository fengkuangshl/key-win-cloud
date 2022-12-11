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
          <template slot-scope="scope">{{ scope.row.startTime | dateTimeFormat }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-tooltip effect="dark" content="办理" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status !== 'SUSPENDED'" icon="el-icon-video-pause" v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini" @click="processTask(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="办理" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="primary" v-if="scope.row.status == 'SUSPENDED'" icon="el-icon-video-play" :disabled=true v-hasPermissionUpdate="processTaskPermissionPrefix" size="mini"></el-button>
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
import PermissionUtil from '@/common/utils/permission/permission-util'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { Name, ProcessTaskDetail } from './interface/todo-task'
import { GetFormDataShow } from './todo-task-api'
@Component({
  components: {
    KWTable
  }
})
export default class ProcessTask extends Vue {
  t: Name = {
    name: ''
  }

  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''

  processTaskPermissionPrefix = PermissionPrefixUtils.processTask

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, ProcessTaskDetail>

  hasPermissionEnabled(): boolean {
    return PermissionUtil.hasPermissionForEnabled(this.processTaskPermissionPrefix)
  }

  hasPermission(data: ProcessTaskDetail): boolean {
    return true
  }

  processTask(id: string): void {
    this.$confirm('确定要办理, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await GetFormDataShow(id)
        if (code !== 200) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchProcessTask()
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

  searchProcessTask(): void {
    this.kwTableRef.loadByCondition(this.t)
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

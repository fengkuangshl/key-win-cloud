<template>
  <div>
    <div class="navigation-breadcrumb">
      <div>组管理</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <!-- <el-breadcrumb-item>后台管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item> -->
        <el-breadcrumb-item>组列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入内容" v-model="t.name" v-hasPermissionQueryPage="groupPermission">
            <el-button slot="append" class="search-primary" icon="el-icon-search" @click="searchGroup"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addGroup" v-hasPermissionAdd="groupPermission">添加组</el-button>
        </el-col>
      </el-row>
      <KWTable url="api-user/group/findSysGroupByPaged" style="width: 100%" ref="kwTableRef" v-hasPermissionQueryPage="groupPermission">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="name" sortable="custom" label="组名称"> </el-table-column>
        <el-table-column prop="code" sortable="custom" label="code"> </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" v-hasPermissionUpdate="groupPermission" size="mini" @click="showEditDialog(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" v-hasPermissionDelete="groupPermission" @click="deleteGroup(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :title="title" @close="aditGroupClosed" :visible.sync="groupDialogVisble" width="20%">
      <el-form :model="sysGroupForm" :rules="sysGroupFormRules" ref="sysGroupFormRef" label-width="70px">
        <el-form-item label="组" prop="name">
          <el-input v-model="sysGroupForm.name" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="code" prop="code">
          <el-input v-model="sysGroupForm.code" style="max-width: 220px;" :disabled="codeDisabled"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="groupDialogVisble = false">取 消</el-button>
        <el-button type="primary" @click="editGroup">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { SysGroup, SysGroupSearchRequest, SysGroupForm } from './interface/sys-group'
import { DeleteSysGroupApi, SysGroupSaveOrUpdateApi } from './group-api'
import KWTable from '@/components/table/Table.vue'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'

@Component({
  components: {
    KWTable
  }
})
export default class Group extends Vue {
  t: SysGroupSearchRequest = { name: '' }

  title = ''
  groupDialogVisble = false
  menuGroupDialogVisble = false
  groupPermissionVisble = false
  codeDisabled = true
  editSysGroupId = -1
  sysGroupForm: SysGroupForm = { name: '', code: '', parentId: '-1' }
  @Ref('sysGroupFormRef')
  readonly sysGroupFormRef!: ElForm

  groupPermission = PermissionPrefixUtils.group

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<SysGroupSearchRequest, SysGroup>

  readonly sysGroupFormRules: { name: Array<KWRule.Rule | KWRule.MixinRule>; code: Array<KWRule.Rule | KWRule.MixinRule> } = {
    name: [
      { required: true, message: '请输入组的名称', trigger: 'blur' },
      { min: 3, max: 20, message: '组名称的长度3~20个字符之间', trigger: 'blur' }
    ],
    code: [
      { required: true, message: '请输入组的code', trigger: 'blur' },
      { min: 3, max: 20, message: '组code的长度3~20个字符之间', trigger: 'blur' }
    ]
  }

  // 展示编辑用于的对话框
  async showEditDialog(group: SysGroup): Promise<void> {
    this.title = '编辑组'
    this.codeDisabled = true
    this.sysGroupForm = group
    this.groupDialogVisble = true
  }

  aditGroupClosed(): void {
    this.groupDialogVisble = false
    this.sysGroupFormRef.resetFields()
  }

  editGroup(): void {
    this.sysGroupFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      const { code, msg } = await SysGroupSaveOrUpdateApi(this.sysGroupForm)
      if (code !== 200) {
        this.$message.error(msg || '操作组信息失败!')
      } else {
        this.groupDialogVisble = false
        this.searchGroup()
        this.$message.success('操作组信息成功!')
      }
    })
  }

  addGroup(): void {
    this.title = '添加组'
    this.codeDisabled = false
    this.groupDialogVisble = true
    this.$nextTick(() => {
      this.sysGroupFormRef.resetFields()
      this.sysGroupForm = { name: '', code: '', parentId: '-1' }
    })
  }

  deleteGroup(id: string): void {
    this.$confirm('确定要删除, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await DeleteSysGroupApi(id)
        if (code !== 200) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchGroup()
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

  searchGroup(): void {
    this.kwTableRef.loadByCondition(this.t)
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 860px;
}

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

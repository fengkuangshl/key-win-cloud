<template>
  <div>
    <div class="navigation-breadcrumb">
      <div>权限管理</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <!-- <el-breadcrumb-item>后台管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item> -->
        <el-breadcrumb-item>权限列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入内容" v-model="t.name">
            <el-button slot="append" icon="el-icon-search" @click="searchPermission"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addPermission">添加权限</el-button>
        </el-col>
      </el-row>
      <KWTable url="api-user/permissions/getSysPermissionByPaged" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="name" sortable="custom" label="角色名称"> </el-table-column>
        <el-table-column prop="permission" sortable="custom" label="权限标识"> </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="deletePermission(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :title="title" @close="aditPermissionClosed" :visible.sync="permissionDialogVisble" width="20%">
      <el-form :model="sysPermissionForm" :rules="sysPermissionFormRules" ref="sysPermissionFormRef" label-width="90px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="sysPermissionForm.name" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="sysPermissionForm.permission" style="max-width: 220px;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="permissionDialogVisble = false">取 消</el-button>
        <el-button type="primary" @click="editPermission">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { Name, PermissionResponse, PermissionForm } from './interface/permission-response'
import KWTable from '@/components/table/Table.vue'
import { SysPermissionSaveOrUpdateApi, DeleteSysPermissionApi } from './permission-api'

@Component({
  components: {
    KWTable
  }
})
export default class Permission extends Vue {
  t: Name = { name: '' }

  title = ''
  permissionDialogVisble = false
  sysPermissionForm: PermissionForm = { name: '', permission: '' }
  @Ref('sysPermissionFormRef')
  readonly sysPermissionFormRef!: ElForm

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, PermissionResponse>

  readonly sysPermissionFormRules: { name: Array<KWRule.Rule | KWRule.MixinRule>; permission: Array<KWRule.Rule> } = {
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' },
      { min: 3, max: 10, message: '名称的长度3~10个字符之间', trigger: 'blur' }
    ],
    permission: [{ required: true, message: '请输入权限标识', trigger: 'blur' }]
  }

  // 展示编辑用于的对话框
  async showEditDialog(permission: PermissionResponse): Promise<void> {
    this.title = '编辑权限'
    this.sysPermissionForm = permission
    this.permissionDialogVisble = true
  }

  aditPermissionClosed(): void {
    this.permissionDialogVisble = false
    this.sysPermissionFormRef.resetFields()
  }

  editPermission(): void {
    this.sysPermissionFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      const { code, msg } = await SysPermissionSaveOrUpdateApi(this.sysPermissionForm)
      if (code !== 0) {
        this.$message.error(msg || '操作权限信息失败!')
      } else {
        this.permissionDialogVisble = false
        this.searchPermission()
        this.$message.success('操作权限信息成功!')
      }
    })
  }

  addPermission(): void {
    this.title = '添加角色'
    this.permissionDialogVisble = true
    this.$nextTick(() => {
      this.sysPermissionFormRef.resetFields()
      this.sysPermissionForm = { name: '', permission: '' }
    })
  }

  deletePermission(id: string): void {
    this.$confirm('确定要重置密码, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await DeleteSysPermissionApi(id)
        if (code !== 0) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchPermission()
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

  searchPermission(): void {
    this.kwTableRef.loadByCondition(this.t)
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 860px;
}
</style>

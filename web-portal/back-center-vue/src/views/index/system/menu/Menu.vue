<template>
  <div>
    <div class="navigation-breadcrumb">
      <div>菜单管理</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <!-- <el-breadcrumb-item>后台管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item> -->
        <el-breadcrumb-item>菜单列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入内容" v-model="t.name">
            <el-button slot="append" icon="el-icon-search" @click="searchMenu"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addMenu">添加菜单</el-button>
        </el-col>
      </el-row>
      <KWTable url="api-user/getSysMenusByPaged" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="name" sortable="custom" label="菜单名称"> </el-table-column>
        <el-table-column prop="path" sortable="custom" label="菜单路由"> </el-table-column>
        <el-table-column prop="url" sortable="custom" label="菜单URL"> </el-table-column>
        <el-table-column prop="css" sortable="custom" label="样式"> </el-table-column>
        <el-table-column prop="sort" sortable="custom" label="排序"> </el-table-column>
        <el-table-column
          prop="isMenu"
          sortable="custom"
          label="类型"
          :formatter="
            row => {
              if (row.isMenu == 2) {
                return '<i style=\'el-icon-notebook-2\'>按钮</i>'
              }
              if (row.parentId == -1) {
                return '<span style=\'el-icon-folder\'>目录</span>'
              } else {
                return '<span style=\'el-icon-menu\'>菜单</span>'
              }
            }
          "
        >
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteMenu(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :title="title" @close="aditMenuClosed" :visible.sync="menuDialogVisble" width="20%">
      <el-form :model="sysMenuForm" :rules="sysMenuFormRules" ref="sysMenuFormRef" label-width="70px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="sysMenuForm.name" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="菜单路由" prop="path">
          <el-input v-model="sysMenuForm.path" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="菜单URL" prop="url">
          <el-input v-model="sysMenuForm.url" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" prop="css">
          <el-input v-model="sysMenuForm.css" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="css">
          <el-radio-group v-model="sysMenuForm.hidden">
            <el-radio label="否"></el-radio>
            <el-radio label="是"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否为菜单" prop="isMenu">
          <el-radio-group v-model="sysMenuForm.isMenu">
            <el-radio label="是"></el-radio>
            <el-radio label="否"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model="sysMenuForm.sort" type="number" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId">
          <el-input v-model="sysMenuForm.parentId" style="max-width: 220px;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisble = false">取 消</el-button>
        <el-button type="primary" @click="editMenu">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { Name, MenuResponse, MenuForm } from './interface/menu-response'
import { DeleteSysMenuApi, SysMuenSaveOrUpdateApi, GetMenuByIdApi } from './menu-api'
import KWTable from '@/components/table/Table.vue'

@Component({
  components: {
    KWTable
  }
})
export default class Menu extends Vue {
  t: Name = { name: '' }

  title = ''
  menuDialogVisble = false
  menuMenuDialogVisble = false
  menuPermissionVisble = false
  sysMenuForm: MenuForm = { name: '', css: '', hidden: false, isMenu: 0, parentId: '', path: '', sort: 0, url: '' }
  @Ref('sysMenuFormRef')
  readonly sysMenuFormRef!: ElForm

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<Name, MenuResponse>

  readonly sysMenuFormRules: { name: Array<KWRule.Rule | KWRule.MixinRule> } = {
    name: [
      { required: true, message: '请输入菜单名称', trigger: 'blur' },
      { min: 3, max: 10, message: '菜单名称的长度3~10个字符之间', trigger: 'blur' }
    ]
  }

  created(): void {
    setTimeout(() => {
      this.searchMenu()
    }, 100)
  }

  // 展示编辑用于的对话框
  async showEditDialog(menu: MenuResponse): Promise<void> {
    this.title = '编辑用户'
    this.sysMenuForm = menu
    this.menuDialogVisble = true
  }

  aditMenuClosed(): void {
    this.menuDialogVisble = false
    this.sysMenuFormRef.resetFields()
  }

  editMenu(): void {
    this.sysMenuFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      const { code, msg } = await SysMuenSaveOrUpdateApi(this.sysMenuForm)
      if (code !== 0) {
        this.$message.error(msg || '操作用户信息失败!')
      } else {
        this.menuDialogVisble = false
        this.searchMenu()
        this.$message.success('操作用户信息成功!')
      }
    })
  }

  addMenu(): void {
    this.title = '添加角色'
    this.menuDialogVisble = true
    this.$nextTick(() => {
      this.sysMenuFormRef.resetFields()
    })
  }

  deleteMenu(id: string): void {
    this.$confirm('确定要重置密码, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await DeleteSysMenuApi(id)
        if (code !== 0) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchMenu()
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

  searchMenu(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  async setMenu(id: string): Promise<void> {
    console.log(id)
    const { code, data, msg } = await GetMenuByIdApi(id)
    if (code === 0) {
      this.menuMenuDialogVisble = true
      this.sysMenuForm = data
    } else {
      this.$message.error(msg || '获取菜单失败！')
    }
  }
}
</script>

<style lang="less" scoped>
.el-select {
  width: 860px;
}
</style>

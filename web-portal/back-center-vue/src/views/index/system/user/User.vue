<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>后台管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入内容" v-model="page.t.username">
            <el-button slot="append"  icon="el-icon-search" @click="searchUser"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addUser">添加用户</el-button>
        </el-col>
      </el-row>
      <el-table :data="tableData.data" stripe border style="width: 100%">
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="username" label="帐号" width="180"> </el-table-column>
        <el-table-column prop="nickname" label="昵称" width="180"> </el-table-column>
        <el-table-column prop="phone" label="手机"> </el-table-column>
        <el-table-column
          prop="sex"
          label="性别"
          :formatter="
            row => {
              return row.sex === 0 ? '男' : '女'
            }
          "
          width="100"
        >
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="formatterDate" width="180"> </el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template v-slot="scope">
            <el-switch v-model="scope.row.enabled" active-color="#13ce66" inactive-color="#ff4949" @change="userStatuChanged(scope.row)"> </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
            <el-tooltip effect="dark" content="重置密码" placement="top" :enterable="false">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="passwordReset(scope.row.id)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.pageNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="tableData.count"> </el-pagination>
    </el-card>
    <el-dialog title="修改用户" @close="aditUserClosed" :visible.sync="editUserDialogVisble" width="20%">
      <el-form :model="editUserForm" :rules="addFormRules" ref="editUserFormRef" label-width="70px">
        <el-form-item label="帐号">
          <el-input v-model="editUserForm.username" style="max-width: 220px;" :disabled="usernameDisabled"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="nickname">
          <el-input v-model="editUserForm.nickname" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="editUserForm.phone" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="editUserForm.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="editUserForm.roleId" multiple filterable allow-create default-first-option placeholder="请选择">
            <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id"> </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editUserDialogVisble = false">取 消</el-button>
        <el-button type="primary" @click="editUserInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
@Component
export default class User extends Vue {

}
</script>

<style lang="less" scoped>

</style>

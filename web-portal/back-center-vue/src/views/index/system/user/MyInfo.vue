<template>
  <div>
    <div class="navigation-breadcrumb">
      <div>我的信息</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <!-- <el-breadcrumb-item>后台管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item> -->
        <el-breadcrumb-item>我的信息</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-form :model="userForm" :rules="userFormRules" ref="userFormRef" label-width="70px">
        <el-form-item label="帐号">
          <el-input v-model="userForm.username" style="max-width: 220px;" :disabled="usernameDisabled"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="nickname">
          <el-input v-model="userForm.nickname" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="userForm.phone" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="userForm.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="editUserInfo">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { UserForm, Sex, LoginSuccessUserInfo } from './interface/user'
import { UserGetApi, UpdateMeApi } from './user-api'
import { UserModule } from '@/store/user-store'

@Component
export default class MyInfo extends Vue {
  userForm: UserForm = { nickname: '', phone: '', sex: Sex.男, username: '', roleId: '' }
  @Ref('userFormRef')
  readonly userFormRef!: ElForm

  usernameDisabled = true
  readonly userFormRules: { nickname: Array<KWRule.Rule>; phone: Array<KWRule.Rule> } = {
    nickname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    phone: [{ required: true, message: '请输入手机', trigger: 'blur' }]
  }

  created(): void {
    this.getUserInfo()
  }

  // 展示编辑用于的对话框
  async getUserInfo(): Promise<void> {
    const res = await UserGetApi((UserModule.loginUser as LoginSuccessUserInfo).user.id)
    this.userForm = res.data
    this.userForm.sex = this.userForm.sex === 0 ? '男' : '女'
  }

  editUserInfo(): void {
    this.userFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      this.userForm.sex = this.userForm.sex === '男' ? 0 : 1
      const { code, msg } = await UpdateMeApi(this.userForm)
      if (code !== 0) {
        this.$message.error(msg || '操作用户信息失败!')
      } else {
        this.$message.success('操作用户信息成功!')
        this.getUserInfo()
      }
    })
  }
}
</script>

<style lang="less" scoped></style>

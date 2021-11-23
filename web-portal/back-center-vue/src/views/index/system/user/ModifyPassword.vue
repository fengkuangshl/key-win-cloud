<template>
  <div>
    <el-dialog title="修改密码" @close="aditUserPasswordClosed" :visible.sync="userPasswordDialogVisble" width="20%">
      <el-form :model="userForm" :rules="userFormRules" ref="userFormRef" label-width="70px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="userForm.oldPassword" type="password" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="userForm.newPassword" style="max-width: 220px;"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="rePassword">
          <el-input v-model="userForm.rePassword" style="max-width: 220px;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="userPasswordDialogVisble = false">取 消</el-button>
        <el-button type="primary" @click="editUserPassword">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { ModifyPassword } from './interface/user'
import { UpdatePasswordApi } from './user-api'

@Component
export default class User extends Vue {
  userPasswordDialogVisble = false
  userForm: ModifyPassword = { id: '', oldPassword: '', newPassword: '', rePassword: '' }
  @Ref('userFormRef')
  readonly userFormRef!: ElForm

  readonly userFormRules: { oldPassword: Array<KWRule.Rule>; newPassword: Array<KWRule.Rule>; rePassword: Array<KWRule.Rule> } = {
    oldPassword: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    rePassword: [{ required: true, message: '请再次输入密码', trigger: 'blur' }]
  }

  // 展示编辑用于的对话框
  showEditDialog(): void {
    this.userPasswordDialogVisble = true
  }

  aditUserPasswordClosed(): void {
    this.userPasswordDialogVisble = false
    this.userFormRef.resetFields()
  }

  editUserPassword(): void {
    this.userFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      this.modifyPassword()
    })
  }

  modifyPassword(): void {
    this.$confirm('确定要重置密码, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await UpdatePasswordApi(this.userForm)
        if (code !== 0) {
          this.$message.error(msg || '用户密码修改失败!')
        } else {
          this.userPasswordDialogVisble = false
          this.$message.success('用户密码修改成功!')
        }
      })
      .catch(e => {
        console.log(e)
        this.$message({
          type: 'info',
          message: '已取消密码修改'
        })
      })
  }
}
</script>

<style lang="less" scoped></style>

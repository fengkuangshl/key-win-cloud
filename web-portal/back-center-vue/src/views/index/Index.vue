<template>
  <div class="index">
    <el-container class="index-container">
      <el-header>
        <HeaderNav></HeaderNav>
      </el-header>
      <el-container>
        <LeftMenu></LeftMenu>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import HeaderNav from '@/components/header/HeaderNav.vue'
import LeftMenu from '@/components/left/LeftLeftMenu.vue'
import { UserInfoApi } from '../../views/index/system/user/UserApi'
import { CurrentUserResponse } from '../../views/index/system/user/interface/UserResponse'
import * as qs from 'qs'

@Component({
  components: {
    HeaderNav,
    LeftMenu
  }
})
export default class Index extends Vue {
  created(): void {
    this.getUserInfo()
  }

  async getUserInfo(): Promise<void> {
    const { code, data, msg }: Ajax.AjaxResult<CurrentUserResponse> = await UserInfoApi()
    console.log(data)
    if (code === 0) {
      localStorage.setItem('userInfo', qs.stringify(data))
    } else {
      this.$message.error(msg || '获取用户失败！')
    }
  }
}
</script>

<style lang="less" scoped>
.index {
  height: 100%;
  .index-container {
    height: 100%;
    .el-header {
      background-color: #373d3f;
      align-items: center;
    }
  }
}
</style>

<template>
  <div class="index">
    <el-container class="index-container">
      <el-header>
        <HeaderNav></HeaderNav>
      </el-header>
      <el-container>
        <LeftMenu :menusList="menus"></LeftMenu>
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
import LeftMenu from '@/components/left/LeftMenu.vue'
import { UserInfoApi } from '../../views/index/system/user/UserApi'
import { LoginSuccessUserInfo } from './system/user/interface/User'
import { MenuApi } from '../../views/index/system/menu/MenuApi'
import { MenuResponse } from '../../views/index/system/menu/interface/MenuResponse'
import * as qs from 'qs'

@Component({
  components: {
    HeaderNav,
    LeftMenu
  }
})
export default class Index extends Vue {
  menus: Array<MenuResponse> | [] = []

  created(): void {
    this.getUserInfo()
  }

  async getUserInfo(): Promise<void> {
    const { code, data, msg }: KWResponse.Result<LoginSuccessUserInfo> = await UserInfoApi()
    console.log(data)
    if (code === 0) {
      localStorage.setItem('userInfo', qs.stringify(data))
      this.getMenuList()
    } else {
      this.$message.error(msg || '获取用户失败！')
    }
  }

  async getMenuList(): Promise<void> {
    const { code, data, msg }: KWResponse.Result<Array<MenuResponse>> = await MenuApi()
    if (code === 0) {
      console.log(data)
      this.menus = data
    } else {
      this.$message.error(msg || '获取当前用户菜单失败！')
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

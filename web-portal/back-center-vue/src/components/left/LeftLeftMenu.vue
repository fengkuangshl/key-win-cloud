<template>
  <el-aside :width="isCollapse ? '64px' : '200px'">
    <div class="toggle-button" @click="toggleCollpase">|||</div>
    <el-menu background-color="#333744" text-color="#fff" active-text-color="#409EFF" unique-opened :collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
      <!-- 一级菜单 -->
      <el-submenu :index="item.id" v-for="item in menus" :key="item.id">
        <!-- 一级菜单模板区 -->
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>{{ item.name }}</span>
        </template>
        <!--二级菜单-->
        <el-menu-item :index="subItem.url.replace('#!', '/')" v-for="subItem in item.subMenus" :key="subItem.id" @click="saveActivePath(subItem.url.replace('#!', '/'))">
          <!-- 二级菜单模板区 -->
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>{{ subItem.name }}</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { UserInfoApi } from '../../common/user/UserApi'
import { MenuApi } from '../../common/menu/MenuApi'
import { CurrentUserResponse } from '../../common/user/interface/UserResponse'
import { MenuResponse } from '../../common/menu/interface/MenuResponse'
import * as qs from 'qs'

@Component({
  components: {}
})
export default class LeftMenu extends Vue {
  isCollapse = false
  activePath = ''
  menus: Array<MenuResponse> | [] = []

  created(): void {
    this.getUserInfo()
    this.activePath = localStorage.getItem('activePath') || ''
  }

  async getUserInfo(): Promise<void> {
    const { code, data, msg }: Ajax.AjaxResult<CurrentUserResponse> = await UserInfoApi()
    console.log(data)
    if (code === 0) {
      localStorage.setItem('userInfo', qs.stringify(data))
      this.getMenuList()
    } else {
      this.$message.error(msg || '获取用户失败！')
    }
  }

  async getMenuList(): Promise<void> {
    const { code, data, msg }: Ajax.AjaxResult<Array<MenuResponse>> = await MenuApi()
    if (code === 0) {
      console.log(data)
      this.menus = data
    } else {
      this.$message.error(msg || '获取当前用户菜单失败！')
    }
  }

  toggleCollpase(): void {
    this.isCollapse = !this.isCollapse
  }

  saveActivePath(activePath: string): void {
    localStorage.setItem('activePath', activePath)
    this.activePath = activePath
  }
}
</script>

<style lang="less" scoped>
.el-aside {
  background-color: #333744;
  align-items: left;
  .el-menu {
    border-right: none;
  }
}
.el-main {
  background-color: #eaedf1;
}
.iconfont {
  padding-right: 10px;
}
.toggle-button {
  background: #4a5064;
  font-size: 10px;
  text-align: center;
  line-height: 24px;
  color: #fff;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>

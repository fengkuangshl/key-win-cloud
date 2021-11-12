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
import { MenuResponse } from '@/views/index/system/menu/interface/menu-response'
// import * as qs from 'qs'
import { MenuModule } from '@/store/menu-store'
@Component({
  components: {
    HeaderNav,
    LeftMenu
  }
})
export default class Index extends Vue {
  menus: Array<MenuResponse> | [] = []

  created(): void {
    this.menus = MenuModule.getMenus
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

<template>
  <div class="index">
    <el-container class="index-container">
      <LeftMenu :menusList="menus"></LeftMenu>
      <el-container>
        <el-header>
          <HeaderNav></HeaderNav>
        </el-header>
        <el-main>
          <PageTabs :keep-alive-component-instance="keepAliveComponentInstance" />
          <div ref="keepAliveContainer" style="padding-top:20px">
            <keep-alive>
              <router-view :key="$route.fullPath" />
            </keep-alive>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import HeaderNav from '@/components/header/HeaderNav.vue'
import LeftMenu from '@/components/left/LeftMenu.vue'
import PageTabs from '@/components/page-tabs/PageTabs.vue'
import { MenuResponse } from '@/views/index/system/menu/interface/menu-response'
// import * as qs from 'qs'
import { MenuModule } from '@/store/menu-store'
@Component({
  components: {
    HeaderNav,
    LeftMenu,
    PageTabs
  }
})
export default class Index extends Vue {
  menus: Array<MenuResponse> | [] = []
  keepAliveComponentInstance: any = null

  @Ref('pageTabs')
  readonly pageTabs!: PageTabs

  created(): void {
    this.menus = MenuModule.getMenus
  }

  mounted(): void {
    if (this.$refs.keepAliveContainer) {
      this.keepAliveComponentInstance = (this.$refs.keepAliveContainer as any).childNodes[0].__vue__ // 获取keep-alive的控件实例对象
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

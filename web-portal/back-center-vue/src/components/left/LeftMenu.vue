<template>
  <el-aside :width="isCollapse ? '64px' : '200px'">
    <div class="toggle-button" @click="toggleCollpase">|||</div>
    <el-menu background-color="#333744" text-color="#fff" active-text-color="#409EFF" unique-opened :collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
      <!-- 一级菜单 -->
      <el-submenu :index="item.id" v-for="item in menusList" :key="item.id">
        <!-- 一级菜单模板区 -->
        <template slot="title">
          <i :class="item.css"></i>
          <span>{{ item.name }}</span>
        </template>
        <!--二级菜单-->
        <el-menu-item :index="subItem.url.replace('#!', '/')" v-for="subItem in item.subMenus" :key="subItem.id" @click="saveActivePath(subItem.url.replace('#!', '/'))">
          <!-- 二级菜单模板区 -->
          <template slot="title">
            <i :class="subItem.css"></i>
            <span>{{ subItem.name }}</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { MenuResponse } from '@/views/index/system/menu/interface/menu-response'
import { local } from '@/store'

@Component({
  components: {}
})
export default class LeftMenu extends Vue {
  isCollapse = false
  activePath = ''
  @Prop({ default: [] })
  readonly menusList!: Array<MenuResponse> | []

  created(): void {
    this.activePath = local.getStr('activePath')
  }

  toggleCollpase(): void {
    this.isCollapse = !this.isCollapse
  }

  saveActivePath(activePath: string): void {
    local.save('activePath', activePath)
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

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

@Component({
  components: {
    HeaderNav,
    LeftMenu
  }
})
export default class Index extends Vue {}
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

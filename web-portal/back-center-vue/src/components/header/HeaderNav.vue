<template>
  <div class="el-header-div">
    <div>
      <img src="../../assets/keywin.png" alt="" />
      <span>后台管理系统</span>
    </div>
    <el-button @click="logout">退出</el-button>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  components: {}
})
export default class HeaderNav extends Vue {
  logout(): void {
    localStorage.clear()
    this.$router.push('/login')
  }
}
</script>

<style lang="less" scoped>
.el-header-div {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  font-size: 20px;
  height: 100%;
  > div {
    display: flex;
    align-items: center;
    img {
      width: 156px;
      height: 40px;
      // border-radius: 50%;
    }
    span {
      margin-left: 15px;
    }
  }
}
</style>

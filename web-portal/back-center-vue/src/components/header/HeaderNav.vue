<template>
  <div class="el-header-div">
    <div>
      <img src="../../assets/keywin.png" alt="" />
      <span>后台管理系统</span>
    </div>
    <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
      <!-- src="@/assets/404-images/404-cloud.png" -->
      <div class="avatar-wrapper">
        <img :src="avatar" class="user-avatar" @error="imgerrorfun" />
        <div>
          <span>{{ nickname }}</span>
          <i class="el-icon-caret-bottom" />
        </div>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="userInfoCenter">个人信息</el-dropdown-item>
        <el-dropdown-item @click.native="modifyPassword">修改密码</el-dropdown-item>
        <el-dropdown-item divided @click.native="logout">
          <span style="display:block;">退出系统</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script lang="ts">
import { UserModule } from '@/store/user-store'
import { Component, Vue } from 'vue-property-decorator'
@Component({
  components: {}
})
export default class HeaderNav extends Vue {
  get avatar(): string | null {
    return UserModule.getUser.user.headImgUrl
  }

  get nickname(): string {
    return UserModule.getUser.user.nickname
  }

  userInfoCenter(): void {
    console.log('userInfoCenter')
  }

  modifyPassword(): void {
    console.log('modifyPassword')
  }

  logout(): void {
    localStorage.clear()
    this.$router.push('/login')
  }

  dropdownClick(): void {
    console.log('dropdownClick')
  }

  /**
   * @description: 图片404则会进入图片err事件
   * @param {*} event
   * @return {*}
   */
  private imgerrorfun(event: any): void {
    // console.log(event);
    const img: HTMLImageElement = event.srcElement
    img.src = require('@/assets/head.png')
    img.onerror = null // 控制不要一直跳动;
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
  .el-dropdown-menu__item {
    > a {
      text-decoration: none;
    }
  }
  .avatar-container {
    margin-right: 30px;

    .avatar-wrapper {
      margin-top: 5px;
      position: relative;
      > div {
        float: right;
        cursor: pointer;
        margin-top: 10%;
        margin-left: -13px;
      }

      .user-avatar {
        cursor: pointer;
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }

      .el-icon-caret-bottom {
        cursor: pointer;
        position: absolute;
        right: -20px;
        top: 9px;
        font-size: 20px;
      }
    }
  }
}
</style>

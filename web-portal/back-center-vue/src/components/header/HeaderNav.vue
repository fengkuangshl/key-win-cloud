<template>
  <div class="el-header-div">
    <div>
      <!-- <div class="toggle-button" @click="toggleCollpase">|||</div> -->
      <i :class="collapseMenuIcon" @click="toggleCollpase"></i>
      <span>后台管理系统</span>
    </div>
    <div>
      <el-tooltip class="fullscreen" :content="tipFullScrollContent" effect="dark" placement="bottom" fullscreen>
        <i :class="fullScrollClass" @click="fullScroll" />
      </el-tooltip>
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <!-- src="@/assets/404-images/404-cloud.png" -->
        <div class="avatar-wrapper">
          <img :src="headImgUrl" class="user-avatar" @error="imgerrorfun" />
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
  </div>
</template>

<script lang="ts">
import { UserModule } from '@/store/user-store'
import { MenuCollapseModule } from '@/store/menu-collapse-store'
import { Component, Vue } from 'vue-property-decorator'
@Component({
  components: {}
})
export default class HeaderNav extends Vue {
  fullScrollClass = 'iconfont icon-quanping_o'
  tipFullScrollContent = '全屏浏览'
  collapseMenuIcon = 'el-icon-s-fold'

  get nickname(): string {
    const user = UserModule.getUser.user
    if (user !== undefined) {
      return user.nickname
    }
    return ''
  }

  get headImgUrl(): string | null {
    const user = UserModule.getUser.user
    if (user !== undefined) {
      return user.headImgUrl
    }
    return null
  }

  mounted(): void {
    this.$nextTick(() => {
      document.addEventListener('fullscreenchange', () => {
        this.isFullscreen()
      })
    })
  }

  destroyed(): void {
    this.$nextTick(() => {
      document.addEventListener('fullscreenchange', () => {
        this.isFullscreen()
      })
    })
  }

  toggleCollpase(): void {
    const isCollapseMenu = MenuCollapseModule.isCollapseMenu
    if (isCollapseMenu) {
      this.collapseMenuIcon = 'el-icon-s-fold'
    } else {
      this.collapseMenuIcon = 'el-icon-s-unfold'
    }
    MenuCollapseModule.changeCollapseMenu(!isCollapseMenu)
    console.log('MenuCollapseModule.getCollapseMenuState:', MenuCollapseModule.getCollapseMenuState)
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

  // 判断是否全屏
  public isFullscreen(): boolean {
    const doc: any = document
    if (doc.fullscreenElement) {
      this.fullScrollClass = 'iconfont icon-quanping_o'
      this.tipFullScrollContent = '退出全屏'
      return true
    } else {
      this.fullScrollClass = 'iconfont icon-quxiaoquanping_o'
      this.tipFullScrollContent = '全屏浏览'
      return false
    }
  }

  // 点击按钮全屏事件
  public fullScroll(): void {
    if (this.isFullscreen()) {
      const doc: any = document
      // 是全屏就退出全屏
      if (doc.exitFullscreen) {
        doc.exitFullscreen()
      } else if (doc.mozCancelFullScreen) {
        const docAny = doc as any
        docAny.mozCancelFullScreen()
      } else if (doc.webkitCancelFullScreen) {
        doc.webkitCancelFullScreen()
      }
    } else {
      const doc: any = document.documentElement
      // 否则将页面全屏
      if (doc.requestFullscreen) {
        doc.requestFullscreen()
      } else if (doc.mozRequestFullScreen) {
        // FireFox
        doc.mozRequestFullScreen()
      } else if (doc.webkitRequestFullScreen) {
        // Chrome等
        doc.webkitRequestFullScreen()
      } else if (doc.msRequestFullscreen) {
        // IE11
        doc.msRequestFullscreen()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.iconfont {
  font-family: 'iconfont' !important;
  font-size: 30px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
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
    span {
      margin-left: 15px;
    }
  }
  .fullscreen {
    padding: 0 10px;
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

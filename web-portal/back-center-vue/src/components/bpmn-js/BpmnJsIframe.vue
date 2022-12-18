<template>
  <div style="height: 100%">
    <el-dialog :visible.sync="dialogVisible" custom-class="dialogclass" :fullscreen="true" style="height:auto;" slot="header" :before-close="handleClose">
      <div style="height:auto;">
        <iframe id="bdIframe" :src=bpmnUrl frameborder="0" style="width:100%;height:100%;" scrolling="no"></iframe>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Watch, Vue } from 'vue-property-decorator'
import { local } from '@/store'
import settings from '@/settings'
@Component({
  components: {}
})
export default class KWBpmnJsIframe extends Vue {
  prefix = 'http://localhost:9013?serviceUrl=http://127.0.0.1:9200/api-activiti/&access_token=' + local.getStr(settings.accessToken)
  bpmnUrl = ''
  @Prop({ default: '', type: String })
  type!: string

  @Prop({ default: '', type: String })
  deploymentFileUUID!: string

  @Prop({ default: '', type: String })
  deploymentName!: string

  @Prop({ default: '', type: String })
  instanceId!: string

  dialogVisible = false

  @Prop({ default: false, type: Boolean })
  showBpmn!: boolean

  @Watch('showBpmn')
  onShowBpmn(): void {
    if (this.showBpmn) {
      this.dialogVisible = this.showBpmn
      this.resetWindows()
    }
  }

  handleClose(): void {
    this.$confirm('确认关闭？')
      .then(value => {
        this.dialogVisible = false
        console.log(value)
      })
      .catch(err => {
        console.log(err)
      })
  }

  mount(): void {
    this.resetWindows()
  }

  resetWindows(): void {
    setTimeout(() => {
      this.bpmnUrl = this.prefix + '&instanceId=' + this.instanceId + '&type=' + this.type + '&deploymentFileUUID=' + this.deploymentFileUUID + '&deploymentName=' + encodeURI(this.deploymentName) + '&date=' + new Date().getTime()
      console.log('---------')
      /**
       * iframe-宽高自适应显示`
       */
      const oIframe = document.getElementById('bdIframe') as HTMLElement
      console.log(oIframe)
      const deviceWidth = document.documentElement.clientWidth
      const deviceHeight = document.documentElement.clientHeight
      // oIframe.style.width = Number(deviceWidth) - 220 + "px"; //数字是页面布局宽度差值
      oIframe.style.width = Number(deviceWidth) + 'px' // 数字是页面布局宽度差值
      oIframe.style.height = Number(deviceHeight) + 'px' // 数字是页面布局高度差
    }, 1000)
  }
}
</script>

<style lang="less" >
.dialogclass {
  .el-dialog__body {
    padding: 0px !important;
  }
  .el-dialog__header {
    padding: 0px !important;
  }
  .dialog-footer {
    height: 0px !important;
  }
}
</style>

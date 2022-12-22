<template>
  <div id="app" style="height: 100%">
    <el-button type="text" @click="edit()">点击打开 Dialog</el-button>
    <el-button type="text" @click="showDynamicForm()">动态form</el-button>
    <el-dialog :visible.sync="dialogVisible" custom-class="dialogclass" :fullscreen="true" style="height:auto;" slot="header" :before-close="handleClose">
      <div style="height:auto;">
        <iframe id="bdIframe" :src=bpmnUrl frameborder="0" style="width:100%;height:100%;" scrolling="no"></iframe>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogDynamicFormVisible" title="动态表单" style="height:auto;" :before-close="handleClose1" width="20%">
      <div style="height:auto;">
        <KWDynamicForm :formItems='formItems' :dynamicFormRules='rules' ref='dynamicForm' :inputFormData='inputFormData'></KWDynamicForm>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogDynamicFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="getDynamicFormDatas()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from 'vue-property-decorator'
import { local } from '@/store'
import settings from '@/settings'
import KWDynamicForm from '@/components/dynamic-form/DynamicForm.vue'
@Component({
  components: {
    KWDynamicForm
  }
})
export default class BpmnJsHome extends Vue {
  dialogVisible = false
  dialogDynamicFormVisible = false
  bpmnUrl = 'http://localhost:9013?serviceUrl=http://127.0.0.1:9200/api-activiti/&access_token=' + local.getStr(settings.accessToken)

  formItems = [
    {
      label: '省份名称',
      type: 'text',
      model: 'cityName'
    },
    {
      label: '选择时间',
      type: 'date',
      model: 'time1',
      pickerDate: {
        type: 'daterange',
        formatValue: 'yyyy-MM-dd'
      }
    },
    {
      label: '选择城市',
      type: 'select',
      model: 'city',
      func: true,
      opts: [
        {
          label: '哈尔滨',
          value: '1'
        },
        {
          label: '齐齐哈尔',
          value: '2'
        },
        {
          label: '牡丹江',
          value: '3'
        },
        {
          label: '佳木斯',
          value: '4'
        }
      ]
    },
    {
      label: '选择城市',
      type: 'radio',
      model: 'city',
      func: true,
      opts: [
        {
          label: '哈尔滨',
          value: '1'
        },
        {
          label: '齐齐哈尔',
          value: '2'
        },
        {
          label: '牡丹江',
          value: '3'
        },
        {
          label: '佳木斯',
          value: '4'
        }
      ]
    },
    {
      label: '喜欢的运动',
      type: 'checkbox',
      model: 'sports',
      opts: [
        {
          label: '篮球',
          value: '1',
          name: 'ball'
        },
        {
          label: '羽毛球',
          value: '2',
          name: 'ball'
        },
        {
          label: '足球',
          value: '3',
          name: 'ball'
        }
      ]
    }
  ]

  inputFormData = { cityName: 'test', time1: '2022-12-12', city: '4', sports: ['1'] }

  rules = {
    cityName: [{ required: true, message: '请输入cityName', trigger: 'blur' }]
  }

  @Ref('dynamicForm')
  readonly dynamicForm!: KWDynamicForm

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

  handleClose1(): void {
    this.dialogDynamicFormVisible = false
    // this.$confirm('确认关闭？')
    //   .then(value => {
    //     console.log(value)
    //   })
    //   .catch(err => {
    //     console.log(err)
    //   })
  }

  edit(): void {
    this.dialogVisible = true

    setTimeout(function () {
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

  showDynamicForm(): void {
    this.dialogDynamicFormVisible = true
  }

  getDynamicFormDatas(): void {
    console.log('get...')
    this.dynamicForm.dynamicFormRef.validate(valid => {
      console.log(valid)
      if (!valid) {
        return false
      }
      console.log(this.dynamicForm.dynamicFormData)
      // this.dialogDynamicFormVisible = false
    })
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

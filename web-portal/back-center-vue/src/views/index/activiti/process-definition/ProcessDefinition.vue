<template>
  <div style="height: 100%">
    <div class="navigation-breadcrumb">
      <div>部署管理</div>
      <el-breadcrumb>
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>部署管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入内容" v-model="t.name" v-hasPermissionQueryPage="processDefinitionPermissionPrefix">
            <el-button slot="append" class="search-primary" icon="el-icon-search" @click="searchProcessDefinition"></el-button>
          </el-input>
        </el-col>
        <el-col :span="7">
          <el-button type="primary" v-hasPermissionAdd="processDefinitionPermissionPrefix" @click="activitiUpload">上传流程文件</el-button>
          <el-button type="primary" v-hasPermissionAdd="processDefinitionPermissionPrefix" @click="activitiDraw">在线绘制流程文件</el-button>
        </el-col>
      </el-row>
      <KWTable url="/api-activiti/processDefinitionCtrl/getDefinitions" v-hasPermissionQueryPage="processDefinitionPermissionPrefix" style="width: 100%" ref="kwTableRef">
        <el-table-column type="index" width="80" label="序号"></el-table-column>
        <el-table-column prop="id" sortable="custom" label="流程定义ID"> </el-table-column>
        <el-table-column prop="deploymentId" sortable="custom" label="部署ID"> </el-table-column>
        <el-table-column prop="name" label="流程定义名称" sortable="custom"></el-table-column>
        <el-table-column prop="resourceName" label="流程文件名称" sortable="custom"></el-table-column>
        <el-table-column prop="key" label="KEY" sortable="custom"></el-table-column>
        <el-table-column prop="version" label="部署版本" sortable="custom"></el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-view" v-hasPermissionUpdate="processDefinitionPermissionPrefix" size="mini" @click="showProcessDefintionDetailDialog(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" v-hasPermissionDelete="processDefinitionPermissionPrefix" size="mini" @click="deleteProcessDefinition(scope.row)">
            </el-button>
            <el-tooltip effect="dark" content="启动实例" v-if="hasPermission(scope.row)" placement="top" :enterable="false">
              <el-button type="warning" icon="el-icon-video-play" size="mini" @click="showProcessDefintionDetailEditDialog(scope.row)">
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </KWTable>
    </el-card>
    <el-dialog :title="title" @close="aditFileClosed" :visible.sync="fileDialogVisble" width="20%">
      <el-upload class="upload-demo" ref="upload" action="" :on-preview="handlePreview" :on-remove="handleRemove" :on-success="uploadSuccess" :http-request="httpRequest" :file-list="fileList" :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="fileDialogVisble = false; clearFiles()">取 消</el-button>
        <el-button type="primary" @click="submitUpload">上传到服务器</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="title" @close="aditPocessInstanceClosed" :visible.sync="processInstanceEditVisble" width="20%">
      <el-form :model="processInstanceForm" :rules="processInstanceFormRules" ref="processInstanceFormRef" label-width="90px">
        <el-form-item label="实例名称" prop="name">
          <el-input v-model="processInstanceForm.name" style="max-width: 220px;">
          </el-input>
        </el-form-item>
        <!-- <el-form-item label="实例变量" prop="variable">
          <el-input v-model="processInstanceForm.variable" style="max-width: 220px;"></el-input>
        </el-form-item> -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="processInstanceEditVisble = false">取 消</el-button>
        <el-button type="primary" @click="startProcessInstance">确 定</el-button>
      </span>
    </el-dialog>
    <KWBpmnJsIframe :showBpmn="showBpmn" :deploymentFileUUID="deploymentFileUUID" :type="type" :deploymentName="deploymentName"></KWBpmnJsIframe>
  </div>
</template>

<script lang="ts">
import { ElForm } from 'element-ui/types/form'
import { Component, Vue, Ref } from 'vue-property-decorator'
import { ElUpload, ElUploadInternalFileDetail, HttpRequestOptions } from 'element-ui/types/upload'
import KWTable from '@/components/table/Table.vue'
import KWBpmnJsIframe from '@/components/bpmn-js/BpmnJsIframe.vue'
import PermissionUtil from '@/common/utils/permission/permission-util'
import PermissionPrefixUtils from '@/common/utils/permission/permission-prefix'
import { ProcessDefinitionDetail } from './interface/process-definition'
import { DeleteProcessDefinitionApi, ProcessDefinitionUploadApi } from './process-definition-api'
import { ProcessInstanceForm } from '../process-instance/interface/process-instance'
import { StartProcessInstanceApi } from '../process-instance/process-instance-api'
import FormValidatorRule from '@/common/form-validator/form-validator'
@Component({
  components: {
    KWTable,
    KWBpmnJsIframe
  }
})
export default class ProcessDefinition extends Vue {
  t: ProcessDefinitionDetail = {
    deploymentId: '',
    resourceName: '',
    key: '',
    id: '',
    name: '',
    version: 0
  }

  @Ref('upload')
  readonly upload!: ElUpload

  fileList: Array<ElUploadInternalFileDetail> = []
  processDefinitionPermissionPrefix = PermissionPrefixUtils.processDefinition
  title = ''
  fileDialogVisble = false
  processInstanceEditVisble = false
  processInstanceForm: ProcessInstanceForm = {
    variables: new Map(),
    businessKey: '',
    processDefinitionId: '',
    processDefinitionKey: '',
    name: '',
    variable: ''
  }

  showBpmn = false
  deploymentFileUUID = ''
  deploymentName = ''
  type = ''

  @Ref('processInstanceFormRef')
  readonly processInstanceFormRef!: ElForm

  @Ref('kwTableRef')
  readonly kwTableRef!: KWTable<ProcessDefinitionDetail, ProcessDefinitionDetail>

  hasPermission(data: ProcessDefinition): boolean {
    return true
  }

  hasPermissionEnabled(): boolean {
    return PermissionUtil.hasPermissionForEnabled(this.processDefinitionPermissionPrefix)
  }

  readonly processInstanceFormRules: { name: Array<KWRule.Rule | KWRule.MixinRule> } = {
    name: [FormValidatorRule.requiredRule('请输入实例名称'), FormValidatorRule.mixinRul(2, 10, '实例名称的长度2~10个字符之间')]
  }

  aditFileClosed(): void {
    this.fileDialogVisble = false
  }

  aditProcessDefinitionClosed(): void {
    this.fileDialogVisble = false
    this.processInstanceFormRef.resetFields()
  }

  showProcessDefintionDetailDialog(data: ProcessDefinitionDetail): void {
    this.deploymentFileUUID = data.deploymentId
    this.deploymentName = data.resourceName
    this.showBpmn = true
    this.type = 'lookBpmn'
    setTimeout(() => {
      this.showBpmn = false
    }, 1000)
  }

  deleteProcessDefinition(data: ProcessDefinitionDetail): void {
    this.$confirm('确定要删除, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(async () => {
        const { code, msg } = await DeleteProcessDefinitionApi(data.id, data.deploymentId)
        if (code !== 200) {
          this.$message.error(msg || '删除失败!')
        } else {
          this.searchProcessDefinition()
          this.$message.success('删除成功!')
        }
      })
      .catch(e => {
        console.log(e)
        this.$message.error('删除失败!')
      })
  }

  searchProcessDefinition(): void {
    this.kwTableRef.loadByCondition(this.t)
  }

  activitiDraw(): void {
    this.deploymentFileUUID = ''
    this.deploymentName = ''
    this.showBpmn = true
    this.type = 'addBpmn'
    setTimeout(() => {
      this.showBpmn = false
    }, 1000)
  }

  activitiUpload(): void {
    this.title = '上传文件'
    this.fileDialogVisble = true
  }

  submitUpload(): void {
    this.upload.submit()
  }

  handleRemove(file: ElUploadInternalFileDetail, fileList: Array<ElUploadInternalFileDetail>): void {
    console.log(file, fileList)
  }

  handlePreview(file: ElUploadInternalFileDetail): void {
    console.log(file)
  }

  async httpRequest(params: HttpRequestOptions): Promise<void> {
    console.log(params.file) // 拿到上传的文件
    var formdata = new FormData()
    formdata.append('processFile', params.file)
    const { code, msg } = await ProcessDefinitionUploadApi(formdata)
    if (code !== 200) {
      this.$message.error(msg || '上传失败!')
    } else {
      this.searchProcessDefinition()
      this.$message.success('上传成功!')
      this.fileDialogVisble = false
    }
  }

  uploadSuccess(): void {
    this.clearFiles()
  }

  clearFiles(): void {
    this.upload.clearFiles()
  }

  aditPocessInstanceClosed(): void {
    this.processInstanceEditVisble = false
    this.processInstanceFormRef.resetFields()
  }

  // 展示编辑用于的对话框
  showProcessDefintionDetailEditDialog(data: ProcessDefinitionDetail): void {
    this.title = '启动流程部署信息'
    this.processInstanceEditVisble = true
    this.processInstanceForm.processDefinitionKey = data.key
    this.processInstanceForm.processDefinitionId = data.id
  }

  startProcessInstance(): void {
    this.processInstanceFormRef.validate(async valid => {
      if (!valid) {
        return false
      }
      const { code, msg } = await StartProcessInstanceApi(this.processInstanceForm)
      if (code !== 200) {
        this.$message.error(msg || '启动实例失败!')
      } else {
        this.processInstanceEditVisble = false
        this.searchProcessDefinition()
        this.$message.success('启动实例成功!')
      }
    })
  }
}
</script>

<style lang="less" >
.search-primary {
  background: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
}
.search-primary:focus,
.search-primary:hover {
  background: #66b1ff !important;
  border-color: #66b1ff !important;
  color: #fff !important;
}
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

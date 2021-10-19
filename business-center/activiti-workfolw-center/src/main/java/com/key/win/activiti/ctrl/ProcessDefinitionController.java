package com.key.win.activiti.ctrl;


import com.key.win.activiti.config.GlobalMappingConfig;
import com.key.win.activiti.service.FormDataService;
import com.key.win.activiti.service.ProcessDefinitionService;
import com.key.win.activiti.vo.ProcessDefinitionVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.ApiOperation;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/processDefinitionCtrl/*")
public class ProcessDefinitionController {

    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private GlobalMappingConfig globalMappingConfig;

    @Autowired
    private FormDataService formDataService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;


    @PostMapping(value = "/uploadStreamAndDeployment")
    public Result uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile) {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = multipartFile.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//创建处理引擎实例
            // repositoryService = processEngine.getRepositoryService();//创建仓库服务实例

            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()//初始化流程
                        .addZipInputStream(zip)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()//初始化流程
                        .addInputStream(fileName, fileInputStream)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            }

            return Result.succeed("部署流程成功！");

        } catch (Exception e) {
            log.error("部署流程失败:" + e.getMessage(), e);
            return Result.failed("部署流程失败:" + e.getMessage());
        }
    }


    @PostMapping(value = "/upload")
    public Result upload(HttpServletRequest request, @RequestParam("processFile") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            System.out.println("文件为空");
        }
        String fileName = multipartFile.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = globalMappingConfig.getBpmnUploadPath(); // 上传后的路径

        //本地路径格式转上传路径格式
        filePath = filePath.replace("\\", "/");
        filePath = filePath.replace("file:", "");

        // String filePath = request.getSession().getServletContext().getRealPath("/") + "bpmn/";
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File file = new File(filePath + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String bpmnUrl = globalMappingConfig.getBpmnUrlPrefix()+fileName;
        try {
            multipartFile.transferTo(file);
            return Result.succeed(bpmnUrl,"");
        } catch (Exception e) {
            log.error(fileName + "上传出错！" + e.getMessage(), e);
            return Result.failed(fileName + "上传出错！" + e.getMessage());
        }
    }


    /**
     * @param deploymentFileUUID
     * @return
     */
    @PostMapping(value = "/addDeploymentByFileNameBPMN")
    public Result addDeploymentByFileNameBPMN(@RequestParam("deploymentFileUUID") String deploymentFileUUID, @RequestParam("deploymentName") String deploymentName) {
        try {
            String filename = "resources/bpmn/" + deploymentFileUUID;
            Deployment deployment = repositoryService.createDeployment()//初始化流程
                    .addClasspathResource(filename)
                    .name(deploymentName)
                    .deploy();
            //System.out.println(deployment.getName());
            return Result.succeed(deployment.getId());
        } catch (Exception e) {
            log.error("BPMN部署流程失败:" + e.getMessage(), e);
            return Result.succeed("BPMN部署流程失败:" + e.getMessage());
        }

    }

    @PostMapping(value = "/addDeploymentByString")
    public Result addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                    .name("不知道在哪显示的部署名称")
                    .deploy();
            //System.out.println(deployment.getName());
            return Result.succeed(deployment.getId());
        } catch (Exception e) {
            log.error("string部署流程失败:" + e.getMessage(), e);
            return Result.succeed("部署流程失败:" + e.getMessage());
        }
    }

//缺失流程部署ID属性版本，import org.activiti.api.process.model.ProcessDefinition;
    /*@GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions() {

        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            Page<ProcessDefinition> processDefinitions = processRuntime.processDefinitions(Pageable.of(0, 50));
            System.out.println("流程定义数量： " + processDefinitions.getTotalItems());
            for (ProcessDefinition pd : processDefinitions.getContent()) {
                System.out.println("getId：" + pd.getId());
                System.out.println("getName：" + pd.getName());
                System.out.println("getStatus：" + pd.getKey());
                System.out.println("getStatus：" + pd.getFormKey());
            }


            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processDefinitions.getContent());
        }catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程定义失败", e.toString());
        }
    }*/


    //import org.activiti.engine.RepositoryService;
    @ApiOperation("分页")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    @PostMapping(value = "/getDefinitions")
    public PageResult<ProcessDefinitionVo> getDefinitions(@RequestBody PageRequest<ProcessDefinitionVo> t) {
        return processDefinitionService.findProcessDefinitionByPaged(t);

    }

    //获取流程定义XML
    @GetMapping(value = "/getDefinitionXML")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName) {


        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            log.error("获取流程定义XML出错：" + e.getMessage(), e);
        }
    }


    @GetMapping(value = "/getDeployments")
    public List<Deployment> getDeployments() {
        return repositoryService.createDeploymentQuery().list();
    }


    //删除流程定义
    @GetMapping(value = "/delDefinition")
    public Result delDefinition(@RequestParam("depId") String depId, @RequestParam("pdId") String pdId) {
        try {

            //删除数据
            formDataService.deleteFormDataByProcDefId(depId);
            repositoryService.deleteDeployment(depId, true);
            return Result.succeed("删除成功");


        } catch (Exception e) {
            log.error("删除失败:" + e.getMessage(), e);
            return Result.failed("删除失败:" + e.getMessage());
        }
    }

}

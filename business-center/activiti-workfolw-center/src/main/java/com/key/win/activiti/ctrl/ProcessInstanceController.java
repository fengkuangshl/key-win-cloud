package com.key.win.activiti.ctrl;

import com.key.win.activiti.service.ProcessInstanceService;
import com.key.win.activiti.util.SecurityUtil;
import com.key.win.activiti.vo.ProcessInstanceStartFormVo;
import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/processInstanceCtrl")
@Api("工作流实例相关的api")
public class ProcessInstanceController {
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessInstanceService processInstanceService;


    @PostMapping(value = "/getInstances")
    @ApiOperation(value = "获取工作流实例分页")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public PageResult<ProcessInstanceVo> getInstances(@RequestBody PageRequest<ProcessInstanceVo> t) {
        return processInstanceService.findProcessInstanceByPaged(t);
    }


    //启动
    @GetMapping(value = "/startProcess")
    @ApiOperation(value = "启动工作流实例")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                               @RequestParam("instanceName") String instanceName,
                               @RequestParam("instanceVariable") String instanceVariable) {
        try {
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    //.withVariable("content", instanceVariable)
                    //.withVariable("参数2", "参数2的值")
                    .withBusinessKey("自定义BusinessKey")
                    .build());
            return Result.succeed("工作流启动成功！" );
        } catch (Exception e) {
            log.error("创建工作流实例失败:" + e.getMessage(), e);
            return Result.failed("创建工作流实例失败:" + e.getMessage());
        }
    }

    //启动
    @PostMapping(value = "/startProcess")
    @ApiOperation(value = "启动工作流实例")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result startProcessPost(@RequestBody ProcessInstanceStartFormVo processInstanceRequest) {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if(loginAppUser == null){
            throw new RuntimeException("用户不存在！");
        }
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("createUser",loginAppUser.getUsername());
            org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.startProcessInstanceById(processInstanceRequest.getProcessDefinitionId(),map);
            runtimeService.setProcessInstanceName(processInstance.getId(),processInstanceRequest.getName());
            if(!CollectionUtils.isEmpty(processInstanceRequest.getVariables())){
                runtimeService.setVariables(processInstance.getId(),processInstanceRequest.getVariables());
            }
            return Result.succeed("工作流启动成功！" );
        } catch (Exception e) {
            log.error("创建工作流实例失败:" + e.getMessage(), e);
            return Result.failed("创建工作流实例失败:" + e.getMessage());
        }
    }

    //删除
    @DeleteMapping(value = "/deleteInstance/{instanceId}")
    @ApiOperation(value = "删除工作流实例")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result deleteInstance(@PathVariable("instanceId") String instanceId) {
        try {
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("删除工作流实例成功！");
        } catch (Exception e) {
            log.error("删除工作流实例失败:" + e.getMessage(), e);
            return Result.failed("删除工作流实例失败:" + e.getMessage());
        }

    }

    //挂起冷冻
    @GetMapping(value = "/suspendInstance/{instanceId}")
    @ApiOperation(value = "挂起工作流实例")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result suspendInstance(@PathVariable("instanceId") String instanceId) {

        try {
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("工作流实例挂起成功！");
        } catch (Exception e) {
            log.error("挂起工作流实例失败:" + e.getMessage(), e);
            return Result.failed("挂起工作流实例失败:" + e.getMessage());
        }
    }

    //激活
    @GetMapping(value = "/resumeInstance/{instanceId}")
    @ApiOperation(value = "激活工作流实例")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result resumeInstance(@PathVariable("instanceId") String instanceId) {

        try {

            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("工作流实例激活成功！");
        } catch (Exception e) {
            log.error("激活工作流实例失败:" + e.getMessage(), e);
            return Result.failed("激活工作流实例失败:" + e.getMessage());
        }
    }


    //获取参数
    @GetMapping(value = "/variables/{instanceId}")
    @ApiOperation(value = "获取工作流实例参数")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result variables(@PathVariable("instanceId") String instanceId) {
        List<VariableInstance> variableInstance = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(instanceId)
                .build());
        return Result.succeed(variableInstance);
    }


}

package com.key.win.activiti.ctrl;

import com.key.win.activiti.service.ProcessInstanceService;
import com.key.win.activiti.util.SecurityUtil;
import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/processInstanceCtrl")
public class ProcessInstanceController {
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessInstanceService processInstanceService;


    @PostMapping(value = "/getInstances")
    public PageResult<ProcessInstanceVo> getInstances(@RequestBody PageRequest<ProcessInstanceVo> t) {
        return processInstanceService.findProcessInstanceByPaged(t);
    }


    //启动
    @GetMapping(value = "/startProcess")
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
            return Result.succeed("流程启动成功！" );
        } catch (Exception e) {
            log.error("创建流程实例失败:" + e.getMessage(), e);
            return Result.failed("创建流程实例失败:" + e.getMessage());
        }
    }

    //删除
    @GetMapping(value = "/deleteInstance")
    public Result deleteInstance(@RequestParam("instanceId") String instanceId) {
        try {

            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("删除流程实例成功！");
        } catch (Exception e) {
            log.error("删除流程实例失败:" + e.getMessage(), e);
            return Result.failed("删除流程实例失败:" + e.getMessage());
        }

    }

    //挂起冷冻
    @GetMapping(value = "/suspendInstance")
    public Result suspendInstance(@RequestParam("instanceId") String instanceId) {

        try {
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("流程实例挂起成功！");
        } catch (Exception e) {
            log.error("挂起流程实例失败:" + e.getMessage(), e);
            return Result.failed("挂起流程实例失败:" + e.getMessage());
        }
    }

    //激活
    @GetMapping(value = "/resumeInstance")
    public Result resumeInstance(@RequestParam("instanceId") String instanceId) {

        try {

            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceId)
                    .build()
            );
            return Result.succeed("流程实例激活成功！");
        } catch (Exception e) {
            log.error("激活流程实例失败:" + e.getMessage(), e);
            return Result.failed("激活流程实例失败:" + e.getMessage());
        }
    }


    //获取参数
    @GetMapping(value = "/variables")
    public List<VariableInstance> variables(@RequestParam("instanceId") String instanceId) {
        List<VariableInstance> variableInstance = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(instanceId)
                .build());

        return variableInstance;
    }


}

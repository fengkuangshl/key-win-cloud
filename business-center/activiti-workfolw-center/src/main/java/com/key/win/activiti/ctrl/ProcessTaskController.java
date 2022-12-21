package com.key.win.activiti.ctrl;

import com.key.win.activiti.model.FormData;
import com.key.win.activiti.service.FormDataService;
import com.key.win.activiti.service.ProcessRuntimeService;
import com.key.win.activiti.service.ProcessTaskService;
import com.key.win.activiti.vo.DynamicFormVo;
import com.key.win.activiti.vo.ProcessTaskFormVo;
import com.key.win.activiti.vo.ProcessTaskVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/processTaskCtrl")
@Api("工作流任务相关的api")
public class ProcessTaskController {
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private ProcessTaskService processTaskService;

    @Autowired
    private TaskService taskService;


    //获取我的代办任务
    @PostMapping(value = "/getTasks")
    @ApiOperation(value = "获取我的代办任务分页")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public PageResult<ProcessTaskVo> getTasks(@RequestBody PageRequest<ProcessTaskVo> t) {
        return processTaskService.findProcessTaskByPaged(t);
    }

    //完成待办任务
    @GetMapping(value = "/completeTask/{taskId}")
    @ApiOperation(value = "完成待办任务")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result completeTask(@PathVariable("taskId") String taskId) {
        try {
            Task task = taskRuntime.task(taskId);
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    //.withVariable("num", "2")//执行环节设置变量
                    .build());


            return Result.succeed("完成待办任务");
        } catch (Exception e) {
            log.error("完成失败:" + e.getMessage(), e);
            return Result.failed("完成失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/trunTask")
    @ApiOperation(value = "转签任务")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result trunTask(@RequestBody ProcessTaskFormVo processTaskForm) {
        try {
            if (StringUtils.isNotBlank(processTaskForm.getAudit())) {
                taskService.addComment(processTaskForm.getTaskId(), processTaskForm.getProcessInstanceId(), processTaskForm.getAudit());
            }
            taskService.setAssignee(processTaskForm.getTaskId(), processTaskForm.getAssignee());
            return Result.succeed("转签任务成功！");
        } catch (Exception e) {
            log.error("转签任务失败:" + e.getMessage(), e);
            return Result.failed("转签任务失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/giveBackTask")
    @ApiOperation(value = "退回任务")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result giveBackTask(@RequestBody ProcessTaskFormVo processTaskForm) {
        try {
            Task task = taskRuntime.task(processTaskForm.getTaskId());
            if (task.getClaimedDate() != null) {

                if (StringUtils.isNotBlank(processTaskForm.getAudit())) {
                    taskService.addComment(processTaskForm.getTaskId(), processTaskForm.getProcessInstanceId(), processTaskForm.getAudit());
                }
                taskService.setAssignee(processTaskForm.getTaskId(), null);

                return Result.succeed("退还任务成功！");
            }
            return Result.failed("任务不能退还！");

        } catch (Exception e) {
            log.error("退回任务失败:" + e.getMessage(), e);
            return Result.failed("退回任务失败:" + e.getMessage());
        }
    }


    //完成待办任务
    @PostMapping(value = "/completeTask")
    @ApiOperation(value = "完成待办任务")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result completeTask(@RequestBody ProcessTaskFormVo processTaskForm) {
        try {
            Task task = taskRuntime.task(processTaskForm.getTaskId());
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }

            if (StringUtils.isNotBlank(processTaskForm.getAudit())) {
                taskService.addComment(processTaskForm.getTaskId(), task.getProcessInstanceId(), processTaskForm.getAudit());
            }

            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    //.withVariable("num", "2")//执行环节设置变量
                    .build());

            return Result.succeed("完成待办任务");
        } catch (Exception e) {
            log.error("完成失败:" + e.getMessage(), e);
            return Result.failed("完成失败:" + e.getMessage());
        }
    }

}

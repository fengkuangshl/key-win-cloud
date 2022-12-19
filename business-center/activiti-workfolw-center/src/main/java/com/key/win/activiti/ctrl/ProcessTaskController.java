package com.key.win.activiti.ctrl;

import com.key.win.activiti.model.FormData;
import com.key.win.activiti.service.FormDataService;
import com.key.win.activiti.service.ProcessRuntimeService;
import com.key.win.activiti.service.ProcessTaskService;
import com.key.win.activiti.vo.DynamicFormVo;
import com.key.win.activiti.vo.ProcessTaskFormVo;
import com.key.win.activiti.vo.ProcessTaskVo;
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
    private RepositoryService repositoryService;

    @Autowired
    private FormDataService formDataService;

    @Autowired
    private ProcessRuntimeService runtimeService;

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

    //渲染表单
    @GetMapping(value = "/formDataShow/{taskId}")
    @ApiOperation(value = "渲染表单")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result formDataShow(@PathVariable("taskId") String taskId) {
        try {

            Task task = taskRuntime.task(taskId);

//-----------------------构建表单控件历史数据字典------------------------------------------------
            //本实例所有保存的表单数据HashMap，为了快速读取控件以前环节存储的值
            HashMap<String, String> controlistMap = new HashMap<>();
            //本实例所有保存的表单数据
            List<HashMap<String, Object>> tempControlList = formDataService.selectFormData(task.getProcessInstanceId());

            for (HashMap ls : tempControlList) {
                //String Control_ID = ls.get("Control_ID_").toString();
                //String Control_VALUE = ls.get("Control_VALUE_").toString();
                controlistMap.put(ls.get("Control_ID_").toString(), ls.get("Control_VALUE_").toString());
            }
            //String controlistMapValue = controlistMap.get("控件ID");
            //controlistMap.containsKey()

            //

/*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2-_!类型-_!名称-_!默认值-_!是否参数
            例子：
            FormProperty_0lovri0-_!string-_!姓名-_!请输入姓名-_!f
            FormProperty_1iu6onu-_!int-_!年龄-_!请输入年龄-_!s

            默认值：无、字符常量、FormProperty_开头定义过的控件ID
            是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
            注：类型是可以获取到的，但是为了统一配置原则，都配置到
            */

            //注意!!!!!!!!:表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key“task.getFormKey()”当做任务key
            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    //.getFlowElement(task. getFormKey());
                    .getFlowElement(task.getTaskDefinitionKey());


            List<FormProperty> formProperties = userTask.getFormProperties();
            if (CollectionUtils.isEmpty(formProperties)) {
                return Result.succeed("non-from", "无表单");
            }
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (FormProperty fp : formProperties) {
                String[] splitFP = fp.getId().split("-_!");

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", splitFP[0]);
                hashMap.put("controlType", splitFP[1]);
                hashMap.put("controlLabel", splitFP[2]);


                //默认值如果是表单控件ID
                if (splitFP[3].startsWith("FormProperty_")) {
                    //控件ID存在
                    if (controlistMap.containsKey(splitFP[3])) {
                        hashMap.put("controlDefValue", controlistMap.get(splitFP[3]));
                    } else {
                        //控件ID不存在
                        hashMap.put("controlDefValue", "读取失败，检查" + splitFP[0] + "配置");
                    }
                } else {
                    //默认值如果不是表单控件ID则写入默认值
                    hashMap.put("controlDefValue", splitFP[3]);
                }


                hashMap.put("controlIsParam", splitFP[4]);
                listMap.add(hashMap);
            }

            return Result.succeed(listMap, "获取表单数据成功");
        } catch (Exception e) {
            log.error("获取表单数据失败:" + e.getMessage(), e);
            return Result.failed("获取表单数据失败:" + e.getMessage());
        }
    }

    //保存表单
    @PostMapping(value = "/formDataSave")
    @ApiOperation(value = "保存表单")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result formDataSave(@RequestBody DynamicFormVo dynamicFormVo) {
        try {


            Task task = taskRuntime.task(dynamicFormVo.getTaskId());

            //formData:控件id-_!控件值-_!是否参数!_!控件id-_!控件值-_!是否参数
            //FormProperty_0lovri0-_!不是参数-_!f!_!FormProperty_1iu6onu-_!数字参数-_!s


            HashMap<String, Object> variables = new HashMap<String, Object>();
            Boolean hasVariables = false;//没有任何参数


            List<FormData> list = new ArrayList<>();

            //前端传来的字符串，拆分成每个控件
            //String[] formDataList = formData.split("!_!");//
            for (String controlItem : dynamicFormVo.getFormData()) {
                String[] formDataItem = controlItem.split("-_!");

                FormData fd = new FormData();
                fd.setProcDefId(task.getProcessDefinitionId());
                fd.setProcInstId(task.getProcessInstanceId());
                fd.setFormKey(task.getFormKey());
                fd.setControlId(formDataItem[0]);
                fd.setControlValue(formDataItem[1]);
                list.add(fd);

                //构建参数集合
                switch (formDataItem[2]) {
                    case "f":
                        log.info("控件值不作为参数");
                        break;
                    case "s":
                        variables.put(formDataItem[0], formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        variables.put(formDataItem[0], timeFormat.parse(formDataItem[2]));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[2]));
                        hasVariables = true;
                        break;
                    default:
                        log.info("控件参数类型配置错误：" + formDataItem[0] + "的参数类型不存在，" + formDataItem[2]);
                }
            }//for结束

            if (hasVariables) {
                //带参数完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(dynamicFormVo.getTaskId())
                        .withVariables(variables)
                        .build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(dynamicFormVo.getTaskId())
                        .build());
            }

            //写入数据库
            boolean result = formDataService.saveBatch(list);

            return Result.succeed(list, "保存表单成功！");
        } catch (Exception e) {
            log.error("保存表单失败:" + e.getMessage(), e);
            return Result.failed("保存表单失败:" + e.getMessage());
        }
    }

}

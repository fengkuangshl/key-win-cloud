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
@RequestMapping("/dynamicFormCtrl")
@Api("工作流任务相关的api")
public class DynamicFormController {
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormDataService formDataService;

    //渲染表单
    @GetMapping(value = "/getFormData/{instanceId}")
    @ApiOperation(value = "渲染表单")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result getFormDataByInstanceId(@PathVariable("instanceId") String instanceId) {
        FormData input = new FormData();
        input.setProcInstId(instanceId);
        List<FormData> formDataByList = formDataService.getFormDataByCondition(input);
        return Result.succeed(formDataByList);
    }


    //渲染表单
    @GetMapping(value = "/formDataShow/{taskId}")
    @ApiOperation(value = "渲染表单")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result formDataShow(@PathVariable("taskId") String taskId) {

        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if (loginAppUser == null) {
            throw new RuntimeException("用户不存在！");
        }
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
            FormProperty_0ueitp2-_!类型-_!名称-_!默认值-_!是否参数-_!是否只读
            例子：
            FormProperty_0lovri0-_!string-_!姓名-_!请输入姓名-_!f-_!f
            FormProperty_1iu6onu-_!int-_!年龄-_!请输入年龄-_!s-_!t

            默认值：无、字符常量、FormProperty_开头定义过的控件ID
            是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
            是否只读:t只读，f:可写(默认f，可以不写)
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
            List<FormData> formDataList = new ArrayList<FormData>();
            for (FormProperty fp : formProperties) {
                String[] splitFP = fp.getId().split("-_!");

                FormData formData = new FormData();
                formData.setProcInstId(task.getProcessInstanceId());
                formData.setProcTaskId(taskId);
                formData.setProcDefId(task.getProcessDefinitionId());
                formData.setFormKey(task.getFormKey());
                formData.setControlId(splitFP[0]);
                formData.setControlType(splitFP[1]);
                formData.setControlLabel(splitFP[2]);


                //默认值如果是表单控件ID
                if (splitFP[3].startsWith("FormProperty_")) {
                    //控件ID存在
                    if (controlistMap.containsKey(splitFP[3])) {
                        formData.setControlValue(controlistMap.get(splitFP[3]));
                    } else {
                        //控件ID不存在
                        formData.setControlValue("读取失败，检查" + splitFP[0] + "配置");
                    }
                } else if (splitFP[3].toLowerCase().equals("c_username")) {
                    formData.setControlValue(loginAppUser.getUsername());
                } else {
                    //默认值如果不是表单控件ID则写入默认值
                    formData.setControlValue(splitFP[3]);
                }

                formData.setControlValueParamType(splitFP[4]);
                if (splitFP.length == 6) {
                    if (splitFP[5].toLowerCase().equals("t") || splitFP[5].toLowerCase().equals("true")) {

                        formData.setIsReadOnlyControl(true);
                    } else {
                        formData.setIsReadOnlyControl(true);
                    }
                } else {
                    formData.setIsReadOnlyControl(true);
                }
                formDataList.add(formData);
            }

            return Result.succeed(formDataList, "获取表单数据成功");
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


            HashMap<String, Object> variables = new HashMap<String, Object>();
            Boolean hasVariables = false;//没有任何参数

            List<FormData> list = new ArrayList<>();
            for (FormData controlItem : dynamicFormVo.getFormData()) {
                FormData fd = new FormData();
                controlItem.setProcTaskId(task.getId());
                controlItem.setProcDefId(task.getProcessDefinitionId());
                controlItem.setProcInstId(task.getProcessInstanceId());
                controlItem.setFormKey(task.getFormKey());
                list.add(controlItem);

                //构建参数集合
                switch (controlItem.getControlValueParamType()) {
                    case "f":
                        log.info("控件值不作为参数");
                        break;
                    case "s":
                        variables.put(controlItem.getControlId(), controlItem.getControlValue());
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        variables.put(controlItem.getControlId(), timeFormat.parse(controlItem.getControlValue()));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(controlItem.getControlId(), BooleanUtils.toBoolean(controlItem.getControlValue()));
                        hasVariables = true;
                        break;
                    default:
                        log.info("控件参数类型配置错误：" + controlItem.getControlId() + "的参数类型不存在，" + controlItem.getControlValue());
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

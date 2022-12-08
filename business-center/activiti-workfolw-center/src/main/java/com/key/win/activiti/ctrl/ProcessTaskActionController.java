package com.key.win.activiti.ctrl;

import com.google.common.collect.Lists;
import com.key.win.activiti.service.ProcessRuntimeService;
import com.key.win.activiti.util.ActivitiConstant;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/processTaskActionController")
@Api("工作流动作任务相关的api")
public class ProcessTaskActionController {

    @Autowired
    private ProcessRuntimeService processRuntimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    @GetMapping("/getHistoryNode")
    @ApiOperation(value = "获取流程实例已执行节点", notes = "获取流程实例已执行节点")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result<List<Map<String, Object>>> getHistoryNode(@ApiParam("流程实例id") @RequestParam String instanceId,
                                                            @ApiParam("任务id") @RequestParam String taskId) {

        List<HistoricActivityInstance> tempList = new ArrayList<>();
        org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String currActivityId = task.getTaskDefinitionKey();

        // 添加发起人任务节点
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("nodeName", ActivitiConstant.PROCESS_CREATOR_NAME);
        tempMap.put("nodeId", ActivitiConstant.PROCESS_CREATOR_KEY);
        resultList.add(tempMap);

        // 添加上一步任务节点
        tempMap = new HashMap<>();
        tempMap.put("nodeName", ActivitiConstant.PROCESS_PREVIOUS_STEP_NAME);
        tempMap.put("nodeId", ActivitiConstant.PROCESS_PREVIOUS_STEP_KEY);
        resultList.add(tempMap);

        List<HistoricActivityInstance> activityFinishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .desc()
                .list();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        getReverseNodeList(currActivityId, tempList, bpmnModel, activityFinishedList);

        for (HistoricActivityInstance historicActivityInstance : tempList) {
            tempMap = new HashMap<>();
            tempMap.put("nodeName", historicActivityInstance.getActivityName());
            tempMap.put("nodeId", historicActivityInstance.getActivityId());
            if (!resultList.contains(tempMap)) {
                resultList.add(tempMap);
            }
        }
        return Result.succeed(resultList, "successful!!");
    }

    /**
     * 逆向获取执行过的任务节点：
     * 1、子流程只能获取到本子流程节点+主流程节点，不能获取兄弟流程任务节点；
     * 2、主流程任务节点不能获取子流程任务节点；
     *
     * @param currActivityId
     */
    private void getReverseNodeList(String currActivityId, List<HistoricActivityInstance> tempList, BpmnModel bpmnModel, List<HistoricActivityInstance> activityFinishedList) {
        // 获取当前节点的进线，通过进线查询上个节点
        FlowNode currFlow = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currActivityId);
        List<SequenceFlow> incomingFlows = currFlow.getIncomingFlows();

        // 找到上个任务节点
        for (SequenceFlow incomingFlow : incomingFlows) {
            String sourceNodeId = incomingFlow.getSourceRef();
            FlowNode sourceFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sourceNodeId);
            String gatewayType = sourceNodeId.substring(sourceNodeId
                    .lastIndexOf("_") + 1);
            if (sourceFlowNode instanceof ParallelGateway && "END".equals(gatewayType.toUpperCase())) {
                sourceNodeId = sourceNodeId.substring(0, sourceNodeId
                        .lastIndexOf("_")) + "_start";
                for (HistoricActivityInstance historicActivityInstance : activityFinishedList) {
                    if (historicActivityInstance.getActivityId().equals(sourceNodeId)) {
                        tempList.add(historicActivityInstance);
                    }
                }
                getReverseNodeList(sourceNodeId, tempList, bpmnModel, activityFinishedList);
            } else {
                for (HistoricActivityInstance historicActivityInstance : activityFinishedList) {
                    if (historicActivityInstance.getActivityId().equals(sourceNodeId)) {
                        tempList.add(historicActivityInstance);
                    }
                }
                getReverseNodeList(sourceNodeId, tempList, bpmnModel, activityFinishedList);
            }
        }
    }

    @GetMapping("/getPreOneIncomeNode")
    @ApiOperation(value = "驳回上一节点", notes = "驳回上一节点")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result getPreOneIncomeNode(@ApiParam("流程实例id")@RequestParam String taskId) {
        List<Map<String, String>> incomeNodes = new ArrayList<>();

        org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String currActivityId = task.getTaskDefinitionKey();

        // 获取当前用户任务节点
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        org.activiti.bpmn.model.Process process = bpmnModel.getProcesses().get(0);

        getIncomeNodesRecur(currActivityId, incomeNodes, process, false);

        FlowNode currFlow = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currActivityId);
        if (currFlow == null) {
            List<SubProcess> subProcessList = bpmnModel.getMainProcess().findFlowElementsOfType(SubProcess.class, true);
            for (SubProcess subProcess : subProcessList) {
                FlowElement flowElement = subProcess.getFlowElement(currActivityId);

                if (flowElement != null) {
                    currFlow = (FlowNode) flowElement;
                    break;
                }
            }
        }

        // 记录原活动方向
        List<SequenceFlow> oriSequenceFlows = Lists.newArrayList();
        oriSequenceFlows.addAll(currFlow.getOutgoingFlows());

        // 清理活动方向
        currFlow.getOutgoingFlows().clear();

        List<SequenceFlow> newSequenceFlows = Lists.newArrayList();

        for (int i = 0; i < incomeNodes.size(); i++) {
            Map<String, String> item = incomeNodes.get(i);
            String nodeId = item.get("id");

            // 获取目标节点
            FlowNode target = (FlowNode) bpmnModel.getFlowElement(nodeId);

            //如果不是同一个流程（子流程）不能驳回
            if (!(currFlow.getParentContainer().equals(target.getParentContainer()))) {
                continue;
            }

            // 建立新方向
            SequenceFlow newSequenceFlow = new SequenceFlow();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newSequenceFlow.setId(uuid);
            newSequenceFlow.setSourceFlowElement(currFlow);// 原节点
            newSequenceFlow.setTargetFlowElement(target);// 目标节点
            newSequenceFlows.add(newSequenceFlow);
        }
        ;

        currFlow.setOutgoingFlows(newSequenceFlows);

        // 拒接、通过、驳回指定节点
        taskService.complete(taskId);

        //恢复原方向
        currFlow.setOutgoingFlows(oriSequenceFlows);
        return Result.result(true);
    }

    /**
     * 递归遍历获取上个任务节点
     **/
    public void getIncomeNodesRecur(String currentNodeId, List<Map<String, String>> incomeNodes, Process process, boolean isAll) {
        FlowElement currentFlowElement = process.getFlowElement(currentNodeId);
        List<SequenceFlow> incomingFlows = null;
        if (currentFlowElement instanceof UserTask) {
            incomingFlows = ((UserTask) currentFlowElement).getIncomingFlows();
        } else if (currentFlowElement instanceof Gateway) {
            incomingFlows = ((Gateway) currentFlowElement).getIncomingFlows();
        } else if (currentFlowElement instanceof StartEvent) {
            incomingFlows = ((StartEvent) currentFlowElement).getIncomingFlows();
        }
        if (incomingFlows != null && incomingFlows.size() > 0) {
            incomingFlows.forEach(incomingFlow -> {
                String expression = incomingFlow.getConditionExpression();
                // 出线的上一节点
                String sourceFlowElementID = incomingFlow.getSourceRef();
                // 查询上一节点的信息
                FlowElement preFlowElement = process.getFlowElement(sourceFlowElementID);

                //用户任务
                if (preFlowElement instanceof UserTask) {
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put("id", preFlowElement.getId());
                    tempMap.put("name", preFlowElement.getName());
                    incomeNodes.add(tempMap);
                    if (isAll) {
                        getIncomeNodesRecur(preFlowElement.getId(), incomeNodes, process, true);
                    }
                }
                //排他网关
                else if (preFlowElement instanceof ExclusiveGateway) {
                    getIncomeNodesRecur(preFlowElement.getId(), incomeNodes, process, isAll);
                }
                //并行网关
                else if (preFlowElement instanceof ParallelGateway) {
                    getIncomeNodesRecur(preFlowElement.getId(), incomeNodes, process, isAll);
                }
            });
        }
    }

    //获取参数
    @GetMapping(value = "/revocation")
    @ApiOperation(value = "申请追回")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result revocation(@RequestParam("instanceId") String instanceId) {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if(loginAppUser == null){
            throw new RuntimeException("用户不存在！");
        }

        // 获取当前执行任务节点
        org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        Set<Execution> executions = list.stream().filter(execution -> execution.getActivityId() != null).collect(Collectors.toSet());

        Iterator<Execution> iterator = executions.iterator();
        while (iterator.hasNext()) {
            Execution execution = iterator.next();
            // 获取当前执行任务
            Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
            String comment = "【" + loginAppUser.getNickname() + "】追回了该申请";
            handleResult(task.getId(), instanceId, ActivitiConstant.HANDLE_STATUS_YCX, comment, task.getTaskDefinitionKey(), loginAppUser.getUsername(), execution.getId());
        }

        runtimeService.deleteProcessInstance(instanceId, ActivitiConstant.HANDEL_RESULT_CX);
        return Result.result(true);
    }

    @GetMapping(value = "/handleCancellation")
    @ApiOperation(value = "申请作废")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result handleCancellation(@RequestParam("processInstanceId") String instanceId) {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if(loginAppUser == null){
            throw new RuntimeException("用户不存在！");
        }

        // 获取当前执行任务节点
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        Set<Execution> executions = list.stream().filter(execution -> execution.getActivityId() != null).collect(Collectors.toSet());

        Iterator<Execution> iterator = executions.iterator();
        while (iterator.hasNext()) {
            Execution execution = iterator.next();
            // 获取当前执行任务
            Task task = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
            String comment = "【" + loginAppUser.getNickname() + "】作废了该申请";
            handleResult(task.getId(), instanceId, ActivitiConstant.HANDLE_STATUS_YZF, comment, task.getTaskDefinitionKey(), loginAppUser.getUsername(), execution.getId());
        }

        runtimeService.deleteProcessInstance(instanceId, ActivitiConstant.HANDEL_RESULT_ZF);
        return Result.result(true);
    }

    private void handleResult(String id, String instanceId, Integer handleStatus, String comment, String taskDefinitionKey, String username, String executionId) {

    }


}

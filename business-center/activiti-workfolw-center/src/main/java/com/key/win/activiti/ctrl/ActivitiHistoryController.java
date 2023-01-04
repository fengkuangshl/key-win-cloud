package com.key.win.activiti.ctrl;


import com.key.win.activiti.service.ActivitiHistoryService;
import com.key.win.activiti.vo.ActivitiHistoryVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activitiHistoryCtrl")
@Api("工作流历史相关的api")
public class ActivitiHistoryController {

    // private final static String AUTHORITY_PREFIX = "activiti::history-task::HistoryTask::";

    @Autowired
    private ActivitiHistoryService activitiHistoryService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    //用户历史
    @PostMapping(value = "/getInstancesByUserName")
    @ApiOperation(value = "获取历史实例分页")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    // @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::PAGED')")
    public PageResult<ActivitiHistoryVo> getInstancesByUser(@RequestBody PageRequest<ActivitiHistoryVo> t) {
        return activitiHistoryService.findActivitiHistoryByPaged(t);

    }

    //任务实例历史
    @PostMapping(value = "/getInstancesByPiId")
    @ApiOperation(value = "任务实例历史")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result getInstancesByPiId(@RequestParam("instanceId") String instanceId) {

        //--------------------------------------------另一种写法-------------------------
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId(instanceId)
                .list();

        return Result.succeed(historicTaskInstances);


    }

    //任务实例历史
    @PostMapping(value = "/getHistoryProcessDefinition/{instanceId}")
    @ApiOperation(value = "任务实例历史")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    public Result getHistoryProcessDefinition(@PathVariable("instanceId") String instanceId) {

        //--------------------------------------------另一种写法-------------------------
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId(instanceId)
                .list();

        return Result.succeed(historicTaskInstances);


    }


    //流程图高亮
    @GetMapping("/getHighLine")
    @ApiOperation(value = "流程图高亮")
    @LogAnnotation(module = "activiti-workfolw-center", recordRequestParam = false)
    // @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::ID')")
    public Result getHighLine(@RequestParam("instanceId") String instanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //判断是否是连线
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //获取流程实例 历史节点(全部)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        //各个历史节点   两两组合 key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //高亮连线ID
//        Set<String> highLine = new HashSet<>();
//         keyList.forEach(s -> highLine.add(map.get(s)));

       // Set<String> highLine = getHighLightedData(process,list);
        /*
         * 获取流程走过的线
         */
        // 获取流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
        List<String> highLine = this.getHighLightedFlows(bpmnModel, processDefinition, list);

        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .list();
        //高亮节点ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();

        //需要移除的高亮连线
        Set<String> set = new HashSet<>();
        //待办高亮节点
        Set<String> waitingToDo = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingToDo.add(s.getActivityId());

            for (FlowElement flowElement : flowElements) {
                //判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;

                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                        if (outgoingFlows != null && outgoingFlows.size() > 0) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(s.getActivityId())) {
                                    set.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });

        highLine.removeAll(set);


        //获取当前用户
        //User sysUser = getSysUser();
        Set<String> iDo = new HashSet<>(); //存放 高亮 我的办理节点
        //当前用户已完成的任务
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();

        if (loginAppUser == null) {
            throw new RuntimeException("当前用户未登录！！");
        }

        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(loginAppUser.getUsername())
                .finished()
                .processInstanceId(instanceId).list();

        taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("highPoint", highPoint);
        returnMap.put("highLine", highLine);
        returnMap.put("waitingToDo", waitingToDo);
        returnMap.put("iDo", iDo);
        return Result.succeed(returnMap);

    }


    /**
     * <p>
     * 获取流程走过的线
     * </p>
     *
     * @param bpmnModel                 流程对象模型
     * @param processDefinitionEntity   流程定义对象
     * @param historicActivityInstances 历史流程已经执行的节点，并已经按执行的先后顺序排序
     * @return List<String> 流程走过的线
     */
    public static List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        // 用以保存高亮的线flowId
        List<String> highFlows = new ArrayList<String>();
        if (historicActivityInstances == null || historicActivityInstances.size() == 0)
            return highFlows;

        // 遍历历史节点
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 取出已执行的节点
            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);

            // 用以保存后续开始时间相同的节点
            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();

            // 获取下一个节点（用于连线）
            FlowNode sameActivityImpl = getNextFlowNode(bpmnModel, historicActivityInstances, i, activityImpl_);
//          FlowNode sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i + 1).getActivityId());

            // 将后面第一个节点放在时间相同节点的集合里
            if (sameActivityImpl != null)
                sameStartTimeNodes.add(sameActivityImpl);

            // 循环后面节点，看是否有与此后继节点开始时间相同的节点，有则添加到后继节点集合
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().getTime() != activityImpl2.getStartTime().getTime())
                    break;

                // 如果第一个节点和第二个节点开始时间相同保存
                FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                sameStartTimeNodes.add(sameActivityImpl2);
            }

            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());
            // 取出节点的所有出去的线，对所有的线进行遍历
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows();
            for (SequenceFlow pvmTransition : pvmTransitions) {
                // 获取节点
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());

                // 不是后继节点
                if (!sameStartTimeNodes.contains(pvmActivityImpl))
                    continue;

                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                highFlows.add(pvmTransition.getId());
            }
        }

        // 返回高亮的线
        return highFlows;
    }

    /**
     * <p>
     * 获取下一个节点信息
     * </p>
     *
     * @param bpmnModel                 流程模型
     * @param historicActivityInstances 历史节点
     * @param i                         当前已经遍历到的历史节点索引（找下一个节点从此节点后）
     * @param activityImpl_             当前遍历到的历史节点实例
     * @return FlowNode 下一个节点信息
     * @author FRH
     * @time 2018年12月10日上午11:26:55
     * @version 1.0
     */
    private static FlowNode getNextFlowNode(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances, int i, HistoricActivityInstance activityImpl_) {
        // 保存后一个节点
        FlowNode sameActivityImpl = null;

        // 如果当前节点不是用户任务节点，则取排序的下一个节点为后续节点
        if (!"userTask".equals(activityImpl_.getActivityType())) {
            // 是最后一个节点，没有下一个节点
            if (i == historicActivityInstances.size())
                return sameActivityImpl;
            // 不是最后一个节点，取下一个节点为后继节点
            sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i + 1).getActivityId());// 找到紧跟在后面的一个节点
            // 返回
            return sameActivityImpl;
        }

        // 遍历后续节点，获取当前节点后续节点
        for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
            // 后续节点
            HistoricActivityInstance activityImp2_ = historicActivityInstances.get(k);
            // 都是userTask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
            if ("userTask".equals(activityImp2_.getActivityType()) && activityImpl_.getStartTime().getTime() == activityImp2_.getStartTime().getTime())
                continue;
            // 找到紧跟在后面的一个节点
            sameActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());
            break;
        }
        return sameActivityImpl;
    }

    private Set<String> getHighLightedData(Process process,
                                           List<HistoricActivityInstance> historicActInstances) {

        // 高亮流程已发生流转的线id集合
        Set<String> highLightedFlowIds = new HashSet<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActInstances) {
            FlowNode flowNode = (FlowNode) process.getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) process.getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转：
             * 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) process.getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(sequenceFlow.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("highLightedFlowId", sequenceFlow.getId());
                            map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                            tempMapList.add(map);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(tempMapList)) {
                    // 遍历匹配的集合，取得开始时间最早的一个
                    long earliestStamp = 0L;
                    String highLightedFlowId = null;
                    for (Map<String, Object> map : tempMapList) {
                        long highLightedFlowStartTime = Long.parseLong(map.get("highLightedFlowStartTime").toString());
                        if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
                            highLightedFlowId = map.get("highLightedFlowId").toString();
                            earliestStamp = highLightedFlowStartTime;
                        }
                    }
                    highLightedFlowIds.add(highLightedFlowId);
                }
            }
        }

        return highLightedFlowIds;
    }


}

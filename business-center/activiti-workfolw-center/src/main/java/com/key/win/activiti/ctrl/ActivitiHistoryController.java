package com.key.win.activiti.ctrl;


import com.key.win.activiti.service.ActivitiHistoryService;
import com.key.win.activiti.vo.ActivitiHistoryVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activitiHistoryCtrl")
public class ActivitiHistoryController {

    @Autowired
    private ActivitiHistoryService activitiHistoryService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    //用户历史
    @PostMapping(value = "/getInstancesByUserName")
    public PageResult<ActivitiHistoryVo> InstancesByUser(@RequestBody PageRequest<ActivitiHistoryVo> t) {
        return activitiHistoryService.findActivitiHistoryByPaged(t);

    }

    //任务实例历史
    @PostMapping(value = "/getInstancesByPiId")
    public List<HistoricTaskInstance> getInstancesByPiId(@RequestParam("piId") String piId) {

        //--------------------------------------------另一种写法-------------------------
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId(piId)
                .list();

        return historicTaskInstances;


    }


    //流程图高亮
    @GetMapping("/gethighLine")
    public Map<String, Object> gethighLine(@RequestParam("instanceId") String instanceId) {
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
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> highLine.add(map.get(s)));


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

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("highPoint", highPoint);
        reMap.put("highLine", highLine);
        reMap.put("waitingToDo", waitingToDo);
        reMap.put("iDo", iDo);
        return reMap;

    }


}

package com.key.win.activiti.service.impl;

import com.key.win.activiti.service.ProcessRuntimeService;
import com.key.win.activiti.util.PageResultUtil;
import com.key.win.activiti.vo.ProcessDefinitionVo;
import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.activiti.vo.ProcessTaskVo;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Order;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.TaskQueryProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessRuntimeServiceImpl implements ProcessRuntimeService {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private RepositoryService repositoryService;


    private static Map<String, TaskQueryProperty> processTaskMapping = new HashMap<>();

    static {
        processTaskMapping.put("id", TaskQueryProperty.TASK_ID);
        processTaskMapping.put("assignee", TaskQueryProperty.ASSIGNEE);
        processTaskMapping.put("createTime", TaskQueryProperty.CREATE_TIME);
        processTaskMapping.put("description", TaskQueryProperty.DESCRIPTION);
        processTaskMapping.put("dueDate", TaskQueryProperty.DUE_DATE);
        processTaskMapping.put("executionId", TaskQueryProperty.EXECUTION_ID);
        processTaskMapping.put("name", TaskQueryProperty.NAME);
        processTaskMapping.put("owner", TaskQueryProperty.OWNER);
        processTaskMapping.put("priority", TaskQueryProperty.PRIORITY);
        processTaskMapping.put("processDefinitionId", TaskQueryProperty.PROCESS_DEFINITION_ID);
        processTaskMapping.put("processInstanceId", TaskQueryProperty.PROCESS_INSTANCE_ID);
        processTaskMapping.put("taskDefinitionKey", TaskQueryProperty.TASK_DEFINITION_KEY);
        processTaskMapping.put("tenantId", TaskQueryProperty.TENANT_ID);
    }

    public PageResult<ProcessTaskVo> findProcessTaskByPaged(PageRequest<ProcessTaskVo> pageRequest) {
        Order.Direction direction = pageRequest.getSortDir() == OrderDir.DESC ? Order.Direction.DESC : Order.Direction.ASC;
        String order = "id";
        if (StringUtil.isNotBlank(pageRequest.getSortName())) {
            order = pageRequest.getSortName();
        }
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize(),
                Order.by(processTaskMapping.get(order).getName(), direction)));
        return PageResultUtil.constructPageResult(pageRequest, tasks.getTotalItems(), this.processTaskToVos(tasks.getContent()));
    }


    private List processTaskToVos(List<Task> processTasks) {
        List<ProcessTaskVo> processTaskVos = new ArrayList<>();
        for (Task processTask : processTasks) {
            ProcessTaskVo vo = new ProcessTaskVo();
            BeanUtils.copyProperties(processTask, vo);
            processTaskVos.add(vo);
            vo.setName(processTask.getName());
            ProcessInstance processInstance = processRuntime.processInstance(processTask.getProcessInstanceId());
            vo.setInstanceName(processInstance.getName());
            vo.setStatus(processTask.getStatus().name());
            vo.setCreateTime(processTask.getCreatedDate());
        }
        return processTaskVos;
    }



    @Override
    public PageResult<ProcessInstanceVo> findProcessInstanceByPaged(PageRequest<ProcessInstanceVo> pageRequest) {
        Order.Direction direction = pageRequest.getSortDir() == OrderDir.DESC ? Order.Direction.DESC : Order.Direction.ASC;
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize(),
                Order.by(pageRequest.getSortName(), direction)));
        return PageResultUtil.constructPageResult(pageRequest, processInstancePage.getTotalItems(), this.processInstanceToVos(processInstancePage.getContent()));
    }

    private List processInstanceToVos(List<ProcessInstance> processInstances) {
        List<ProcessInstanceVo> processInstanceVos = new ArrayList<>();
        for (ProcessInstance processInstance : processInstances) {
            ProcessInstanceVo vo = new ProcessInstanceVo();
            BeanUtils.copyProperties(processInstance, vo);
            processInstanceVos.add(vo);
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processInstance.getProcessDefinitionId())
                    .singleResult();
            vo.setStatus(processInstance.getStatus().name());
            vo.setResourceName(pd.getResourceName());
            vo.setDeploymentId(pd.getDeploymentId());
            vo.setStartTime(processInstance.getStartDate());

        }
        return processInstanceVos;
    }
}

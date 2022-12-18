package com.key.win.activiti.service.impl;

import com.key.win.activiti.service.ProcessTaskService;
import com.key.win.activiti.util.PageResultUtil;
import com.key.win.activiti.vo.ProcessTaskVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.TaskQueryProperty;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessTaskServiceImpl implements ProcessTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private  UserGroupManager userGroupManager;

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


    private List processTaskToVos(List<Task> processTasks) {
        List<ProcessTaskVo> processTaskVos = new ArrayList<>();
        for (Task processTask : processTasks) {
            ProcessTaskVo vo = new ProcessTaskVo();
            BeanUtils.copyProperties(processTask, vo);
            processTaskVos.add(vo);
            vo.setName(processTask.getName());
            vo.setCreateTime(processTask.getCreateTime());
            vo.setClaimTime(processTask.getClaimTime());
            ProcessInstance processInstance = processRuntime.processInstance(processTask.getProcessInstanceId());
            vo.setInstanceName(processInstance.getName());
            vo.setInstanceId(processInstance.getId());
            vo.setStatus(processInstance.getStatus().name());
        }
        return processTaskVos;
    }

    /**
     *  String authenticatedUserId = this.securityManager.getAuthenticatedUserId();
     *         if (authenticatedUserId != null && !authenticatedUserId.isEmpty()) {
     *             List<String> tasks = this.userGroupManager.getUserGroups(authenticatedUserId);
     *             getTasksPayload.setAssigneeId(authenticatedUserId);
     *             getTasksPayload.setGroups(tasks);
     *             taskQuery = (TaskQuery)((TaskQuery)((TaskQuery)taskQuery.or()).taskCandidateOrAssigned(getTasksPayload.getAssigneeId(), getTasksPayload.getGroups()).taskOwner(authenticatedUserId)).endOr();
     *             if (getTasksPayload.getProcessInstanceId() != null) {
     *                 taskQuery = (TaskQuery)taskQuery.processInstanceId(getTasksPayload.getProcessInstanceId());
     *             }
     *
     *             if (getTasksPayload.getParentTaskId() != null) {
     *                 taskQuery = (TaskQuery)taskQuery.taskParentTaskId(getTasksPayload.getParentTaskId());
     *             }
     *
     *             tasks = this.taskConverter.from(taskQuery.listPage(pageable.getStartIndex(), pageable.getMaxItems()));
     * @param pageRequest
     * @return
     */
    public PageResult<ProcessTaskVo> findProcessTaskByPaged(PageRequest<ProcessTaskVo> pageRequest) {

        TaskQuery taskQuery = taskService.createTaskQuery();
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if(loginAppUser == null){
            throw new RuntimeException("用户不存在！");
        }
        List<String> userGroups = userGroupManager.getUserGroups(loginAppUser.getUsername());
        (taskQuery.or()).taskCandidateOrAssigned(loginAppUser.getUsername(),userGroups).taskOwner(loginAppUser.getUsername()).endOr();
        long count = taskQuery.count();
        String order = "id";
        if (StringUtil.isNotBlank(pageRequest.getSortName())) {
            order = pageRequest.getSortName();
        }
        taskQuery.orderBy(processTaskMapping.get(order));

        if (pageRequest.getSortDir() != null && pageRequest.getSortDir() == OrderDir.DESC) {
            taskQuery.desc();
        } else {
            taskQuery.asc();
        }

        List<Task> processTasks = taskQuery.listPage(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize());
        return PageResultUtil.constructPageResult(pageRequest, count, this.processTaskToVos(processTasks));
    }
}


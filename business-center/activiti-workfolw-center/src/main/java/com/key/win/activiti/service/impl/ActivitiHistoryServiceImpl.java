package com.key.win.activiti.service.impl;

import com.key.win.activiti.service.ActivitiHistoryService;
import com.key.win.activiti.util.PageResultUtil;
import com.key.win.activiti.vo.ActivitiHistoryResponseVo;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.HistoricTaskInstanceQueryProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitiHistoryServiceImpl implements ActivitiHistoryService {

    @Autowired
    private HistoryService historyService;

    private static Map<String, HistoricTaskInstanceQueryProperty> activitiHistoryMapping = new HashMap<>();

    /**
     *   private static final Map<String, HistoricTaskInstanceQueryProperty> properties = new HashMap();
     *     public static final HistoricTaskInstanceQueryProperty HISTORIC_TASK_INSTANCE_ID = new HistoricTaskInstanceQueryProperty("RES.ID_");
     *     public static final HistoricTaskInstanceQueryProperty PROCESS_DEFINITION_ID = new HistoricTaskInstanceQueryProperty("RES.PROC_DEF_ID_");
     *     public static final HistoricTaskInstanceQueryProperty PROCESS_INSTANCE_ID = new HistoricTaskInstanceQueryProperty("RES.PROC_INST_ID_");
     *     public static final HistoricTaskInstanceQueryProperty EXECUTION_ID = new HistoricTaskInstanceQueryProperty("RES.EXECUTION_ID_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_NAME = new HistoricTaskInstanceQueryProperty("RES.NAME_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_DESCRIPTION = new HistoricTaskInstanceQueryProperty("RES.DESCRIPTION_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_ASSIGNEE = new HistoricTaskInstanceQueryProperty("RES.ASSIGNEE_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_OWNER = new HistoricTaskInstanceQueryProperty("RES.OWNER_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_DEFINITION_KEY = new HistoricTaskInstanceQueryProperty("RES.TASK_DEF_KEY_");
     *     public static final HistoricTaskInstanceQueryProperty DELETE_REASON = new HistoricTaskInstanceQueryProperty("RES.DELETE_REASON_");
     *     public static final HistoricTaskInstanceQueryProperty START = new HistoricTaskInstanceQueryProperty("RES.START_TIME_");
     *     public static final HistoricTaskInstanceQueryProperty END = new HistoricTaskInstanceQueryProperty("RES.END_TIME_");
     *     public static final HistoricTaskInstanceQueryProperty DURATION = new HistoricTaskInstanceQueryProperty("RES.DURATION_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_PRIORITY = new HistoricTaskInstanceQueryProperty("RES.PRIORITY_");
     *     public static final HistoricTaskInstanceQueryProperty TASK_DUE_DATE = new HistoricTaskInstanceQueryProperty("RES.DUE_DATE_");
     *     public static final HistoricTaskInstanceQueryProperty TENANT_ID_ = new HistoricTaskInstanceQueryProperty("RES.TENANT_ID_");
     *     public static final HistoricTaskInstanceQueryProperty INCLUDED_VARIABLE_TIME = new HistoricTaskInstanceQueryProperty("VAR.LAST_UPDATED_TIME_");
     */
    static {
        activitiHistoryMapping.put("id", HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID);
        activitiHistoryMapping.put("executionId", HistoricTaskInstanceQueryProperty.EXECUTION_ID);
        activitiHistoryMapping.put("assignee", HistoricTaskInstanceQueryProperty.TASK_ASSIGNEE);
        activitiHistoryMapping.put("description", HistoricTaskInstanceQueryProperty.TASK_DESCRIPTION);
        activitiHistoryMapping.put("deleteReason", HistoricTaskInstanceQueryProperty.DELETE_REASON);
        activitiHistoryMapping.put("executionId", HistoricTaskInstanceQueryProperty.EXECUTION_ID);
        activitiHistoryMapping.put("name", HistoricTaskInstanceQueryProperty.TASK_NAME);
        activitiHistoryMapping.put("owner", HistoricTaskInstanceQueryProperty.TASK_OWNER);
        activitiHistoryMapping.put("startTime", HistoricTaskInstanceQueryProperty.START);
        activitiHistoryMapping.put("processDefinitionId", HistoricTaskInstanceQueryProperty.PROCESS_DEFINITION_ID);
        activitiHistoryMapping.put("processInstanceId", HistoricTaskInstanceQueryProperty.PROCESS_INSTANCE_ID);
        activitiHistoryMapping.put("taskDefinitionKey", HistoricTaskInstanceQueryProperty.TASK_DEFINITION_KEY);
        activitiHistoryMapping.put("endTime", HistoricTaskInstanceQueryProperty.END);
        activitiHistoryMapping.put("duration", HistoricTaskInstanceQueryProperty.DURATION);
        activitiHistoryMapping.put("priority", HistoricTaskInstanceQueryProperty.TASK_PRIORITY);
        activitiHistoryMapping.put("dueDate", HistoricTaskInstanceQueryProperty.TASK_DUE_DATE);
        activitiHistoryMapping.put("tenantId", HistoricTaskInstanceQueryProperty.TENANT_ID_);
        activitiHistoryMapping.put("lastUpdatedTime", HistoricTaskInstanceQueryProperty.INCLUDED_VARIABLE_TIME);
    }


    private List activitiHistoryToVos(List<HistoricTaskInstance> activitiHistorys) {
        List<ActivitiHistoryResponseVo> processTaskVos = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : activitiHistorys) {
            ActivitiHistoryResponseVo vo = new ActivitiHistoryResponseVo();
            BeanUtils.copyProperties(historicTaskInstance, vo);
            processTaskVos.add(vo);
        }
        return processTaskVos;
    }

    public PageResult<ActivitiHistoryResponseVo> findActivitiHistoryByPaged(PageRequest<ActivitiHistoryResponseVo> pageRequest) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if (loginAppUser == null) {
            throw new RuntimeException("用户不存在！");
        }
        historicTaskInstanceQuery.taskAssignee(loginAppUser.getUsername());
        if (StringUtil.isNotBlank(pageRequest.getT().getName())) {
            historicTaskInstanceQuery.taskNameLikeIgnoreCase("%" + pageRequest.getT().getName() + "%");
        }
        long count = historicTaskInstanceQuery.count();
        String order = "id";
        if (StringUtil.isNotBlank(pageRequest.getSortName())) {
            order = pageRequest.getSortName();
        }
        historicTaskInstanceQuery.orderBy(activitiHistoryMapping.get(order));

        if (pageRequest.getSortDir() != null && pageRequest.getSortDir() == OrderDir.DESC) {
            historicTaskInstanceQuery.desc();
        } else {
            historicTaskInstanceQuery.asc();
        }


        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.listPage(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize());
        return PageResultUtil.constructPageResult(pageRequest, count, this.activitiHistoryToVos(historicTaskInstances));
    }
}


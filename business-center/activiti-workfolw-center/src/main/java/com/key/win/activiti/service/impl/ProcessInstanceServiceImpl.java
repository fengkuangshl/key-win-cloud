package com.key.win.activiti.service.impl;

import com.key.win.activiti.service.ProcessInstanceService;
import com.key.win.activiti.util.PageResultUtil;
import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.ProcessInstanceQueryProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    private static Map<String, ProcessInstanceQueryProperty> processInstanceMapping = new HashMap<>();

    static {
        processInstanceMapping.put("id", ProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
        processInstanceMapping.put("processDefinitionId", ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
        processInstanceMapping.put("processDefinitionKey", ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
        processInstanceMapping.put("tenantId", ProcessInstanceQueryProperty.TENANT_ID);
        processInstanceMapping.put("name", new ProcessInstanceQueryProperty("RES.NAME_"));
        processInstanceMapping.put("status", new ProcessInstanceQueryProperty("RES.SUSPENSION_STATE_"));
        processInstanceMapping.put("startTime", new ProcessInstanceQueryProperty("RES.START_TIME_"));
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
            vo.setStatus(processInstance.isSuspended()?"suspended":"active");
            vo.setResourceName(pd.getResourceName());
            vo.setDeploymentId(pd.getDeploymentId());
            vo.setName(processInstance.getName());

        }
        return processInstanceVos;
    }

    @Override
    public PageResult<ProcessInstanceVo> findProcessInstanceByPaged(PageRequest<ProcessInstanceVo> pageRequest) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        long count = processInstanceQuery.count();
        String order = "id";
        if (StringUtil.isNotBlank(pageRequest.getSortName())) {
            order = pageRequest.getSortName();
        }
        processInstanceQuery.orderBy(processInstanceMapping.get(order));
        if (pageRequest.getSortDir() != null && pageRequest.getSortDir() == OrderDir.DESC) {
            processInstanceQuery.desc();
        } else {
            processInstanceQuery.asc();
        }
        List<ProcessInstance> processInstances = processInstanceQuery.listPage(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize());

        return PageResultUtil.constructPageResult(pageRequest, count, this.processInstanceToVos(processInstances));
    }
}


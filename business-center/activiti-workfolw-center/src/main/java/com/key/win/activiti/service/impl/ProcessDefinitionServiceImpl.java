package com.key.win.activiti.service.impl;

import com.key.win.activiti.service.ProcessDefinitionService;
import com.key.win.activiti.util.PageResultUtil;
import com.key.win.activiti.vo.ProcessDefinitionVo;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ProcessDefinitionQueryProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {


    @Autowired
    private RepositoryService repositoryService;

    private static Map<String, ProcessDefinitionQueryProperty> processDefinitionMapping = new HashMap<>();

    static {
        processDefinitionMapping.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
        processDefinitionMapping.put("deploymentId", ProcessDefinitionQueryProperty.DEPLOYMENT_ID);
        processDefinitionMapping.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
        processDefinitionMapping.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
        processDefinitionMapping.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
        processDefinitionMapping.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
        processDefinitionMapping.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
    }

    @Override
    public PageResult<ProcessDefinitionVo> findProcessDefinitionByPaged(PageRequest<ProcessDefinitionVo> pageRequest) {
        long count = repositoryService.createProcessDefinitionQuery().count();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String order = "id";
        if (StringUtil.isNotBlank(pageRequest.getSortName())) {
            order = pageRequest.getSortName();
        }
        processDefinitionQuery.orderBy(processDefinitionMapping.get(order));
        if (pageRequest.getSortDir() != null && pageRequest.getSortDir() == OrderDir.DESC) {
            processDefinitionQuery.desc();
        } else {
            processDefinitionQuery.asc();
        }
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(pageRequest.getHbPageNo() * pageRequest.getPageSize(), pageRequest.getPageSize());

        return PageResultUtil.constructPageResult(pageRequest, count, this.processDefinitionsToVos(processDefinitions));
    }

    private List<ProcessDefinitionVo> processDefinitionsToVos(List<ProcessDefinition> processDefinitions) {
        List<ProcessDefinitionVo> processDefinitionVos = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            ProcessDefinitionVo vo = new ProcessDefinitionVo();
            BeanUtils.copyProperties(processDefinition, vo);
            processDefinitionVos.add(vo);
        }
        return processDefinitionVos;
    }
}

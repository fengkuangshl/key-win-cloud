package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessDefinitionResponseVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessDefinitionService {

    public PageResult<ProcessDefinitionResponseVo> findProcessDefinitionByPaged(PageRequest<ProcessDefinitionResponseVo> pageRequest);
}

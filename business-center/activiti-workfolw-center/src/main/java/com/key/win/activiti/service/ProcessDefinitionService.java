package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessDefinitionVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessDefinitionService {

    public PageResult<ProcessDefinitionVo> findProcessDefinitionByPaged(PageRequest<ProcessDefinitionVo> pageRequest);
}

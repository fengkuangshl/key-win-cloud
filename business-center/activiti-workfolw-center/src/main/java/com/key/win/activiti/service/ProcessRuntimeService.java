package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessDefinitionVo;
import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.activiti.vo.ProcessTaskVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessRuntimeService {

    public PageResult<ProcessInstanceVo> findProcessInstanceByPaged(PageRequest<ProcessInstanceVo> pageRequest);

    public PageResult<ProcessTaskVo> findProcessTaskByPaged(PageRequest<ProcessTaskVo> pageRequest);
}

package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessInstanceVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessInstanceService {

    public PageResult<ProcessInstanceVo> findProcessInstanceByPaged(PageRequest<ProcessInstanceVo> pageRequest);
}

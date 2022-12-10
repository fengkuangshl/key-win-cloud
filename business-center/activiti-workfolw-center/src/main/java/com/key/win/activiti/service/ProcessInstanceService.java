package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessInstanceResponseVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessInstanceService {

    public PageResult<ProcessInstanceResponseVo> findProcessInstanceByPaged(PageRequest<ProcessInstanceResponseVo> pageRequest);
}

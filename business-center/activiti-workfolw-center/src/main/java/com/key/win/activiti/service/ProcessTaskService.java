package com.key.win.activiti.service;

import com.key.win.activiti.vo.ProcessTaskVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ProcessTaskService {

    public PageResult<ProcessTaskVo> findProcessTaskByPaged(PageRequest<ProcessTaskVo> pageRequest);
}

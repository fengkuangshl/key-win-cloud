package com.key.win.activiti.service;

import com.key.win.activiti.vo.ActivitiHistoryVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ActivitiHistoryService {

    public PageResult<ActivitiHistoryVo> findActivitiHistoryByPaged(PageRequest<ActivitiHistoryVo> pageRequest);
}

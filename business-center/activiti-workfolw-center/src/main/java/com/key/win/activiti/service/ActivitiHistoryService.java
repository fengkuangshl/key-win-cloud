package com.key.win.activiti.service;

import com.key.win.activiti.vo.ActivitiHistoryResponseVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface ActivitiHistoryService {

    public PageResult<ActivitiHistoryResponseVo> findActivitiHistoryByPaged(PageRequest<ActivitiHistoryResponseVo> pageRequest);
}

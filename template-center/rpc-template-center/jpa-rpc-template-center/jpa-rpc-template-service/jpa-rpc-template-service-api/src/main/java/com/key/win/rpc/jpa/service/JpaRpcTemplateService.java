package com.key.win.rpc.jpa.service;

import com.key.win.common.web.Result;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.rpc.jpa.vo.JpaRpcTemplateVo;

public interface JpaRpcTemplateService {

    public Result delete(String id);

    public Result saveOrUpdateJpaRpcTemplate(JpaRpcTemplateVo mybatiesTemplate);

    public JpaRpcTemplateVo get(String id);

    public PageResult<JpaRpcTemplateVo> findJpaRpcTemplateByPaged(PageRequest<JpaRpcTemplateVo> pageRequest);
}

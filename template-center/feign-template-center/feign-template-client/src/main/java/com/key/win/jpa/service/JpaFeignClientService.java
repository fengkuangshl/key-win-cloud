package com.key.win.jpa.service;

import com.key.win.common.web.Result;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface JpaFeignClientService {

    public JpaFeignTemplateVo get(String id);

    public Result saveOrUpdateJpaFeignTemplate(JpaFeignTemplateVo jpaTemplate);

    public Result delete(String id);

    public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(PageRequest<JpaFeignTemplateVo> t);
}

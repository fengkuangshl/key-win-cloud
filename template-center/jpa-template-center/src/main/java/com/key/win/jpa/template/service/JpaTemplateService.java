package com.key.win.jpa.template.service;

import com.key.win.common.web.Result;
import com.key.win.jpa.template.model.JpaTemplate;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

public interface JpaTemplateService {

    public JpaTemplate get(String id);

    public Result saveOrUpdateJpaTemplate(JpaTemplate jpaTemplate);

    public Result delete(String id);

    public PageResult<JpaTemplate> getJpaTemplateByPaged(PageRequest<JpaTemplate> t);

    public String getCacheTest();
}

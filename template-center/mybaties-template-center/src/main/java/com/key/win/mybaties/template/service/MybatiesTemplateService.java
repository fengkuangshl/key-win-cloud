package com.key.win.mybaties.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.mybaties.template.model.MybatiesTemplate;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;

public interface MybatiesTemplateService extends IService<MybatiesTemplate> {


    public String getCacheTest();

    public List<MybatiesTemplate> getMybatiesTemplateByCondition(String name,String code);

    public List<MybatiesTemplate> getMybatiesTemplateByLike(String name);

    public PageResult<MybatiesTemplate> findMybatiesTemplateByPaged(PageRequest<MybatiesTemplate> pageRequest);
}

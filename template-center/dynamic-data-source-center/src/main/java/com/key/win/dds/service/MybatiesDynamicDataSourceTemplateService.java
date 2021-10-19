package com.key.win.dds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.dds.model.MybatiesDynamicDataSourceTemplate;

import java.util.List;

public interface MybatiesDynamicDataSourceTemplateService extends IService<MybatiesDynamicDataSourceTemplate> {


    public String getCacheTest();

    public List<MybatiesDynamicDataSourceTemplate> getMybatiesDynamicDataSourceTemplateByCondition(String name, String code);

    public List<MybatiesDynamicDataSourceTemplate> getMybatiesDynamicDataSourceTemplateByLike(String name);

    public PageResult<MybatiesDynamicDataSourceTemplate> findMybatiesDynamicDataSourceTemplateByPaged(PageRequest<MybatiesDynamicDataSourceTemplate> pageRequest);
}

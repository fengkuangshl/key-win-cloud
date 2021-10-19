package com.key.win.mybaties.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.mybaties.model.MybatiesFeignTemplate;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;

import java.util.List;

public interface MybatiesFeignTemplateService extends IService<MybatiesFeignTemplate> {

    public Result delete(String id);

    public Result saveOrUpdateMybatiesFeignTemplate(MybatiesFeignTemplateVo mybatiesTemplate);

    public MybatiesFeignTemplateVo get(String id);

    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(String name, String code);

    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(String name);

    public PageResult<MybatiesFeignTemplateVo> findMybatiesFeignTemplateByPaged(PageRequest<MybatiesFeignTemplateVo> pageRequest);
}

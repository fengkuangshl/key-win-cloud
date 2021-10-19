package com.key.win.mybaties.service;

import com.key.win.common.web.Result;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface MybatiesFeignClientService{

    public Result saveOrUpdateMybatiesFeignTemplate(MybatiesFeignTemplateVo mybatiesTemplate);

    public MybatiesFeignTemplateVo get( String id);

    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(MybatiesFeignTemplateVo mybatiesTemplate);

    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(MybatiesFeignTemplateVo mybatiesTemplate);

    public PageResult<MybatiesFeignTemplateVo> findMybatiesFeignTemplateByPaged(PageRequest<MybatiesFeignTemplateVo> pageRequest);

    public Result delete(@PathVariable String id);
}

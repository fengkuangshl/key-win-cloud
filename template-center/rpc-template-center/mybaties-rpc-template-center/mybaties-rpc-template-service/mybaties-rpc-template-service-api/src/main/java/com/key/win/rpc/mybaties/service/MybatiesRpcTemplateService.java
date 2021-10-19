package com.key.win.rpc.mybaties.service;

import com.key.win.common.web.Result;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.rpc.mybaties.vo.MybatiesRpcTemplateVo;

import java.util.List;

public interface MybatiesRpcTemplateService{

    public Result delete(String id);

    public Result saveOrUpdateMybatiesRpcTemplate(MybatiesRpcTemplateVo mybatiesTemplate);

    public MybatiesRpcTemplateVo get(String id);

    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByCondition(String name, String code);

    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByLike(String name);

    public PageResult<MybatiesRpcTemplateVo> findMybatiesRpcTemplateByPaged(PageRequest<MybatiesRpcTemplateVo> pageRequest);
}

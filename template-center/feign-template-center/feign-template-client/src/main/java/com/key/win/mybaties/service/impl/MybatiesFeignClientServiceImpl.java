package com.key.win.mybaties.service.impl;

import com.key.win.common.web.Result;
import com.key.win.mybaties.feign.MybatiesFeignClient;
import com.key.win.mybaties.service.MybatiesFeignClientService;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatiesFeignClientServiceImpl implements MybatiesFeignClientService {

    private static final Logger log = LoggerFactory.getLogger(MybatiesFeignClientServiceImpl.class);

    @Autowired
    private MybatiesFeignClient mybatiesFeignClient;

    public Result saveOrUpdateMybatiesFeignTemplate(MybatiesFeignTemplateVo mybatiesTemplate) {
        return mybatiesFeignClient.saveOrUpdateMybatiesFeignTemplate(mybatiesTemplate);
    }

    public MybatiesFeignTemplateVo get(String id) {
        return mybatiesFeignClient.get(id);
    }

    @Override
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(MybatiesFeignTemplateVo mybatiesTemplate) {
        return mybatiesFeignClient.getMybatiesFeignTemplateByCondition(mybatiesTemplate);
    }

    @Override
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(MybatiesFeignTemplateVo mybatiesTemplate) {
        return mybatiesFeignClient.getMybatiesFeignTemplateByLike(mybatiesTemplate);
    }


    public PageResult<MybatiesFeignTemplateVo> findMybatiesFeignTemplateByPaged(PageRequest<MybatiesFeignTemplateVo> pageRequest) {
        return mybatiesFeignClient.getMybatiesFeignTemplateByPaged(pageRequest);
    }

    @Override
    public Result delete(String id) {
        return mybatiesFeignClient.delete(id);
    }
}

package com.key.win.jpa.service.impl;

import com.key.win.common.web.Result;
import com.key.win.jpa.feign.JpaFeignClient;
import com.key.win.jpa.service.JpaFeignClientService;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaFeignClientServiceImpl implements JpaFeignClientService {

    @Autowired
    private JpaFeignClient jpaFeignClient;

    @Override
    public JpaFeignTemplateVo get(String id) {
        return  jpaFeignClient.get(id);
    }

    @Override
    public Result saveOrUpdateJpaFeignTemplate(JpaFeignTemplateVo jpaTemplate) {
        return jpaFeignClient.saveOrUpdateJpaFeignTemplate(jpaTemplate);
    }

    @Override
    public Result delete(String id) {
       return jpaFeignClient.delete(id);
    }

    @Override
    public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(PageRequest<JpaFeignTemplateVo> t) {
        return  jpaFeignClient.getJpaFeignTemplateByPaged(t);
    }
}

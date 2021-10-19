package com.key.win.jpa.feign;

import com.key.win.common.feign.FeignExceptionConfig;
import com.key.win.common.web.Result;
import com.key.win.jpa.feign.fallback.JpaFeignClientFallbackFactory;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



@FeignClient(value="jpa-feign-template-service",configuration = FeignExceptionConfig.class ,fallbackFactory = JpaFeignClientFallbackFactory.class, decode404 = true)
public interface JpaFeignClient {


    @PostMapping("/jpaTemplateCtrl/getJpaFeignTemplateByPaged")
    public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(@RequestBody PageRequest<JpaFeignTemplateVo> t);


    @PostMapping("/jpaTemplateCtrl/saveOrUpdateJpaFeignTemplate")
    public Result saveOrUpdateJpaFeignTemplate(@RequestBody JpaFeignTemplateVo jpaTemplate);

    @GetMapping("/jpaTemplateCtrl/get/{id}")
    public JpaFeignTemplateVo get(@PathVariable String id);


    @DeleteMapping("/jpaTemplateCtrl/delete/{id}")
    public Result delete(@PathVariable String id);
    
}

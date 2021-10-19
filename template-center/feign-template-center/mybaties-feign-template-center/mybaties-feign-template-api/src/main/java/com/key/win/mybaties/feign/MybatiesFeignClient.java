package com.key.win.mybaties.feign;

import com.key.win.common.feign.FeignExceptionConfig;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.mybaties.feign.fallback.MybatiesFeignClientFallbackFactory;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value="mybaties-feign-template-service",configuration = FeignExceptionConfig.class ,fallbackFactory = MybatiesFeignClientFallbackFactory.class, decode404 = true)
public interface MybatiesFeignClient {



    @PostMapping("/mybatiesTemplateCtrl/getMybatiesFeignTemplateByPaged")
    public PageResult<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByPaged(@RequestBody PageRequest<MybatiesFeignTemplateVo> t) ;



    @PostMapping("/mybatiesTemplateCtrl/saveOrUpdateMybatiesFeignTemplate")
    public Result saveOrUpdateMybatiesFeignTemplate(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate) ;


    @GetMapping("/mybatiesTemplateCtrl/get/{id}")
    public MybatiesFeignTemplateVo get(@PathVariable String id);


    @DeleteMapping("/mybatiesTemplateCtrl/delete/{id}")
    public Result delete(@PathVariable String id);

    @PostMapping("/mybatiesTemplateCtrl/getMybatiesFeignTemplateByCondition")
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate);

    @PostMapping("/mybatiesTemplateCtrl/getMybatiesFeignTemplateByLike")
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate);



}

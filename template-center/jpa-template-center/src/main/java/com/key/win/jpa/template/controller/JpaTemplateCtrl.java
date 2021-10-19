package com.key.win.jpa.template.controller;

import com.key.win.common.web.Result;
import com.key.win.jpa.template.model.JpaTemplate;
import com.key.win.jpa.template.service.JpaTemplateService;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jpaTemplateCtrl/*")
public class JpaTemplateCtrl {

    @Autowired
    private JpaTemplateService jpaTemplateService;



    @ApiOperation("分页")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @PostMapping("/getJpaTemplateByPaged")
    public PageResult<JpaTemplate> getJpaTemplateByPaged(@RequestBody PageRequest<JpaTemplate> t) {
        return jpaTemplateService.getJpaTemplateByPaged(t);
    }

    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateJpaTemplate")
    public Result saveOrUpdateJpaTemplate(@RequestBody JpaTemplate jpaTemplate){
        return  jpaTemplateService.saveOrUpdateJpaTemplate(jpaTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public JpaTemplate get(@PathVariable String id) {
        return jpaTemplateService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        return jpaTemplateService.delete(id);
    }

    @ApiOperation("cache")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @GetMapping("/getCacheTest")
    public Result getCacheTest() {
        return Result.succeed(jpaTemplateService.getCacheTest());
    }

}

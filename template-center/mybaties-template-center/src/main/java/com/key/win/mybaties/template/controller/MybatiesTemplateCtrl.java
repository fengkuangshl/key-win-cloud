package com.key.win.mybaties.template.controller;

import com.key.win.common.web.Result;
import com.key.win.datalog.annotation.DataLog;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.mybaties.template.model.MybatiesTemplate;
import com.key.win.mybaties.template.service.MybatiesSqlTemplateService;
import com.key.win.mybaties.template.service.MybatiesTemplateService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mybatiesTemplateCtrl/*")
public class MybatiesTemplateCtrl {

    @Autowired
    private MybatiesTemplateService mybatiesTemplateService;

    @Autowired
    private MybatiesSqlTemplateService mybatiesSqlTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesTemplateByPaged")
    public PageResult<MybatiesTemplate> getMybatiesTemplateByPaged(@RequestBody PageRequest<MybatiesTemplate> t) {
        return mybatiesTemplateService.findMybatiesTemplateByPaged(t);
    }


    @ApiOperation("cache")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/getCacheTest")
    public Result getCacheTest() {
        return Result.succeed(mybatiesTemplateService.getCacheTest());
    }

    @ApiOperation("getAll")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/getAll")
    public Result getAll() {
        return Result.succeed(mybatiesSqlTemplateService.getAll(),"");
    }

    @DataLog
    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateMybatiesTemplate")
    public Result saveOrUpdateMybatiesTemplate(@RequestBody MybatiesTemplate mybatiesTemplate){
        boolean b = mybatiesTemplateService.saveOrUpdate(mybatiesTemplate);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public MybatiesTemplate get(@PathVariable String id) {
        return mybatiesTemplateService.getById(id);
    }

    @DataLog
    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        boolean b = mybatiesTemplateService.removeById(id);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }



}

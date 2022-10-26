package com.key.win.dds.ctrl;

import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.dds.model.MybatiesDynamicDataSourceTemplate;
import com.key.win.dds.service.MybatiesDynamicDataSourceTemplateService;
import com.key.win.log.annotation.LogAnnotation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dynamicDataSourceCtrl/*")
public class DynamicDataSourceCtrl {

    @Autowired
    private MybatiesDynamicDataSourceTemplateService mybatiesDynamicDataSourceTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "dynamic-data-source-center", recordRequestParam = false)
    @PostMapping("/getMybatiesDynamicDataSourceTemplateByPaged")
    public PageResult<MybatiesDynamicDataSourceTemplate> getMybatiesDynamicDataSourceTemplateByPaged(@RequestBody PageRequest<MybatiesDynamicDataSourceTemplate> t) {
        return mybatiesDynamicDataSourceTemplateService.findMybatiesDynamicDataSourceTemplateByPaged(t);
    }

    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "dynamic-data-source-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateMybatiesDynamicDataSourceTemplate")
    public Result saveOrUpdateMybatiesDynamicDataSourceTemplate(@RequestBody MybatiesDynamicDataSourceTemplate mybatiesTemplate){
        boolean b = mybatiesDynamicDataSourceTemplateService.saveOrUpdate(mybatiesTemplate);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "dynamic-data-source-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public MybatiesDynamicDataSourceTemplate get(@PathVariable String id) {
        return mybatiesDynamicDataSourceTemplateService.getById(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "dynamic-data-source-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        boolean b = mybatiesDynamicDataSourceTemplateService.removeById(id);
        return  b ? Result.succeed("操作成功"):Result.succeed("操作失败");
    }

}

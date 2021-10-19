package com.key.win.controller;

import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.model.DataSourceTest;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.service.DataSourceTestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dataSourceTest")
public class DataSourceTestController {
    @Resource
    private DataSourceTestService dataSourceTestService;

    @ApiOperation("分页")
    @LogAnnotation(module = "datasource-template-center", recordRequestParam = false)
    @PostMapping("/getDataSourceTemplateByPage")
    public PageResult<DataSourceTest> getDataSourceTemplateByPaged(@RequestBody PageRequest<DataSourceTest> t) {
        return dataSourceTestService.findDataSourceTemplateByPage(t);
    }

    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "datasource-template-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateDataSourceTemplate")
    public Result saveOrUpdateDataSourceTemplate(@RequestBody DataSourceTest dataSourceTest) {
        return dataSourceTestService.saveOrUpdateDataSourceTemplate(dataSourceTest);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "datasource-template-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public DataSourceTest get(@PathVariable String id) {
        return dataSourceTestService.get(id);
    }

    @ApiOperation("根据Id删除信息")
    @LogAnnotation(module = "datasource-template-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        return dataSourceTestService.delete(id);
    }

}


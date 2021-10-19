package com.key.win.mybaties.ctrl;

import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.mybaties.service.MybatiesFeignClientService;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mybatiesTemplateCtrl/*")
public class MybatiesFeignClientCtrl {

    @Autowired
    private MybatiesFeignClientService mybatiesFeignClientService;

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesFeignTemplateByPaged")
    public PageResult<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByPaged(@RequestBody PageRequest<MybatiesFeignTemplateVo> t) {
        return mybatiesFeignClientService.findMybatiesFeignTemplateByPaged(t);
    }


    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateMybatiesFeignTemplate")
    public Result saveOrUpdateMybatiesFeignTemplate(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate) {
        return mybatiesFeignClientService.saveOrUpdateMybatiesFeignTemplate(mybatiesTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public MybatiesFeignTemplateVo get(@PathVariable String id) {
        return mybatiesFeignClientService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
       return mybatiesFeignClientService.delete(id);
    }

}

package com.key.win.mybaties.ctrl;

import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.mybaties.service.MybatiesFeignTemplateService;
import com.key.win.mybaties.vo.MybatiesFeignTemplateVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mybatiesTemplateCtrl/*")
public class MybatiesFeignTemplateCtrl {

    @Autowired
    private MybatiesFeignTemplateService mybatiesTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesFeignTemplateByPaged")
    public PageResult<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByPaged(@RequestBody PageRequest<MybatiesFeignTemplateVo> t) {
        return mybatiesTemplateService.findMybatiesFeignTemplateByPaged(t);
    }


    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateMybatiesFeignTemplate")
    public Result saveOrUpdateMybatiesFeignTemplate(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate) {
        return mybatiesTemplateService.saveOrUpdateMybatiesFeignTemplate(mybatiesTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public MybatiesFeignTemplateVo get(@PathVariable String id) {
        return mybatiesTemplateService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
      return mybatiesTemplateService.delete(id);
    }
    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesFeignTemplateByCondition")
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByCondition(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate){
        return mybatiesTemplateService.getMybatiesFeignTemplateByCondition(mybatiesTemplate.getName(),mybatiesTemplate.getCode());
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesFeignTemplateByLike")
    public List<MybatiesFeignTemplateVo> getMybatiesFeignTemplateByLike(@RequestBody MybatiesFeignTemplateVo mybatiesTemplate){
        return  mybatiesTemplateService.getMybatiesFeignTemplateByLike(mybatiesTemplate.getName());
    }

}

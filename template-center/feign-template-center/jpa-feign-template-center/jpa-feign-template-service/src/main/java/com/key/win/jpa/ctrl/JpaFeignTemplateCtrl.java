package com.key.win.jpa.ctrl;

import com.key.win.common.web.Result;
import com.key.win.jpa.service.JpaFeignTemplateService;
import com.key.win.jpa.vo.JpaFeignTemplateVo;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jpaTemplateCtrl/*")
public class JpaFeignTemplateCtrl {

    @Autowired
    private JpaFeignTemplateService jpaTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @PostMapping("/getJpaFeignTemplateByPaged")
    public PageResult<JpaFeignTemplateVo> getJpaFeignTemplateByPaged(@RequestBody PageRequest<JpaFeignTemplateVo> t) {
        return jpaTemplateService.getJpaFeignTemplateByPaged(t);
    }

    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateJpaFeignTemplate")
    public Result saveOrUpdateJpaFeignTemplate(@RequestBody JpaFeignTemplateVo jpaTemplate){
        return  jpaTemplateService.saveOrUpdateJpaFeignTemplate(jpaTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public JpaFeignTemplateVo get(@PathVariable String id) {
        return jpaTemplateService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "jpa-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        return jpaTemplateService.delete(id);
    }

}

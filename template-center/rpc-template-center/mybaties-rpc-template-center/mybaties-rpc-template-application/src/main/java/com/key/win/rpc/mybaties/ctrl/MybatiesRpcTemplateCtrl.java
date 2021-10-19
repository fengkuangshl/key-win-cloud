package com.key.win.rpc.mybaties.ctrl;

import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.rpc.mybaties.service.MybatiesRpcTemplateService;
import com.key.win.rpc.mybaties.vo.MybatiesRpcTemplateVo;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mybatiesRpcTemplateCtrl/*")
public class MybatiesRpcTemplateCtrl {

    @DubboReference
    private MybatiesRpcTemplateService mybatiesRpcTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesRpcTemplateByPaged")
    public PageResult<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByPaged(@RequestBody PageRequest<MybatiesRpcTemplateVo> t) {
        return mybatiesRpcTemplateService.findMybatiesRpcTemplateByPaged(t);
    }


    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateMybatiesRpcTemplate")
    public Result saveOrUpdateMybatiesRpcTemplate(@RequestBody MybatiesRpcTemplateVo mybatiesTemplate) {
        return mybatiesRpcTemplateService.saveOrUpdateMybatiesRpcTemplate(mybatiesTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public MybatiesRpcTemplateVo get(@PathVariable String id) {
        return mybatiesRpcTemplateService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
      return mybatiesRpcTemplateService.delete(id);
    }
    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesRpcTemplateByCondition")
    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByCondition(@RequestBody MybatiesRpcTemplateVo mybatiesTemplate){
        return mybatiesRpcTemplateService.getMybatiesRpcTemplateByCondition(mybatiesTemplate.getName(),mybatiesTemplate.getCode());
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "mybaties-templat-center", recordRequestParam = false)
    @PostMapping("/getMybatiesRpcTemplateByLike")
    public List<MybatiesRpcTemplateVo> getMybatiesRpcTemplateByLike(@RequestBody MybatiesRpcTemplateVo mybatiesTemplate){
        return  mybatiesRpcTemplateService.getMybatiesRpcTemplateByLike(mybatiesTemplate.getName());
    }

}

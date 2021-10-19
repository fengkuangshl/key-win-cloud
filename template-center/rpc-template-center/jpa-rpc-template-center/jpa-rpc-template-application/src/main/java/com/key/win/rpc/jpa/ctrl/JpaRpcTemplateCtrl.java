package com.key.win.rpc.jpa.ctrl;

import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.rpc.jpa.service.JpaRpcTemplateService;
import com.key.win.rpc.jpa.vo.JpaRpcTemplateVo;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jpaRpcTemplateCtrl/*")
public class JpaRpcTemplateCtrl {

    @DubboReference
    private JpaRpcTemplateService jpaRpcTemplateService;

    @ApiOperation("分页")
    @LogAnnotation(module = "jpa-template-center", recordRequestParam = false)
    @PostMapping("/getJpaRpcTemplateByPaged")
    public PageResult<JpaRpcTemplateVo> getJpaRpcTemplateByPaged(@RequestBody PageRequest<JpaRpcTemplateVo> t) {
        return jpaRpcTemplateService.findJpaRpcTemplateByPaged(t);
    }


    @ApiOperation("保存或更新数据")
    @LogAnnotation(module = "jpa-template-center", recordRequestParam = false)
    @PostMapping("/saveOrUpdateJpaRpcTemplate")
    public Result saveOrUpdateJpaRpcTemplate(@RequestBody JpaRpcTemplateVo jpaTemplate) {
        return jpaRpcTemplateService.saveOrUpdateJpaRpcTemplate(jpaTemplate);
    }

    @ApiOperation("根据Id获取信息")
    @LogAnnotation(module = "jpa-template-center", recordRequestParam = false)
    @GetMapping("/get/{id}")
    public JpaRpcTemplateVo get(@PathVariable String id) {
        return jpaRpcTemplateService.get(id);
    }

    @ApiOperation("根据Id信息")
    @LogAnnotation(module = "jpa-template-center", recordRequestParam = false)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
      return jpaRpcTemplateService.delete(id);
    }

}

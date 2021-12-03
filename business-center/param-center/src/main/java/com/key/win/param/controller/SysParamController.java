package com.key.win.param.controller;

import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.model.baseData.SysParam;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.param.service.SysParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "PARAM API")
@RequestMapping("/sysParam")
public class SysParamController {
    @Autowired
    private SysParamService sysParamService;

    @PreAuthorize("hasAuthority('param:get/sysParam/{id}')")
    @GetMapping("/get/{id}")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result get(@PathVariable String id) throws ControllerException {
        return Result.succeed(sysParamService.getById(id), "");
    }

    /**
     * 删除菜单
     *
     * @param id
     * @throws ControllerException
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    @PreAuthorize("hasAuthority('param:delete/sysParam/{id}')")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result delete(@PathVariable String id) throws ControllerException {
        boolean b = sysParamService.removeById(id);
        return b ? Result.succeed("保存成功！") : Result.failed("保存失败");
    }

    /**
     * 添加菜单 或者 更新
     *
     * @return
     * @throws ControllerException
     */
    @PostMapping("saveOrUpdate")
    @ApiOperation(value = "新增")
    @PreAuthorize("hasAnyAuthority('param:post/sysParam','param:put/sysParam')")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysParam param) throws ControllerException {
        if (StringUtil.isBlank(param.getId())) {
            SysParam newSysParam = new SysParam();
            newSysParam.setCode(param.getCode());
            List<SysParam> list = sysParamService.getSysParam(newSysParam);
            if (list != null && list.size() > 0) {
                throw new ControllerException(param.getCode() + "已经存在！");
            }
        }
        boolean b = sysParamService.saveOrUpdate(param);
        return b ? Result.succeed("保存成功！") : Result.failed("保存失败");
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/getSysParamByPaged")
    public PageResult<SysParam> getSysParamByPaged(@RequestBody PageRequest<SysParam> t) {
        return sysParamService.getSysParamByPaged(t);
    }

    @ApiOperation("根据条件查询")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/findSysParam")
    public Result getSysParam(@RequestBody SysParam sysParam) {
        List<SysParam> list = sysParamService.getSysParam(sysParam);
        return Result.succeed(list, "");
    }
}

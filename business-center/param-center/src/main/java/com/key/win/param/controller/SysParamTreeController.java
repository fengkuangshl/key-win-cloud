package com.key.win.param.controller;

import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.model.baseData.SysParamTree;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.param.service.SysParamTreeService;
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
@RequestMapping("/sysParamTree")
public class SysParamTreeController {
    @Autowired
    private SysParamTreeService sysParamTreeService;

    @PreAuthorize("hasAuthority('param:get/sysParamTree/{id}')")
    @GetMapping("/get/{id}")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result get(@PathVariable String id) throws ControllerException {
        return Result.succeed(sysParamTreeService.getById(id), "");
    }

    /**
     * 删除菜单
     *
     * @param id
     * @throws ControllerException
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    @PreAuthorize("hasAuthority('param:delete/sysParamTree/{id}')")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result delete(@PathVariable String id) throws ControllerException {
        boolean b = sysParamTreeService.removeById(id);
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
    @PreAuthorize("hasAnyAuthority('param:post/sysParamTree','param:put/sysParamTree')")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysParamTree param) throws ControllerException {
        if (param.getId() != null) {
            SysParamTree newSysParam = new SysParamTree();
            newSysParam.setCode(param.getCode());
            List<SysParamTree> list = sysParamTreeService.getSysParamTree(newSysParam);
            if (list != null && list.size() > 0) {
                throw new ControllerException(param.getCode() + "已经存在！");
            }
        }
        boolean b = sysParamTreeService.saveOrUpdate(param);
        return b ? Result.succeed("保存成功！") : Result.failed("保存失败");
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/getSysParamByPaged")
    public PageResult<SysParamTree> getSysParamTreeByPaged(@RequestBody PageRequest<SysParamTree> t) {
        return sysParamTreeService.getSysParamTreeByPaged(t);
    }

    @ApiOperation("根据条件查询")
    @LogAnnotation(module = "param-center", recordRequestParam = false)
    @PostMapping("/findSysParamTree")
    public Result getSysParamTree(@RequestBody SysParamTree sysParamTree) {
        List<SysParamTree> list = sysParamTreeService.getSysParamTree(sysParamTree);
        return Result.succeed(list, "");
    }
}

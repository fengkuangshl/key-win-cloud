package com.key.win.user.controller;

import com.key.win.common.model.system.SysGroup;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.user.service.SysGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group/*")
@Api("Group相关的api")
public class SysGroupCtrl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String AUTHORITY_PREFIX = "system::group::SysGroup::";

    @Autowired
    private SysGroupService sysGroupService;

    @PostMapping("/findSysGroupByPaged")
    @ApiOperation(value = "Group分页")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::PAGED')")
    public PageResult<SysGroup> findSysGroupByPaged(@RequestBody PageRequest<SysGroup> t) {
        return sysGroupService.findSysGroupByPaged(t);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取Group")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::ID')")
    public Result get(@PathVariable Long id) {
        return Result.succeed(sysGroupService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "DELETE')")
    public Result delete(@PathVariable Long id) {
        boolean b = sysGroupService.deleteById(id);
        return Result.result(b);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "新增/更新")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('" + AUTHORITY_PREFIX + "MODIFY','" + AUTHORITY_PREFIX + "ADD')")
    public Result saveOrUpdate(@RequestBody SysGroup sysGroup) {
        if (StringUtils.isBlank(sysGroup.getCode())) {
            logger.error("组code为空！");
            throw new IllegalArgumentException("组code为空！");
        }
        if (StringUtils.isBlank(sysGroup.getName())) {
            logger.error("组名称为空！");
            throw new IllegalArgumentException("组名称为空！");
        }
        boolean b = sysGroupService.saveOrUpdateGroup(sysGroup);
        return Result.result(b);
    }

    @GetMapping("/getGroupAll")
    @ApiOperation(value = "获取所有Group")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::LIST')")
    public Result getGroupAll() {
        return Result.succeed(sysGroupService.list());
    }
}

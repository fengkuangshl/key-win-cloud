package com.key.win.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.system.SysRole;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.user.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 角色管理
 */

@RestController
@Api(tags = "ROLE API")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 后台管理查询角色
     *
     * @param params
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/roles")
    @ApiOperation(value = "后台管理查询角色")
    @PreAuthorize("hasAuthority('role:get/roles')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public PageResult<SysRole> findRoles(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
//			BizLog.info("角色列表", LogEntry.builder().clazz(this.getClass().getName()).method("findRoles").msg("hello").path("/roles").build());
            return sysRoleService.findRoles(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PostMapping("/getSysRolesByPaged")
    public PageResult<SysRole> getSysRoleByPaged(@RequestBody PageRequest<SysRole> t) {
        return sysRoleService.findSysRoleByPaged(t);
    }

    /**
     * 角色新增或者更新
     *
     * @param sysRole
     * @return
     * @throws ControllerException
     */
    @PostMapping("/roles/saveOrUpdate")
    @PreAuthorize("hasAnyAuthority('role:post/roles','role:put/roles')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysRole sysRole) throws ControllerException {
        if (StringUtils.isBlank(sysRole.getId())) {
            LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysRole::getCode, sysRole.getCode());
            List<SysRole> list = sysRoleService.list(lambdaQueryWrapper);
            if (list != null && list.size() > 0) {
                throw new ControllerException("code已经存在！！");
            }
        }
        boolean b = sysRoleService.saveOrUpdate(sysRole);
        return b ? Result.succeed("保存成功！") : Result.failed("保存失败");
    }

    /**
     * 后台管理删除角色
     * delete /role/1
     *
     * @param id
     * @throws ControllerException
     */
    @DeleteMapping("/roles/{id}")
    @ApiOperation(value = "后台管理删除角色")
    @PreAuthorize("hasAuthority('role:delete/roles/{id}')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result deleteRole(@PathVariable String id) throws ControllerException {
        try {
            if ("1".equals(id)) {
                return Result.failed("管理员不可以删除");
            }
            sysRoleService.removeById(id);
            return Result.succeed("操作成功");
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @PostMapping("/roles/findAllSysRole")
    @ApiOperation(value = "后台管理删除角色")
    @PreAuthorize("hasAuthority('role:findAllSysRole')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findAllSysRole() {
        return Result.succeed(sysRoleService.findAllSysRole(), "");
    }
}

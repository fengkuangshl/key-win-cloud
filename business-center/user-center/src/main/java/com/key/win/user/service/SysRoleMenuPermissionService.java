package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.*;
import com.key.win.common.model.system.SysRoleMenuPermission;

import java.util.List;
import java.util.Set;

public interface SysRoleMenuPermissionService extends IService<SysRoleMenuPermission> {

    PageResult<SysRoleMenuPermission> findSysRoleMenuPermissionByPaged(PageRequest<SysRoleMenuPermission> t);

    List<SysRoleMenuPermission> findSysRoleMenuPermission(SysRoleMenuPermission sysRoleMenuPermission);

    boolean saveOrUpdateSysRoleMenuPermission(SysRoleMenuPermission sysRoleMenuPermission);

    boolean saveOrUpdateSysRoleMenuPermissionForBatch(List<SysRoleMenuPermission> sysRoleMenuPermissions);

    List<SysRoleMenuPermission> findGrantMenus(Long roleId);

    List<SysRoleMenuPermission> findGrantMenuPermissions(Long roleId);

    List<SysRoleMenuPermission> findGrantSysRoleMenuPermissionByRoleId(Long roleId);


    List<SysRoleMenuPermission> findGrantMenus(Set<Long> roleIds);

    List<SysRoleMenuPermission> findGrantMenuPermissions(Set<Long> roleIds);

    List<SysRoleMenuPermission> findGrantSysRoleMenuPermissionByRoleId(Set<Long> roleIds);
}

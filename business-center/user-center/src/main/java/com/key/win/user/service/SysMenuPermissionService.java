package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.*;
import com.key.win.common.model.system.SysMenuPermission;

import java.util.List;
import java.util.Set;

public interface SysMenuPermissionService extends IService<SysMenuPermission> {

    PageResult<SysMenuPermission> findSysMenuPermissionByPaged(PageRequest<SysMenuPermission> t);

    List<SysMenuPermission> findSysMenuPermission(SysMenuPermission sysMenuPermission);

    List<SysMenuPermission> findSysMenuPermissionByChecked(boolean checked);

    List<SysMenuPermission> findSysMenuPermissionByIds(Set<Long> ids);

    boolean saveOrUpdateSysMenuPermission(SysMenuPermission sysMenuPermission);

    boolean saveOrUpdateSysMenuPermissionForBatch(List<SysMenuPermission> sysMenuPermissions);
}

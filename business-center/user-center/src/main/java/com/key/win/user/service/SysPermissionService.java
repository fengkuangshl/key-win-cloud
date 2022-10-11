package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.*;
import com.key.win.common.model.system.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    PageResult<SysPermission> findSysPermissionByPaged(PageRequest<SysPermission> t);

    List<SysPermission> findSysPermission(SysPermission sysPermission);

    boolean saveOrUpdatePermission(SysPermission sysPermission);
}

package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.*;
import com.key.win.common.model.system.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRole> findSysRoleByPaged(PageRequest<SysRole> t);

    List<SysRole> findSysRole(SysRole sysRole);
    
    boolean saveOrUpdateRole(SysRole sysRole);

    boolean deleteById(Long id);
}

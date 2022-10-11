package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.*;
import com.key.win.common.model.system.SysUser;
import com.key.win.user.vo.SysUserVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysUserService extends IService<SysUser> {

    PageResult<SysUser> findSysUserByPaged(PageRequest<SysUser> t);

    List<SysUser> findSysUserByUserName(String userName);

    boolean updateSysUser(SysUserVo sysUser);

    boolean saveSysUser(SysUserVo sysUser);

    boolean modifyPassword(SysUserVo sysUser);

    boolean resetPassword(Long id);

    void setUserToGroup(Long groupId, Set<Long> userIds);

    SysUserVo getUserFullById(Long id);

    boolean deleteById(Long id);

    boolean updateEnabled(SysUser sysUser);
}

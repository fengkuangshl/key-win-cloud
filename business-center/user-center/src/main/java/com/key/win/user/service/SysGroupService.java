package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.model.system.SysGroup;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;

public interface SysGroupService extends IService<SysGroup> {

    PageResult<SysGroup> findSysGroupByPaged(PageRequest<SysGroup> t);

    List<SysGroup> findSysGroup(SysGroup sysGroup);

    List<SysGroup> findSysGroupByParentId(Long parentId);

    List<SysGroup> getGroupTree();

    List<SysGroup> findLeafNode();

    boolean saveOrUpdateGroup(SysGroup sysGroup);

    List<SysGroup> findSysGroupByUserId(Long userId);

    boolean deleteById(Long id);

    SysGroup getSysGroupFullById(Long id);
}

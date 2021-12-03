package com.key.win.param.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.model.baseData.SysParam;
import com.key.win.common.model.baseData.SysParamTree;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;

public interface SysParamTreeService extends IService<SysParamTree> {
    PageResult<SysParamTree> getSysParamTreeByPaged(PageRequest<SysParamTree> t);

    List<SysParamTree> getSysParamTree(SysParamTree sysParamTree);
}

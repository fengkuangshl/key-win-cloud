package com.key.win.param.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.model.baseData.SysParam;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;

public interface SysParamService extends IService<SysParam> {
    PageResult<SysParam> getSysParamByPaged(PageRequest<SysParam> t);

    List<SysParam> getSysParam(SysParam sysParam);
}

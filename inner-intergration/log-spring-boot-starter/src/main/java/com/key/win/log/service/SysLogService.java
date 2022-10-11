package com.key.win.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.log.model.SysLog;

public interface SysLogService extends IService<SysLog> {

    Boolean saveLog(SysLog log);

    Boolean deleteLogById(String id);


    PageResult<SysLog> findSysLogByPaged(PageRequest<SysLog> t);
}
